package com.phoenix.newsapp.feature_news.domain.model.repository

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}