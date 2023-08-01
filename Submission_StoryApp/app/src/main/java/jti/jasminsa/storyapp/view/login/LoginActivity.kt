package jti.jasminsa.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import jti.jasminsa.storyapp.ViewModelFactory
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.databinding.ActivityLoginBinding
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaActivity
import jti.jasminsa.storyapp.view.Cerita.DetailCerita.DetailCeritaActivity
import jti.jasminsa.storyapp.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore),this))[LoginViewModel::class.java]
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val messageTextView = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(titleTextView, messageTextView, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, loginButton, registerButton)
            startDelay = 500
            start()
        }
    }

    private fun setupAction() {
        binding.passwordEditText.addTextChangedListener {
            val password = binding.passwordEditText.text.toString()
            if (password.length < 8){
                binding.passwordEditTextLayout.error = "Masukkan password lebih dari 8 karakter"
            }else
                binding.passwordEditTextLayout.error = null
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    loginViewModel.login(email, password)
                    loginViewModel.datalogin.observe(this, {
                        when{
                            it!!.error == false ->{
                                val intent = Intent(this, DaftarCeritaActivity::class.java)
                                intent.putExtra(it.loginResult.token, "TOKEN")
                                startActivity(intent)
                                finish()
                            }else ->{
                            Toast.makeText(
                                this,
                                "Passwrord atau Email Salah!!",
                                Toast.LENGTH_SHORT
                            ).show()
                            }
                        }
                    })
                }
            }
        }
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}

