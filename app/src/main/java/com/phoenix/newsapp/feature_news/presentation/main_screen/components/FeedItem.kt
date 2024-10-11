package com.phoenix.newsapp.feature_news.presentation.main_screen.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.phoenix.newsapp.feature_news.domain.model.RssItem

@Composable
fun FeedItem(item: RssItem) {
    Text("${item.imageUrl}")
}

@Preview
@Composable
fun PreviewFeedItem() {
    FeedItem(
        RssItem("Title", "http://google.com/", "Hello world!", "10/10/2024")
    )
}