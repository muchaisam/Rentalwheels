package com.msdc.rentalwheels

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.msdc.rentalwheels.auth.RegisterActivity
import com.msdc.rentalwheels.ux.OnboardingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var isReady = false
        splashScreen.setKeepOnScreenCondition { !isReady }

        lifecycleScope.launch {
            val authJob = async(Dispatchers.IO) { checkAuthStatus() }
            val networkJob = async(Dispatchers.IO) { isNetworkConnected() }

            val isLoggedIn = authJob.await()
            val isConnected = networkJob.await()

            isReady = true

            when {
                !isConnected -> showNoInternetDialog()
                isLoggedIn -> navigateTo(HomeActivity::class.java)
                else -> checkAndNavigateToOnboarding()
            }
        }
    }

    private suspend fun checkAuthStatus(): Boolean {
        return try {
            FirebaseAuth.getInstance().currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.getNetworkCapabilities(cm.activeNetwork)?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } ?: false
    }

    private fun showNoInternetDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun checkAndNavigateToOnboarding() {
        val sharedPref = getSharedPreferences("Onboarding", Context.MODE_PRIVATE)
        val shown = sharedPref.getBoolean("Shown", false)

        if (!shown) {
            navigateTo(OnboardingActivity::class.java)
        } else {
            navigateTo(RegisterActivity::class.java)
        }
    }

    private fun navigateTo(destinationClass: Class<*>) {
        startActivity(Intent(this, destinationClass))
        finish()
    }
}