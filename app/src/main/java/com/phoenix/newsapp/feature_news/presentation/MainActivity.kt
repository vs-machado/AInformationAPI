package com.phoenix.newsapp.feature_news.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phoenix.newsapp.feature_news.presentation.main_screen.MainScreen
import com.phoenix.newsapp.feature_news.presentation.news_detail_screen.NewsDetailScreen
import com.phoenix.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Home
                ) {
                    composable<Home> {
                        MainScreen(onNavigationToNewsDetail = { item ->
                            navController.navigate(NewsDetail(item.title, item.description, item.pubDate, item.imageUrl))
                        })
                    }
                    composable<NewsDetail>(
                        enterTransition = { slideInVertically(initialOffsetY = { it }) },
                        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                        popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
                    ) { backStackEntry ->
                        val newsDetail: NewsDetail = backStackEntry.toRoute()
                        NewsDetailScreen(newsDetail.title, newsDetail.description, newsDetail.pubDate, newsDetail.imageUrl)
                    }
                }
            }
        }
    }

    @Serializable
    object Home

    @Serializable
    data class NewsDetail(
        val title: String,
        val description: String,
        val pubDate: String,
        val imageUrl: String?
    )
}