package com.example.rentalwheels.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rentalwheels.BuildConfig
import com.example.rentalwheels.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.card.MaterialCardView
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var greetings: TextView
    private lateinit var card: MaterialCardView
    private lateinit var card1: MaterialCardView
    private lateinit var card2: MaterialCardView

    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient

    private val defaultLocation = LatLng(1.2921, 36.8219)
    private val DEFAULT_ZOOM = 15f
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    // Shared Preferences
    private val SHARED_PREFS = "shared_prefs"
    private val USER_NAME = "user_name"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        greetings = view.findViewById(R.id.greetings)
        card = view.findViewById(R.id.card)
        card1 = view.findViewById(R.id.card1)
        card2 = view.findViewById(R.id.card2)

        setupGreetings()
        setupCardClickListeners()

        // Initialize Places
        context?.let {
            Places.initialize(it.applicationContext, BuildConfig.MAPS_API_KEY)
            placesClient = Places.createClient(it)
        }

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set up the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    private fun setupGreetings() {
        val sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(USER_NAME, "User")

        val timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        greetings.text = when (timeOfDay) {
            in 0..11 -> "Good Morning, $username"
            in 12..15 -> "Good Afternoon, $username"
            else -> "Good Evening, $username"
        }
    }

    private fun setupCardClickListeners() {
        val cardClickListener = View.OnClickListener {
            // TODO: Replace with proper navigation using Navigation Component
            // startActivity(Intent(requireContext(), Moreinfo::class.java))
        }
        card.setOnClickListener(cardClickListener)
        card1.setOnClickListener(cardClickListener)
        card2.setOnClickListener(cardClickListener)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Apply custom map style
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style)
        mMap.setMapStyle(mapStyleOptions)

        // Set custom info window adapter
        mMap.setInfoWindowAdapter(createInfoWindowAdapter())

        // Get location permission and update UI
        getLocationPermission()
        updateLocationUI()

        // Get device location
        getDeviceLocation()
    }

    private fun createInfoWindowAdapter(): GoogleMap.InfoWindowAdapter {
        return object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? = null

            override fun getInfoContents(marker: Marker): View {
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents, null)
                infoWindow.findViewById<TextView>(R.id.title).text = marker.title
                infoWindow.findViewById<TextView>(R.id.snippet).text = marker.snippet
                return infoWindow
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun updateLocationUI() {
        if (!::mMap.isInitialized) return
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}", e)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude),
                                DEFAULT_ZOOM
                            ))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: ${task.exception}")
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exception: ${e.message}", e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}