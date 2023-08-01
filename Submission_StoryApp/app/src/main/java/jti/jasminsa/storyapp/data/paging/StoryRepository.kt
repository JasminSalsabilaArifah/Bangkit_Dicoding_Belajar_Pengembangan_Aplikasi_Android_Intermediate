package jti.jasminsa.storyapp.data.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.response.DaftarCeritaResponse
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.data.response.LoginResult
import jti.jasminsa.storyapp.data.retrofit.ApiService
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaViewModel

class StoryRepository(private val apiService: ApiService,private val token: UserPreference) {

    fun getUser(): LiveData<LoginResult> {
        return token.getUser().asLiveData()
    }

    fun getStorie(token: String?): LiveData<PagingData<ListStoryItem>> {
//        Log.e(DaftarCeritaViewModel.TAG, "wrong 11: ${response.message()}")
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token!!)
            }
        ).liveData
    }
}