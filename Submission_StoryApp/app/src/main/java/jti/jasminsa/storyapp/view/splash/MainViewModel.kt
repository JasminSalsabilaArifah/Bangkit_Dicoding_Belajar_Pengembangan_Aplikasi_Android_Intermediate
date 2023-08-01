package jti.jasminsa.storyapp.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }
}