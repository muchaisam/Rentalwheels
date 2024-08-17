package com.example.rentalwheels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    // Variable to track if the back button was pressed twice
    private var doubleBackToExitPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        // Set up the navigation components
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNav, navController)

        // Handle the back button press to exit the app
        onBackPressedDispatcher.addCallback(this@HomeActivity) {
            if (doubleBackToExitPressed) {
                finish()
            } else {
                doubleBackToExitPressed = true
                Toast.makeText(this@HomeActivity, "Tap again to exit", Toast.LENGTH_SHORT)
                    .show()
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressed = false },
                    1000
                )
            }
        }
    }
}