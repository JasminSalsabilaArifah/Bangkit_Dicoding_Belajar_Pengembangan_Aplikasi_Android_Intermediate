package jti.jasminsa.newsapp.ui.list

import androidx.lifecycle.ViewModel
import jti.jasminsa.newsapp.data.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}