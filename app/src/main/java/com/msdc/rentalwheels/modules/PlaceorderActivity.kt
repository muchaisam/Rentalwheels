package com.msdc.rentalwheels.modules

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.models.DBHelper
import com.msdc.rentalwheels.models.DBModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import timber.log.Timber
import java.util.*

class PlaceorderActivity : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var docDetails: EditText
    private lateinit var topQty: TextView
    private lateinit var lowerQty: TextView
    private lateinit var timeView: TextView
    private lateinit var topPrice: TextView
    private lateinit var lowerPrice: TextView
    private lateinit var radioService: RadioGroup
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var checkout: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var dbHelper: DBHelper
    private lateinit var dateButton: Button

    private var hours = 0
    private var days = 0
    private var audi = 0
    private var landCruiser = 0
    private var bmw = 0
    private var cartTotal = 0
    private var time: String? = null
    private var dbModelList: List<DBModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeorder)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        dbHelper = DBHelper(this)
        topQty = findViewById(R.id.topQty)
        lowerQty = findViewById(R.id.lowerQty)
        autoCompleteTextView = findViewById(R.id.autoComplete)
        radioService = findViewById(R.id.radioService)
        dateButton = findViewById(R.id.datebutton)
        timeView = findViewById(R.id.timeview)
        checkout = findViewById(R.id.checkout)
        topPrice = findViewById(R.id.topPrice)
        lowerPrice = findViewById(R.id.lowerPrice)
        docDetails = findViewById(R.id.docdetails)

        val options = arrayOf("Passport", "National ID", "VISA")
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, options)
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false)
        autoCompleteTextView.setAdapter(arrayAdapter)

        checkout.setOnClickListener {
            val hours = topPrice.text.toString().toInt()
            val days = lowerPrice.text.toString().toInt()
            val docDetails = docDetails.text.toString().trim()
            val cal = Calendar.getInstance()
            val day = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.YEAR)}"

            if (docDetails.isEmpty() || docDetails.length < 6) {
                Toast.makeText(this, "Oops not yet, Kindly recheck if your details", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            time = if (hours == 0) {
                "12:${cal.get(Calendar.MINUTE)}"
            } else {
                "$hours:${cal.get(Calendar.MINUTE)}"
            }

            val currentTime = time ?: ""
            Timber.e("onClick: $hours\n$days\n$day\n$currentTime")
            cartTotal = hours + days

            if (hours == 0 && days == 0) {
                Toast.makeText(this, "cart can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                dbHelper.insert(hours, days, audi, bmw, landCruiser, cartTotal, day, currentTime)
                dbHelper.insertTemp(hours, days, audi, bmw, landCruiser, cartTotal, day, currentTime)
                val sp = getSharedPreferences("RentalWheels", Context.MODE_PRIVATE)
                dbHelper.insertUserOrder(sp.getString("email", "") ?: "", hours, days, audi, bmw, landCruiser, cartTotal, day, currentTime)
            }

            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        timeView.setOnClickListener { showTimePicker() }

        initDatePicker()
        dateButton.text = getTodayDate()
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Reminder Time")
            .build()

        picker.show(supportFragmentManager, "RentalWheels")

        picker.addOnPositiveButtonClickListener {
            timeView.text = if (picker.hour > 12) {
                String.format("%02d", picker.hour - 12) + ":" + String.format("%02d", picker.minute) + "PM"
            } else {
                picker.hour.toString() + ":" + picker.minute + "AM"
            }

            calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, picker.hour)
                set(Calendar.MINUTE, picker.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
    }

    private fun getTodayDate(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val date = makeDateString(day, month + 1, year)
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "${getMonthFormat(month)} $day $year"
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN"
        }
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()
    }

    fun clickRadioButton(view: View) {
        if (view is RadioButton) {
            when (view.id) {
                R.id.Audi -> if (view.isChecked) {
                    displayHourPrice(hours * 100)
                    displayDailyCharge(days * 2000)
                    audi = 1
                    bmw = 0
                    landCruiser = 0
                }
                R.id.BMW -> if (view.isChecked) {
                    displayHourPrice(hours * 150)
                    displayDailyCharge(days * 2500)
                    audi = 0
                    bmw = 1
                    landCruiser = 0
                }
                R.id.LandCruiser -> if (view.isChecked) {
                    displayHourPrice(hours * 200)
                    displayDailyCharge(days * 3000)
                    audi = 0
                    bmw = 0
                    landCruiser = 1
                }
            }
        }
    }

    fun topInc(view: View) {
        if (hours == 11) {
            Toast.makeText(this, "Can't be more than 11", Toast.LENGTH_SHORT).show()
        } else {
            hours++
            displayHour(hours)
        }
        updatePrice("Hours", hours)
    }

    fun topDec(view: View) {
        if (hours == 0) {
            Toast.makeText(this, "Can't be less than 0", Toast.LENGTH_SHORT).show()
        } else {
            hours--
            displayHour(hours)
        }
        updatePrice("Hours", hours)
    }

    fun lowerInc(view: View) {
        if (days == 10) {
            Toast.makeText(this, "Can't be more than 10", Toast.LENGTH_SHORT).show()
        } else {
            days++
            displayDaily(days)
        }
        updatePrice("Days", days)
    }

    fun lowerDec(view: View) {
        if (days == 0) {
            Toast.makeText(this, "Can't be less than 0", Toast.LENGTH_SHORT).show()
        } else {
            days--
            displayDaily(days)
        }
        updatePrice("Days", days)
    }

    private fun displayHour(number: Int) {
        topQty.text = number.toString()
    }

    private fun displayDaily(number: Int) {
        lowerQty.text = number.toString()
    }

    private fun updatePrice(orderType: String, count: Int) {
        val selectedId = radioService.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedId)
        when (radioButton.text) {
            "Audi" -> {
                if (orderType == "Hours") {
                    displayHourPrice(count * 5)
                } else if (orderType == "Days") {
                    displayDailyCharge(count * 10)
                }
            }
            "BMW" -> {
                if (orderType == "Hours") {
                    displayHourPrice(count * 3)
                } else if (orderType == "Days") {
                    displayDailyCharge(count * 3)
                }
            }
            "LandCruiser" -> {
                if (orderType == "Hours") {
                    displayHourPrice(count * 8)
                } else if (orderType == "Days") {
                    displayDailyCharge(count * 13)
                }
            }
        }
    }

    private fun displayHourPrice(number: Int) {
        topPrice.text = number.toString()
    }

    private fun displayDailyCharge(number: Int) {
        lowerPrice.text = number.toString()
    }
}