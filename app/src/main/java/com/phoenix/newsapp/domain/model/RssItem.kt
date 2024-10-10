package com.phoenix.newsapp.domain.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class RssItem(

    @PropertyElement
    var title: String? = null,

    @PropertyElement
    var link: String? = null,

    @PropertyElement
    var description: String? = null,

    @PropertyElement
    var pubDate: String? = null
)
