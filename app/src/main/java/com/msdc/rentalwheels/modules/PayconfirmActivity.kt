package com.msdc.rentalwheels.modules

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.msdc.rentalwheels.HomeActivity
import com.msdc.rentalwheels.R

class PayconfirmActivity : AppCompatActivity() {

    private lateinit var okayBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payconfirm)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        okayBtn = findViewById(R.id.okay_button)
        okayBtn.setOnClickListener {
            val intent = Intent(this@PayconfirmActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}