package jti.jasminsa.storyapp.view.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import jti.jasminsa.storyapp.R
import jti.jasminsa.storyapp.ViewModelFactory
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaActivity
import jti.jasminsa.storyapp.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            setupViewModel()
            finish()
        }, 2000)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[MainViewModel::class.java]
        mainViewModel.getUser().observe(this, { user ->
            if (user.token.isEmpty()){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, DaftarCeritaActivity::class.java))
                finish()
            }
        })
    }
}