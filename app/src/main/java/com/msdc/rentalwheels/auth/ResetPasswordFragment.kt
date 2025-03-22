package com.msdc.rentalwheels.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ux.BlurredProgressDialog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ResetPasswordFragment : BottomSheetDialogFragment() {

    private lateinit var inputEmail: TextInputEditText
    private lateinit var btnReset: MaterialButton
    private lateinit var progressBar: BlurredProgressDialog

    companion object {
        const val TAG = "example_dialog"

        fun display(fragmentManager: FragmentManager): ResetPasswordFragment {
            val dialog = ResetPasswordFragment()
            dialog.show(fragmentManager, TAG)
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetDialog)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        inputEmail = view.findViewById(R.id.resetemail)
        btnReset = view.findViewById(R.id.resetpinbtn)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        progressBar = BlurredProgressDialog(requireActivity(), R.style.CustomProgressDialogTheme)

        btnReset.setOnClickListener {
            val email = inputEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(context, "Please enter your registered email id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.show()

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    Firebase.auth.sendPasswordResetEmail(email).await()
                    Toast.makeText(context, "Check your email/spam folder for your password reset link!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to send reset email link. Please try again!", Toast.LENGTH_SHORT).show()
                }

                progressBar.dismiss()
            }
        }

        return view
    }
}