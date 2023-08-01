package jti.jasminsa.storyapp.view.Cerita.DaftarCerita

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import jti.jasminsa.storyapp.R
import jti.jasminsa.storyapp.ViewModelFactory
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.databinding.ActivityDaftarCeritaBinding
import jti.jasminsa.storyapp.databinding.ActivityMainBinding
import jti.jasminsa.storyapp.view.Cerita.DetailCerita.DetailCeritaActivity
import jti.jasminsa.storyapp.view.Cerita.TambahCerita.TambahCeritaActivity
import jti.jasminsa.storyapp.view.login.LoginActivity
import jti.jasminsa.storyapp.view.maps.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class DaftarCeritaActivity : AppCompatActivity() {

    companion object {
        const val TOKEN = "token"
    }

    private lateinit var binding: ActivityDaftarCeritaBinding
    private lateinit var dcViewModel: DaftarCeritaViewModel
//    private val dcViewModel: DaftarCeritaViewModel by viewModels {
//        ViewModelFactory(UserPreference.getInstance(dataStore),this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarCeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dcViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[DaftarCeritaViewModel::class.java]
//        dcViewModel.getUser().observe(this, { story ->
//            dcViewModel.getAllStORY(story.token)
//        } )
//        dcViewModel.dataStory.observe(this, { user ->
//            setStory(user)
//        })
        binding.rvStoryList.layoutManager = LinearLayoutManager(this)
        val adapter = ListStoryAdater()
        binding.rvStoryList.adapter = adapter
        dcViewModel.story.observe(this, {
            Log.e("TAG", "TAGTAG")
            adapter.submitData(lifecycle, it)
        })
        setupAction()
    }

    private fun setupAction() {
        binding.topAppBar.setOnMenuItemClickListener  {
            when (it.itemId){
                R.id.logout -> {
                    dcViewModel.logout()
                    Log.e("TAG", "onFailure9: onFailure9: onFailure9: ")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.addStory -> {
                    val intent = Intent(this, TambahCeritaActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
            }
                else -> false
            }
        }
    }

//    private fun setStory(user: List<ListStoryItem>) {
//        val adapter = ListStoryAdater(user)
//        binding.rvStoryList.adapter = adapter
//    }
}