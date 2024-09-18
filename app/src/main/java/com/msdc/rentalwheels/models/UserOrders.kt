package com.msdc.rentalwheels.models

data class UserOrders(
    var hour: Int = 0,
    var days: Int = 0,
    var audi: Int = 0,
    var bmw: Int = 0,
    var landCruiser: Int = 0,
    var cartTotal: Int = 0,
    var date: Int = 0,
    var time: String? = null,
    var email: String? = null,
    var id: Int = 0
)