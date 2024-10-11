package com.phoenix.newsapp.feature_news.domain.util

import com.phoenix.newsapp.feature_news.domain.model.RssItem
import java.util.regex.Pattern

class Parser {

    // Update the imageUrl if a matcher exists in the description field
    fun extractImageUrls(items: List<RssItem>): List<RssItem> {

        val imgPattern = Pattern.compile("<img\\s+src=\"([^\"]+)\"")

        return items.map { item ->
            val matcher = item.description?.let { imgPattern.matcher(it) }

            if (matcher != null) {
                if(matcher.find()) {
                    item.imageUrl = matcher.group(1) ?: "" // Group(1) returns only the content inside the <img> tag
                }
            }
            item
        }
    }
}