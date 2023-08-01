package jti.jasminsa.newsapp.di

import android.content.Context
import jti.jasminsa.newsapp.data.NewsRepository
import jti.jasminsa.newsapp.data.local.room.NewsDatabase
import jti.jasminsa.newsapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}