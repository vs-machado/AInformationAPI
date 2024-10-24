package com.phoenix.ainformation.feature_news.presentation.main_screen

import com.phoenix.ainformation.feature_news.domain.model.RssItem


sealed class FeedState {
    object Initial: FeedState()
    object Loading: FeedState()
    data class Success(val items: List<RssItem>): FeedState()
    data class Error(val message: String): FeedState()
}