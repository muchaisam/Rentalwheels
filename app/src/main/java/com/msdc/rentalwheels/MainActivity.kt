package com.msdc.rentalwheels

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.msdc.rentalwheels.auth.RegisterActivity
import com.msdc.rentalwheels.ux.OnboardingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setOnExitAnimationListener { splashScreenViewProvider ->
            // Set the theme back to the normal theme
            setTheme(R.style.Theme_Theesafari)
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500) // Wait for 1.5 seconds before checking the internet connection

            if (!isNetworkConnected()) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again.")
                    .setPositiveButton(
                        android.R.string.ok,
                        null
                    ) // Don't finish the activity when the OK button is clicked
                    .show()
            } else {
                val sharedPref = getSharedPreferences("Onboarding", Context.MODE_PRIVATE)
                val shown = sharedPref.getBoolean("Shown", false)

                if (!shown) {
                    // Start OnboardingActivity
                    val intent = Intent(this@MainActivity, OnboardingActivity::class.java)
                    startActivity(intent)
                } else {
                    // Start LoginActivity
                    val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}