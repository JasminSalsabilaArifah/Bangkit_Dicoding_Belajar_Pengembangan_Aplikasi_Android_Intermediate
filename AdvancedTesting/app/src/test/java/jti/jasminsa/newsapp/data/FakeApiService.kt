package jti.jasminsa.newsapp.data

import jti.jasminsa.newsapp.data.remote.response.NewsResponse
import jti.jasminsa.newsapp.data.remote.retrofit.ApiService
import jti.jasminsa.newsapp.utils.DataDummy

class FakeApiService : ApiService {
    private val dummyResponse = DataDummy.generateDummyNewsResponse()
    override suspend fun getNews(apiKey: String): NewsResponse {
        return dummyResponse
    }
}