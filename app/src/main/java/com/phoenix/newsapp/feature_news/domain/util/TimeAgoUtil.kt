package com.phoenix.newsapp.feature_news.domain.util

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeAgoUtil {

    fun getTimeAgo(pubDate: String): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val publishedDate: Date? = dateFormat.parse(pubDate)

        publishedDate?.let {
            val currentTime = Date()
            val differenceInMillis = currentTime.time - it.time

            val secondsInMillis = 1000
            val minutesInMillis = secondsInMillis * 60
            val hoursInMillis = minutesInMillis * 60
            val daysInMillis = hoursInMillis * 24

            val daysAgo = differenceInMillis / daysInMillis
            val hoursAgo = (differenceInMillis % daysInMillis) / hoursInMillis

            return when {
                daysAgo > 0 -> "${daysAgo}d ago"
                hoursAgo > 0 -> "${hoursAgo}h ago"
                else -> "Just now"
            }
        }

        return ""

    }

}