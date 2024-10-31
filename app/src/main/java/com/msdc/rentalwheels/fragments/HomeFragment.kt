package com.msdc.rentalwheels.fragments

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
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.msdc.rentalwheels.BuildConfig
import com.msdc.rentalwheels.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.data.repository.CarRepository
import com.msdc.rentalwheels.databinding.FragmentHomeBinding
import com.msdc.rentalwheels.ui.screens.CarRentalApp
import com.msdc.rentalwheels.viewmodel.CarViewModel
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentHomeBinding? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val TAG = HomeFragment::class.java.simpleName
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding
    private lateinit var carViewModel: CarViewModel


    private val defaultLocation = LatLng(1.2921, 36.8219)
    private val DEFAULT_ZOOM = 15f
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null

    // Shared Preferences
    private val SHARED_PREFS = "shared_prefs"
    private val FIRST_NAME = "first_name"

    private lateinit var viewModel: CarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        setupGreetings()
        setupViewModel()
        setupComposeView()
    }

    private fun setupGreetings() {
        auth.currentUser?.let { user ->
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: "User"
                        updateGreetingText(firstName)
                    } else {
                        updateGreetingText("User")
                    }
                }
                .addOnFailureListener { e ->
                    updateGreetingText("User")
                    Toast.makeText(context, "Failed to fetch user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: updateGreetingText(" ") // If no user is signed in
    }

    private fun updateGreetingText(firstName: String) {
        val timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.greetings.text = when (timeOfDay) {
            in 0..11 -> "Good Morning, $firstName"
            in 12..15 -> "Good Afternoon, $firstName"
            else -> "Hello, $firstName"
        }
    }

    private fun setupViewModel() {
        val firestore = FirebaseFirestore.getInstance()
        val repository = CarRepository(firestore)
        val factory = CarViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, factory)[CarViewModel::class.java]
    }

    private fun setupComposeView() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                val carDetailState = viewModel.carDetailState.collectAsState()

                CarRentalApp(
                    uiState = uiState.value,
                    carDetailState = carDetailState.value,
                    onCarClick = { carId -> viewModel.loadCarDetails(carId) },
                    onLoadMore = { viewModel },
                    onBackClick = { /* Handle back navigation */ }
                )
            }
        }
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