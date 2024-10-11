package com.phoenix.newsapp.feature_news.domain.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss_channel")
data class RssFeed(

    @Element val channel: Channel
)
