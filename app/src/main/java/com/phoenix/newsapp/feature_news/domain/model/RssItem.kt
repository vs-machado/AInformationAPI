package com.phoenix.newsapp.feature_news.domain.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class RssItem(

    @PropertyElement
    var title: String? = null,

    @PropertyElement
    var link: String? = null,

    @PropertyElement
    var description: String? = null,

    @PropertyElement
    var pubDate: String? = null,

) {
    // imageUrl is inside the description field and needs to be parsed
    @Transient
    var imageUrl: String? = null
}
