package com.phoenix.newsapp.domain.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "channel")
data class RssChannel(

    @PropertyElement(name = "item")
    var items: List<RssItem>? = null
)
