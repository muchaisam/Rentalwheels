package com.example.rentalwheels.ux

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.rentalwheels.R
import com.example.rentalwheels.adapters.OnboardingAdapter
import com.example.rentalwheels.auth.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var prevButton: TextView
    private lateinit var nextButton: TextView
    private lateinit var skipButton: TextView
    private lateinit var adapter: OnboardingAdapter
    private lateinit var indicator: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Initialize views
        viewPager = findViewById(R.id.viewPager)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)
        skipButton = findViewById(R.id.skip_button)
        indicator = findViewById(R.id.onbindicator)

        // Initialize data for onboarding
        val images = intArrayOf(R.drawable.landcruiser, R.drawable.landcruiser, R.drawable.wheels)
        val captions = arrayOf(
            "Wide variety of cars to choose from",
            "Easy booking and flexible rental options",
            "24/7 customer support and roadside assistance"
        )

        // Set up the adapter
        adapter = OnboardingAdapter(this, images, captions)
        viewPager.adapter = adapter
        setOnboardingIndicator()
        setCurrentOnboardingIndicators(0)

        // Set up button click listeners
        prevButton.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        nextButton.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            } else {
                // Navigate to the main activity when the last page is reached
                goToLogin()
            }
        }

        skipButton.setOnClickListener {
            // Handle skip button click, usually navigate to the main activity
            goToLogin()
        }

        // Set up page change listener
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicators(position)
            }
        })

        // After the user finishes the onboarding process
        val sharedPref = getSharedPreferences("Onboarding", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean("Shown", true)
            apply()
        }
    }

    private fun setOnboardingIndicator() {
        val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, R.drawable.default_indicator
                ))
            indicators[i]?.layoutParams = layoutParams
            indicator.addView(indicators[i])
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentOnboardingIndicators(index: Int) {
        val childCount = indicator.childCount
        for (i in 0 until childCount) {
            val imageView = indicator.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.selected_dot))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.default_dot))
            }
        }
        if (index == adapter.itemCount - 1){
            nextButton.text = "Get Started"
        }else {
            nextButton.text = "Next"
        }
    }


    private fun goToLogin() {
        val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}