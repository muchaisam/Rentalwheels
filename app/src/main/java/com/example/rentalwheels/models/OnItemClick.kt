package com.example.rentalwheels.models

interface OnItemClick {
    fun onClick(dbList: List<DBModel>, i: Int)
}