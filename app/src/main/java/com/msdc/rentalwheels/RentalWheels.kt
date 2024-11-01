package com.msdc.rentalwheels

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.ui.utils.NetworkStateMonitor
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RentalWheels : Application() {
    @Inject
    lateinit var networkStateMonitor: NetworkStateMonitor

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        setupFirestore()
    }

    private fun setupFirestore() {
        FirebaseFirestore.setLoggingEnabled(BuildConfig.DEBUG)
    }

    override fun onTerminate() {
        super.onTerminate()
        networkStateMonitor.cleanup()
    }
}