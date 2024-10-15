package com.phoenix.newsapp.feature_news.presentation.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.newsapp.feature_news.data.repository.DefaultPaginator
import com.phoenix.newsapp.feature_news.domain.model.RssItem
import com.phoenix.newsapp.feature_news.domain.model.repository.NewsRepository
import com.phoenix.newsapp.feature_news.domain.util.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _feedState = MutableStateFlow<FeedState>(FeedState.Initial)
    val feedState: StateFlow<FeedState> = _feedState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    var state by mutableStateOf(ScreenState())

    val filteredFeed = combine(feedState, searchQuery) { state, query ->
        when(state){
            is FeedState.Success -> {
                state.copy(items = state.items.filter { item ->
                    item.title.contains(query, ignoreCase = true) ||
                            item.description.contains(query, ignoreCase = true)
                })
            }
            else -> state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), FeedState.Initial)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Used to fetch the news feed during scrolling
    private val paginator = DefaultPaginator (
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            newsRepository.getItems(nextPage, 10)
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                items = state.items + items,
                page = newKey,
                endReached = items.isEmpty()
            )
        }
    )

    /* When user is scrolling the feed the state is not changed to Loading to avoid showing
     * the circular progress indicator used when launching the app. */
    fun fetchRssFeed(isScrolling: Boolean) {
        viewModelScope.launch {
            if(!isScrolling){
                _feedState.value = FeedState.Loading
            }

            runCatching {
                paginator.loadNextItems()
            }.onSuccess {
                val processedItems = Parser().extractImageUrls(state.items)
                _feedState.value = FeedState.Success(processedItems)
            }.onFailure { error ->
                _feedState.value = FeedState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    init {
        fetchRssFeed(isScrolling = false)
    }

    data class ScreenState(
        val isLoading: Boolean = false,
        val items: List<RssItem> = emptyList(),
        val error: String? = null,
        val endReached: Boolean = false,
        val page: Int = 0
    )
}