package com.phoenix.newsapp.feature_news.presentation.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.newsapp.feature_news.domain.model.repository.NewsRepository
import com.phoenix.newsapp.feature_news.domain.util.Parser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun fetchRssFeed() {
        viewModelScope.launch {
            _feedState.value = FeedState.Loading

            runCatching {
                newsRepository.getRssFeed()
            }.onSuccess { feed ->
                Log.d("debug", feed.toString())
                val processedItems = Parser().extractImageUrls(feed.channel.item)
                _feedState.value = FeedState.Success(processedItems)
            }.onFailure { error ->
                Log.d("debug", error.message.toString())
                _feedState.value = FeedState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }
}