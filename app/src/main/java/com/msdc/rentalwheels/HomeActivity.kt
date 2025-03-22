package com.msdc.rentalwheels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.msdc.rentalwheels.appnavigation.RentalWheelsApp
import com.msdc.rentalwheels.ui.theme.rememberThemeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = rememberThemeState()

            RentalWheelsApp(
                navController = rememberNavController(),
                themeState = themeState
            )
        }
    }
}
