package com.phoenix.newsapp.feature_news.presentation.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.phoenix.newsapp.BuildConfig
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

/**
 * MainScreenViewModel is responsible for managing the state and business logic
 * for the main screen of the news app. It handles fetching and filtering news items,
 * pagination, search functionality, and generating AI summaries for news articles.
 *
 * @param newsRepository The repository used to fetch news items.
 */
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val model: GenerativeModel
): ViewModel() {

    private val _feedState = MutableStateFlow<FeedState>(FeedState.Initial)
    val feedState: StateFlow<FeedState> = _feedState.asStateFlow()

    // Verifies if the Gemini response was correctly fetched and handles the app actions.
    private val _aiSummaryState = MutableStateFlow<AISummaryState>(AISummaryState.Initial)
    val aiSummaryState: StateFlow<AISummaryState> = _aiSummaryState.asStateFlow()

    // Ensures that the AI summary displayed in the screen corresponds to the last generated AI response.
    private val _currentSummarizedItemId = MutableStateFlow("")
    val currentSummarizedItemId: StateFlow<String?> = _currentSummarizedItemId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    var state by mutableStateOf(ScreenState())

    // News feed filter.
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

    // Updates aiSummarizedState with the latest generated AI response
    fun generateAISummary(newsContent: String, userLanguage: String, itemId: String) {
        viewModelScope.launch {
            _aiSummaryState.value = AISummaryState.Loading
            _currentSummarizedItemId.value = itemId

            runCatching {
                val response = model.generateContent("Write a summary about this news in $userLanguage that not exceeds 50 lines. In the beginning of the text," +
                        "you must write [AI GENERATED SUMMARY], then put the summary one line below. Write it like a website report. Use paragraphs and indentation" +
                        "when needed. Ensure that the summary reduces the quantity of lines in at least 30% when the news has more than 20 lines." +
                        "If the author's name is in the news, include it in the first line by writing Publisher: (publisher name) ." +
                        "The news: $newsContent")

                if(response.candidates.isNotEmpty()){
                    response.text ?: "No summary generated"
                } else {
                    throw Exception("No valid response generated")
                }

            }.onSuccess { summary ->
                _aiSummaryState.value = AISummaryState.Success(summary)
            }.onFailure { error ->
                _aiSummaryState.value = AISummaryState.Error(error.message ?: "Unknown error occurred")
                _currentSummarizedItemId.value = ""
            }
        }
    }

    fun resetAISummaryState() {
        _aiSummaryState.value = AISummaryState.Initial
        _currentSummarizedItemId.value = ""
    }

    // Used to handle lazycolumn pagination
    data class ScreenState(
        val isLoading: Boolean = false,
        val items: List<RssItem> = emptyList(),
        val error: String? = null,
        val endReached: Boolean = false,
        val page: Int = 0
    )
}