package com.phoenix.ainformation.feature_news.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phoenix.ainformation.feature_news.presentation.main_screen.MainScreen
import com.phoenix.ainformation.feature_news.presentation.news_detail_screen.NewsDetailScreen
import com.phoenix.ainformation.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme(dynamicColor = false) {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        composable<Home> {
                            MainScreen(
                                onNavigationToNewsDetail = { item ->
                                    navController.navigate(NewsDetail(item.title, item.description, item.pubDate, item.imageUrl))
                                },
                                onNavigationToAISummaryScreen = { item, summary ->
                                    navController.navigate(NewsDetail(item.title, summary, item.pubDate, item.imageUrl))
                                }
                            )
                        }
                        composable<NewsDetail>(
                            enterTransition = { slideInVertically(initialOffsetY = { it }) },
                            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                            popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
                        ) { backStackEntry ->
                            val newsDetail: NewsDetail = backStackEntry.toRoute()
                            NewsDetailScreen(
                                onNavigateUp = { navController.navigateUp() },
                                newsDetail.title, newsDetail.description, newsDetail.pubDate, newsDetail.imageUrl)
                        }
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