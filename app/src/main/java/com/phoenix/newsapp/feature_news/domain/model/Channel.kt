package com.phoenix.newsapp.feature_news.domain.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "channel")
data class Channel(
    @PropertyElement val title: String,
    @PropertyElement val description: String,
    @PropertyElement val link: String,
    @Element val item: List<RssItem>
)
