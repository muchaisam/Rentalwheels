package com.example.rentalwheels.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (column_id INTEGER PRIMARY KEY, Hours INTEGER, Days INTEGER, Audi INTEGER, BMW INTEGER, LandCruiser INTEGER, final_price INTEGER, date TEXT, time TEXT, email_id TEXT)")
        db.execSQL("CREATE TABLE $TEMP_TABLE_NAME (column_id INTEGER PRIMARY KEY, Hours INTEGER, Days INTEGER, Audi INTEGER, BMW INTEGER, LandCruiser INTEGER, final_price INTEGER, date TEXT, time TEXT, email_id TEXT)")
        db.execSQL("CREATE TABLE $USER_ORDER_PLACED (column_id INTEGER PRIMARY KEY, Hours INTEGER, Days INTEGER, Audi INTEGER, BMW INTEGER, LandCruiser INTEGER, final_price INTEGER, date TEXT, time TEXT, email_id TEXT)")
        db.execSQL("CREATE TABLE $SIGNUP_TABLE_NAME (user_id INTEGER PRIMARY KEY, first_name TEXT, last_name TEXT, email_id TEXT, phone_number INTEGER, password TEXT)")
        db.execSQL("CREATE TABLE $ADDRESS_TABLE_NAME (user_id INTEGER PRIMARY KEY, full_name TEXT, email_id TEXT, phone_number INTEGER, address TEXT)")
        db.execSQL("CREATE TABLE $USER_ORDER (id INTEGER PRIMARY KEY, order_email TEXT, phone_number TEXT, address TEXT, column_id INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade as needed
    }

    fun getDataFromDbForHistory(): List<DBModel> {
        val modelList = mutableListOf<DBModel>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val model = DBModel().apply {
                    id = cursor.getInt(0)
                    hours = cursor.getInt(1)
                    days = cursor.getInt(2)
                    audi = cursor.getInt(3)
                    bmw = cursor.getInt(4)
                    landCruiser = cursor.getInt(5)
                    finalPrice = cursor.getInt(6)
                    date = cursor.getString(7)
                    time = cursor.getString(8)
                }
                modelList.add(model)
            } while (cursor.moveToNext())
        }

        Log.d("Cart data", modelList.toString())
        return modelList
    }

    fun getDataTemp(): List<DBModel> {
        val modelList = mutableListOf<DBModel>()
        val query = "SELECT * FROM $TEMP_TABLE_NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val model = DBModel().apply {
                    id = cursor.getInt(0)
                    hours = cursor.getInt(1)
                    days = cursor.getInt(2)
                    audi = cursor.getInt(3)
                    bmw = cursor.getInt(4)
                    landCruiser = cursor.getInt(5)
                    finalPrice = cursor.getInt(6)
                    date = cursor.getString(7)
                    time = cursor.getString(8)
                }
                modelList.add(model)
            } while (cursor.moveToNext())
        }

        Log.d("Cart data", modelList.toString())
        return modelList
    }

    fun getUserOrder(): List<UserOrders> {
        val modelList = mutableListOf<UserOrders>()
        val query = "SELECT * FROM $USER_ORDER_PLACED"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val model = UserOrders().apply {
                    hour = cursor.getInt(1)
                    days = cursor.getInt(2)
                    audi = cursor.getInt(3)
                    bmw = cursor.getInt(4)
                    landCruiser = cursor.getInt(5)
                    cartTotal = cursor.getInt(6)
                    date = cursor.getInt(7)
                    time = cursor.getString(8)
                }
                modelList.add(model)
            } while (cursor.moveToNext())
        }

        Log.d("Cart data", modelList.toString())
        return modelList
    }

    fun deleteEntry(item_id: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "column_id = ?", arrayOf(item_id))
    }

    fun deleteTemp(item_id: String) {
        val db = this.writableDatabase
        db.delete(TEMP_TABLE_NAME, "column_id = ?", arrayOf(item_id))
    }

    fun insert(Hours: Int, Days: Int, Audi: Int, BMW: Int, LandCruiser: Int, final_price: Int, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("Hours", Hours)
            put("Days", Days)
            put("Audi", Audi)
            put("BMW", BMW)
            put("LandCruiser", LandCruiser)
            put("final_price", final_price)
            put("date", date)
            put("time", time)
        }
        db.insert(TABLE_NAME, null, contentValues)
        return true
    }

    fun insertTemp(Hours: Int, Days: Int, Audi: Int, BMW: Int, LandCruiser: Int, final_price: Int, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("Hours", Hours)
            put("Days", Days)
            put("Audi", Audi)
            put("BMW", BMW)
            put("LandCruiser", LandCruiser)
            put("final_price", final_price)
            put("date", date)
            put("time", time)
        }
        db.insert(TEMP_TABLE_NAME, null, contentValues)
        return true
    }

    fun insertUserOrder(email_id: String, Hours: Int, Days: Int, Audi: Int, BMW: Int, LandCruiser: Int, final_price: Int, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("email_id", email_id)
            put("Hours", Hours)
            put("Days", Days)
            put("Audi", Audi)
            put("BMW", BMW)
            put("LandCruiser", LandCruiser)
            put("final_price", final_price)
            put("date", date)
            put("time", time)
        }
        db.insert(USER_ORDER_PLACED, null, contentValues)
        return true
    }

    companion object {
        const val DB_NAME = "laundry.db"
        const val TABLE_NAME = "laundryOrdersTable"
        const val TEMP_TABLE_NAME = "Temp_Orders_Table"
        const val SIGNUP_TABLE_NAME = "SIGNUP_TABLE_LAUNDRY"
        const val ADDRESS_TABLE_NAME = "ADDRESS_TABLE"
        const val USER_ORDER = "USER_ORDER_TABLE"
        const val USER_ORDER_PLACED = "USER_ORDER_TABLE_PLACED"
    }
}