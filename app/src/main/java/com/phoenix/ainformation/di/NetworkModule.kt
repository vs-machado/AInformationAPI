package com.phoenix.ainformation.di

import com.phoenix.ainformation.feature_news.data.api.BASE_URL
import com.phoenix.ainformation.feature_news.data.api.RssApiService
import com.phoenix.ainformation.feature_news.data.repository.NewsRepositoryImpl
import com.phoenix.ainformation.feature_news.domain.model.repository.NewsRepository
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RssApiService {
        return retrofit.create(RssApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: RssApiService): NewsRepository {
        return NewsRepositoryImpl(apiService)
    }
}