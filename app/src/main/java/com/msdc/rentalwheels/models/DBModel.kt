package com.msdc.rentalwheels.models

data class DBModel(
    var hours: Int = 0,
    var days: Int = 0,
    var audi: Int = 0,
    var bmw: Int = 0,
    var landCruiser: Int = 0,
    var finalPrice: Int = 0,
    var date: String? = null,
    var time: String? = null,
    var id: Int = 0,
)