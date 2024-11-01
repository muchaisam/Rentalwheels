package com.msdc.rentalwheels.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import com.msdc.rentalwheels.uistates.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Unknown)
    val networkState: StateFlow<NetworkState> = _networkState.asStateFlow()

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    init {
        startMonitoring()
    }

    private fun startMonitoring() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _networkState.value = NetworkState.Available
                }

                override fun onLost(network: Network) {
                    _networkState.value = NetworkState.Lost
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val unmetered = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_NOT_METERED
                    )
                    _networkState.value = NetworkState.Available
                }
            }

            connectivityManager.registerDefaultNetworkCallback(networkCallback!!)
        } else {
            @Suppress("DEPRECATION")
            val activeNetwork = connectivityManager.activeNetworkInfo
            _networkState.value = if (activeNetwork?.isConnected == true) {
                NetworkState.Available
            } else {
                NetworkState.Lost
            }
        }
    }

    fun cleanup() {
        networkCallback?.let {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.unregisterNetworkCallback(it)
        }
    }
}