package com.phoenix.newsapp.feature_news.domain.util

import com.phoenix.newsapp.feature_news.domain.model.RssItem
import org.jsoup.Jsoup

class Parser {

    // Update the imageUrl if a matcher exists in the description field
    fun extractImageUrls(items: List<RssItem>): List<RssItem> {
        return items.map { item ->
            item.description.let { description ->
                // Decode HTML entities
                val decodedHtml = description
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&quot;", "\"")

                val document = Jsoup.parse(decodedHtml)
                val imgTag = document.select("img").first()

                item.imageUrl = imgTag?.attr("src")
            }
            item
        }
    }
}