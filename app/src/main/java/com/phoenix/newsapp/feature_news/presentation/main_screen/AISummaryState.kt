package com.phoenix.newsapp.feature_news.presentation.main_screen

sealed class AISummaryState {
    object Initial: AISummaryState()
    object Loading: AISummaryState()
    data class Success(val summary: String): AISummaryState()
    data class Error(val message: String): AISummaryState()
}