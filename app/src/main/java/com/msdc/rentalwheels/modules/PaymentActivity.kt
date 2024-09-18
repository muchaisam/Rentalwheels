package com.msdc.rentalwheels.modules

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.msdc.rentalwheels.R

class PaymentActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mProgressDialog: ProgressDialog


    lateinit var mAmount: EditText

    lateinit var mPhone: EditText

    lateinit var mPay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        mAmount = findViewById(R.id.etAmount)
        mPhone = findViewById(R.id.etPhone)
        mPay = findViewById(R.id.btnPay)


        mProgressDialog = ProgressDialog(this)

        mPay.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view == mPay) {
            val phoneNumber = mPhone.text.toString()
            val amount = mAmount.text.toString()
            // Add your payment initiation logic here
        }
    }
}