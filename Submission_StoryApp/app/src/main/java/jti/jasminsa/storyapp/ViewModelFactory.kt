package jti.jasminsa.storyapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.paging.StoryRepository
import jti.jasminsa.storyapp.data.retrofit.ApiConfig
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaActivity.Companion.TOKEN
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaViewModel
import jti.jasminsa.storyapp.view.login.LoginViewModel
import jti.jasminsa.storyapp.view.splash.MainViewModel

class ViewModelFactory(private val pref: UserPreference,private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }modelClass.isAssignableFrom(DaftarCeritaViewModel::class.java) -> {
                DaftarCeritaViewModel(pref,Injection.provideRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}
object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN)

    @SuppressLint("SuspiciousIndentation")
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val token = UserPreference.getInstance(context.dataStore)
        return StoryRepository(apiService, token)
    }
}

