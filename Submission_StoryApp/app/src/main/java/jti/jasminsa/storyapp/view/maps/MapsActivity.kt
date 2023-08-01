package jti.jasminsa.storyapp.view.maps

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import jti.jasminsa.storyapp.R
import jti.jasminsa.storyapp.ViewModelFactory
import jti.jasminsa.storyapp.data.UserPreference
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.databinding.ActivityMapsBinding
import jti.jasminsa.storyapp.view.Cerita.DaftarCerita.DaftarCeritaViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userLogin")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var dcViewModel: DaftarCeritaViewModel
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dcViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[DaftarCeritaViewModel::class.java]

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        dcViewModel.getUser().observe(this, { story ->
            dcViewModel.getAllStORY(story.token)
        } )
        dcViewModel.dataStory.observe(this, { loc ->
            addManyMarker(loc)
        })

    }

    private fun addManyMarker(loc: List<ListStoryItem>) {
        loc.forEach { data ->
            val latLng = LatLng(data.lat, data.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(data.name)
                    .snippet(data.description)
            )
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}