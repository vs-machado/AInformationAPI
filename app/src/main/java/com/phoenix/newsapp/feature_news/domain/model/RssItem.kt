package com.phoenix.newsapp.feature_news.domain.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class RssItem(

    @PropertyElement
    val title: String,

    @PropertyElement
    val link: String,

    @PropertyElement
    val description: String,

    @PropertyElement
    val pubDate: String,

) {
    // imageUrl is inside the description field and needs to be parsed
    @Transient
    var imageUrl: String? = null
}
