package com.example.rentalwheels

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.rentalwheels.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.card.MaterialCardView
import java.util.*

import com.example.rentalwheels.BuildConfig
import com.example.rentalwheels.modules.Moreinfo

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var greetings: TextView
    private lateinit var card: MaterialCardView
    private lateinit var card1: MaterialCardView
    private lateinit var card2: MaterialCardView

    // Constant keys for shared preferences
    private val SHARED_PREFS = "shared_prefs"
    private val USER_NAME = "user_name"

    private val TAG = MapsActivity::class.java.simpleName
    private lateinit var map: GoogleMap
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Nairobi) and default zoom to use when location permission is not granted.
    private val defaultLocation = LatLng(1.2921, 36.8219)
    private val DEFAULT_ZOOM = 15f
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    // Keys for storing activity state.
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"

    // Used for selecting the current place.
    private val M_MAX_ENTRIES = 5
    private lateinit var likelyPlaceNames: Array<String?>
    private lateinit var likelyPlaceAddresses: Array<String?>
    private lateinit var likelyPlaceAttributions: Array<List<*>?>
    private lateinit var likelyPlaceLatLngs: Array<LatLng?>

    private lateinit var sharedPreferences: SharedPreferences
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps)

        // Initializing shared prefs
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        // Getting data and storing it
        username = sharedPreferences.getString(USER_NAME, null)

        // Greetings stuff
        greetings = findViewById(R.id.greetings)
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        greetings.text = when (timeOfDay) {
            in 0..11 -> "Good Morning, $username"
            in 12..15 -> "Good Afternoon, $username"
            in 16..23 -> "Good Evening, $username"
            else -> greetings.text
        }

        card = findViewById(R.id.card)
        card1 = findViewById(R.id.card1)
        card2 = findViewById(R.id.card2)
        card.setOnClickListener { startActivity(Intent(this@MapsActivity, Moreinfo::class.java)) }
        card1.setOnClickListener { startActivity(Intent(this@MapsActivity, Moreinfo::class.java)) }
        card2.setOnClickListener { startActivity(Intent(this@MapsActivity, Moreinfo::class.java)) }

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        // Construct a PlacesClient
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Build the map.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map.let {
            outState.putParcelable(KEY_CAMERA_POSITION, it.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.current_place_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_get_place) {
            showCurrentPlace()
        }
        return true
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        mMap = map
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style)
        mMap.setMapStyle(mapStyleOptions)

        // Use a custom info window adapter to handle multiple lines of text in the info window contents.
        this.map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents, findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })

        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        lastKnownLocation?.let {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), DEFAULT_ZOOM))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message ?: "Unknown error", e)
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun showCurrentPlace() {
        if (map == null) {
            return
        }

        if (locationPermissionGranted) {
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val placeResult = placesClient.findCurrentPlace(request)
                placeResult.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val likelyPlaces = task.result

                        val count = if (likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                            likelyPlaces.placeLikelihoods.size
                        } else {
                            M_MAX_ENTRIES
                        }

                        likelyPlaceNames = arrayOfNulls(count)
                        likelyPlaceAddresses = arrayOfNulls(count)
                        likelyPlaceAttributions = arrayOfNulls(count)
                        likelyPlaceLatLngs = arrayOfNulls(count)

                        for ((i, placeLikelihood) in likelyPlaces.placeLikelihoods.withIndex()) {
                            likelyPlaceNames[i] = placeLikelihood.place.name
                            likelyPlaceAddresses[i] = placeLikelihood.place.address
                            likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                            likelyPlaceLatLngs[i] = placeLikelihood.place.latLng

                            if (i > count - 1) {
                                break
                            }
                        }

                        openPlacesDialog()
                    } else {
                        Log.e(TAG, "Exception: %s", task.exception)
                    }
                }
            }
        } else {
            Log.i(TAG, "The user did not grant location permission.")
            map.addMarker(MarkerOptions().title(getString(R.string.default_info_title)).position(defaultLocation).snippet(getString(R.string.default_info_snippet)))
            getLocationPermission()
        }
    }

    private fun openPlacesDialog() {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = "$markerSnippet\n${likelyPlaceAttributions[which]}"
            }

            map.addMarker(MarkerOptions().title(likelyPlaceNames[which]).position(markerLatLng!!).snippet(markerSnippet).icon(BitmapDescriptorFactory.fromResource(R.drawable.markeer)))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, DEFAULT_ZOOM))
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message ?: "Unknown error")
        }
    }
}