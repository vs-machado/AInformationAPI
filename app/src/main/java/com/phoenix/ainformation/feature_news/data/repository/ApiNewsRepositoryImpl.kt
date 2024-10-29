package com.phoenix.ainformation.feature_news.data.repository

import android.util.Log
import com.phoenix.ainformation.BuildConfig
import com.phoenix.ainformation.feature_news.data.api.NewsDataApiService
import com.phoenix.ainformation.feature_news.domain.model.news_api.NewsArticle
import com.phoenix.ainformation.feature_news.domain.model.news_api.repository.ApiNewsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// Fetch data from NewsData.io API
class ApiNewsRepositoryImpl @Inject constructor(
    private val api: NewsDataApiService
): ApiNewsRepository {

    override suspend fun getLatestNews(page: String?): Result<List<NewsArticle>> {
        return try {
            val response = api.getLatestNews(
                apiKey = BuildConfig.newsDataApiKey,
                nextPage = page
            )
            Log.d("ApiNewsRepository", "API Response: $response")
            Log.d("ApiNewsRepository", "Response Status: ${response.status}")
            Log.d("ApiNewsRepository", "API Key: ${BuildConfig.newsDataApiKey}")
            Log.d("ApiNewsRepository", "Next Page: $page")

            if (response.status == "success") {
                // Log the raw JSON response
                Log.d("ApiNewsRepository", "Raw JSON response: ${response.results}")
                Result.success(response.results)
            } else {
                Log.e("ApiNewsRepository", "API returned non-success status: ${response.status}")
                Result.failure(Exception("API returned non-success status: ${response.status}"))
            }

        } catch (e: HttpException) {
            Log.e("ApiNewsRepository", "HttpException: ${e.message}", e)
            Log.e("ApiNewsRepository", "Response code: ${e.code()}")
            Log.e("ApiNewsRepository", "Response body: ${e.response()?.errorBody()?.string()}")
            Result.failure(e)
        } catch (e: IOException) {
            Log.e("ApiNewsRepository", "IOException: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("ApiNewsRepository", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}