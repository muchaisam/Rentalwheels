package com.msdc.rentalwheels.ux

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.msdc.rentalwheels.R

class BlurredProgressDialog(context: Context, theme: Int) : Dialog(context, theme) {

    // Constant for the dim amount
    companion object {
        private const val DIM_AMOUNT = 0.5f
    }

    // Called when the dialog is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_dialog)

        // Apply the custom settings to the window
        window?.let { window ->
            setBackgroundAndBlur(window)
            setStatusBarColor(window)
        }
    }

    // Set the background of the window to be transparent and apply a blur effect
    private fun setBackgroundAndBlur(window: Window) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = DIM_AMOUNT
        }
        window.attributes = layoutParams
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
    }

    // Set the color of the status bar for the dialog
    private fun setStatusBarColor(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(context, R.color.secondary)
    }
}