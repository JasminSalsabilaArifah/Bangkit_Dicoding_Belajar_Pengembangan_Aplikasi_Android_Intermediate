package jti.jasminsa.storyapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.response.LoginResponse
import jti.jasminsa.storyapp.data.response.LoginResult
import jti.jasminsa.storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _datalogin = MutableLiveData<LoginResponse?>()
    val datalogin: MutableLiveData<LoginResponse?> = _datalogin

    companion object {
        private const val TAG = "Login View Model"
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (!responseBody!!.error) {
                        _datalogin.value = responseBody
                        viewModelScope.launch {
                            pref.saveUser(responseBody.loginResult)
                        }
                    }
                } else {
                    Log.e(TAG, "wrong 1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "wrong 2: ${t.message}")
            }
        })
    }
}