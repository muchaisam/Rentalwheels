package com.msdc.rentalwheels.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.msdc.rentalwheels.ui.theme.RentalWheelsTheme
import com.msdc.rentalwheels.ui.theme.rememberThemeState

class ForgotPasswordActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()

        enableEdgeToEdge()

        setContent {
            val themeState = rememberThemeState()
            var isLoading by remember { mutableStateOf(false) }
            var isEmailSent by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            RentalWheelsTheme(themeState = themeState) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ForgotPasswordScreen(
                        isLoading = isLoading,
                        isEmailSent = isEmailSent,
                        errorMessage = errorMessage,
                        onSendResetEmail = { email ->
                            isLoading = true
                            errorMessage = null
                            sendPasswordResetEmail(email) { success, error ->
                                isLoading = false
                                if (success) {
                                    isEmailSent = true
                                    showToast("Password reset email sent successfully!")
                                } else {
                                    errorMessage = error
                                }
                            }
                        },
                        onBackClick = { finish() },
                        onRetryClick = {
                            isEmailSent = false
                            errorMessage = null
                        }
                    )
                }
            }
        }
    }

    private fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        if (!validateEmail(email)) {
            onResult(false, "Please enter a valid email address")
            return
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                val errorMsg =
                    when (task.exception?.message) {
                        "There is no user record corresponding to this identifier. The user may have been deleted." ->
                            "No account found with this email address"

                        "The email address is badly formatted." ->
                            "Please enter a valid email address"

                        else -> "Failed to send reset email. Please try again."
                    }
                onResult(false, errorMsg)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
