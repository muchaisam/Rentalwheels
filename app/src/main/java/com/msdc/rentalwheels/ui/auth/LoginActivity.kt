package com.msdc.rentalwheels.ui.auth

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.HomeActivity
import com.msdc.rentalwheels.ui.theme.RentalWheelsTheme
import com.msdc.rentalwheels.ui.theme.rememberThemeState

class LoginActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        enableEdgeToEdge()

        setContent {
            val themeState = rememberThemeState()
            var isLoading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            RentalWheelsTheme(themeState = themeState) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onLoginClick = { email, password ->
                            isLoading = true
                            errorMessage = null
                            attemptLogin(email, password) { success, error ->
                                isLoading = false
                                if (!success) {
                                    errorMessage = error
                                }
                            }
                        },
                        onRegisterClick = {
                            val intent =
                                Intent(this@LoginActivity, RegisterActivity::class.java)
                            startActivity(intent)
                        }, onForgotPasswordClick = {
                            val intent =
                                Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun attemptLogin(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (!validateInput(email, password)) {
            onResult(false, "Invalid email or password format")
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task
            ->
            if (task.isSuccessful) {
                handleSuccessfulLogin(onResult)
            } else {
                val errorMsg =
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException ->
                            "No account found with this email"

                        is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                        else -> "Login failed. Please try again."
                    }
                onResult(false, errorMsg)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> false
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> false
            password.isEmpty() -> false
            password.length < 6 -> false
            else -> true
        }
    }

    private fun handleSuccessfulLogin(onResult: (Boolean, String?) -> Unit) {
        val user = firebaseAuth.currentUser
        user?.let {
            if (it.isEmailVerified) {
                fetchUserDataFromFirestore(it.uid) { success ->
                    if (success) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        onResult(true, null)
                    } else {
                        onResult(false, "Failed to load user data")
                    }
                }
            } else {
                sendVerificationEmail(it)
                onResult(false, "Please verify your email address. Verification email sent.")
            }
        }
            ?: onResult(false, "Authentication failed")
    }

    private fun fetchUserDataFromFirestore(uid: String, onComplete: (Boolean) -> Unit) {
        firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Store user data in SharedPreferences if needed
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
            .addOnFailureListener { onComplete(false) }
    }

    private fun sendVerificationEmail(user: com.google.firebase.auth.FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Verification email sent to ${user.email}")
            } else {
                showToast("Failed to send verification email")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
