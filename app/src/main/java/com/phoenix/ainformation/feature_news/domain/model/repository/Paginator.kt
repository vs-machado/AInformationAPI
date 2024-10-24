package com.phoenix.ainformation.feature_news.domain.model.repository

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}