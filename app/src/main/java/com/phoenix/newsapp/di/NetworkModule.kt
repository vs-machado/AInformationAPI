package com.phoenix.newsapp.di

import com.phoenix.newsapp.data.api.BASE_URL
import com.phoenix.newsapp.data.api.RssApiService
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.adaptivity.xmlutil.serialization.KotlinxSerializationProvider
import okhttp3.OkHttpClient
import org.xmlpull.v1.XmlPullParserFactory
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
}