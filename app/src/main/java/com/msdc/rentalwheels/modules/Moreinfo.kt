package com.msdc.rentalwheels.modules

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.msdc.rentalwheels.HomeActivity
import com.msdc.rentalwheels.R

class Moreinfo : AppCompatActivity() {

    private lateinit var backbtn: ImageView
    private lateinit var proceedbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        setContentView(R.layout.activity_moreinfo)

        backbtn = findViewById(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this@Moreinfo, HomeActivity::class.java)
            startActivity(intent)
        }

        proceedbtn = findViewById(R.id.proceedbtn)
        proceedbtn.setOnClickListener {
            val intent = Intent(this@Moreinfo, PlaceorderActivity::class.java)
            startActivity(intent)
        }

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}