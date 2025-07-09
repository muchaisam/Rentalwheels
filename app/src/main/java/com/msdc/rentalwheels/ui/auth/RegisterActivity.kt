package com.msdc.rentalwheels.ui.auth

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.HomeActivity
import com.msdc.rentalwheels.ui.theme.RentalWheelsTheme
import com.msdc.rentalwheels.ui.theme.rememberThemeState

class RegisterActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val USER_EMAIL = "user_email"
        const val USER_PHONE = "user_mobile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Check if user is already logged in
        if (firebaseAuth.currentUser != null) {
            navigateToHome()
            return
        }

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
                    RegisterScreen(
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onRegisterClick = { formData ->
                            isLoading = true
                            errorMessage = null
                            attemptRegistration(formData) { success, error ->
                                isLoading = false
                                if (!success) {
                                    errorMessage = error
                                }
                            }
                        },
                        onLoginClick = {
                            val intent =
                                Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun attemptRegistration(
        formData: RegisterFormData,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (!validateFormData(formData)) {
            onResult(false, "Please fill in all fields correctly")
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(formData.email, formData.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        saveUserDataToFirestore(it.uid, formData) { firestoreSuccess ->
                            if (firestoreSuccess) {
                                saveUserDataToSharedPrefs(formData)
                                sendVerificationEmail(it) { emailSent ->
                                    if (emailSent) {
                                        showToast("Account created! Please verify your email.")
                                        navigateToHome()
                                        onResult(true, null)
                                    } else {
                                        onResult(
                                            false,
                                            "Account created but failed to send verification email"
                                        )
                                    }
                                }
                            } else {
                                onResult(false, "Failed to save user data")
                            }
                        }
                    }
                        ?: onResult(false, "Registration failed")
                } else {
                    val errorMsg =
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException ->
                                "An account with this email already exists"

                            is FirebaseAuthWeakPasswordException -> "Password is too weak"
                            else -> "Registration failed. Please try again."
                        }
                    onResult(false, errorMsg)
                }
            }
    }

    private fun validateFormData(formData: RegisterFormData): Boolean {
        return with(formData) {
            firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    phone.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                    phone.length >= 10 &&
                    password.length >= 6 &&
                    password == confirmPassword
        }
    }

    private fun saveUserDataToFirestore(
        uid: String,
        formData: RegisterFormData,
        onComplete: (Boolean) -> Unit
    ) {
        val userData =
            hashMapOf(
                "firstName" to formData.firstName,
                "lastName" to formData.lastName,
                "email" to formData.email,
                "phoneNumber" to formData.phone,
                "createdAt" to System.currentTimeMillis()
            )

        firestore
            .collection("users")
            .document(uid)
            .set(userData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    private fun saveUserDataToSharedPrefs(formData: RegisterFormData) {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString(FIRST_NAME, formData.firstName)
            putString(LAST_NAME, formData.lastName)
            putString(USER_EMAIL, formData.email)
            putString(USER_PHONE, formData.phone)
            apply()
        }
    }

    private fun sendVerificationEmail(
        user: com.google.firebase.auth.FirebaseUser,
        onComplete: (Boolean) -> Unit
    ) {
        user.sendEmailVerification().addOnCompleteListener { task -> onComplete(task.isSuccessful) }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
