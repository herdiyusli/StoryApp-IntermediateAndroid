package com.herdi.yusli.herdistoryapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.herdi.yusli.herdistoryapp.R
import com.herdi.yusli.herdistoryapp.data.User
import com.herdi.yusli.herdistoryapp.databinding.ActivityMapsStoryBinding
import com.herdi.yusli.herdistoryapp.model.MainViewModel
import com.herdi.yusli.herdistoryapp.model.MapStoryViewModel
import com.herdi.yusli.herdistoryapp.preference.AuthPreference
import com.herdi.yusli.herdistoryapp.preference.AuthViewModelFactory

class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsStoryBinding
    private lateinit var pref: AuthPreference
    private lateinit var viewModel: MapStoryViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Maps Story"
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MapStoryViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val bekasi = LatLng(-6.357074472277798, 107.17541626452433)
        mMap.addMarker(MarkerOptions().position(bekasi).title("Planet Bekasi"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bekasi))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()

        pref = AuthPreference.getInstance(dataStore)
        mainViewModel =
            ViewModelProvider(this, AuthViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getToken().observe(this)
        { user: User ->
            if (user.token != "") {
                viewModel.getMapStory(user.token)
            }
        }

        viewModel.listStory.observe(this) {
            for (i in it) {
                val location = LatLng(i.lat, i.lon)
                mMap.addMarker(MarkerOptions().position(location).title(i.name))
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}