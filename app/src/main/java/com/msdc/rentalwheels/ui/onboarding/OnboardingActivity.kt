package com.msdc.rentalwheels.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.auth.LoginActivity
import com.msdc.rentalwheels.ui.theme.RentalWheelsTheme
import com.msdc.rentalwheels.ui.theme.rememberThemeState

class OnboardingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        setContent {
            val themeState = rememberThemeState()
            RentalWheelsTheme(themeState = themeState) {
                OnboardingApp(
                    onComplete = { completeOnboarding() },
                    onSkip = { completeOnboarding() }
                )
            }
        }
    }

    private fun completeOnboarding() {
        // Mark onboarding as completed
        val sharedPref = getSharedPreferences("Onboarding", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("Shown", true)
            apply()
        }

        // Navigate to login screen
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
private fun OnboardingApp(onComplete: () -> Unit, onSkip: () -> Unit) {
    val themeState = rememberThemeState()

    RentalWheelsTheme(themeState = themeState) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            OnboardingScreen(onGetStarted = onComplete, onSkip = onSkip)
        }
    }
}
