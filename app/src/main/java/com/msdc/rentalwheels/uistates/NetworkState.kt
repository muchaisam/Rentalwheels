package com.msdc.rentalwheels.uistates

sealed class NetworkState {
    object Unknown : NetworkState()
    object Available : NetworkState()
    object Lost : NetworkState()
}