package com.msdc.rentalwheels.models

interface OnItemClick {
    fun onClick(dbList: List<DBModel>, i: Int)
}