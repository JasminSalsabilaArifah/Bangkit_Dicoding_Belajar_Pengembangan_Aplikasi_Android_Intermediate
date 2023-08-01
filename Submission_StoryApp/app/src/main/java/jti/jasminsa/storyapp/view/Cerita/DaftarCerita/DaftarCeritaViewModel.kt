package jti.jasminsa.storyapp.view.Cerita.DaftarCerita

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.paging.StoryRepository
import jti.jasminsa.storyapp.data.response.DaftarCeritaResponse
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.data.response.LoginResult
import jti.jasminsa.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarCeritaViewModel(private val pref: UserPreference,storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dataStory = MutableLiveData<List<ListStoryItem>>()
    val dataStory: MutableLiveData<List<ListStoryItem>> = _dataStory

    companion object {
        private const val TAG = "Login View Model"
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    val token: LiveData<LoginResult> = storyRepository.getUser()

    val story: LiveData<PagingData<ListStoryItem>> =token.switchMap  {
        storyRepository.getStorie(it.token).cachedIn(viewModelScope)
    }


    fun getAllStORY(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStory("Bearer $token")
        client.enqueue(object : Callback<DaftarCeritaResponse> {
            override fun onResponse(
                call: Call<DaftarCeritaResponse>,
                response: Response<DaftarCeritaResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (!responseBody!!.error) {
                        _dataStory.value = responseBody.listStory
                    }
                } else {
                    Log.e(TAG, "wrong 11: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DaftarCeritaResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "wrong 2: ${t.message}")
            }
        })
    }
}