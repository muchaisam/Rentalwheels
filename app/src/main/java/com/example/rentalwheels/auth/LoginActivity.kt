package com.example.rentalwheels.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rentalwheels.HomeActivity
import com.example.rentalwheels.R
import com.example.rentalwheels.databinding.ActivityLoginBinding
import com.example.rentalwheels.ux.BlurredProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: BlurredProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupListeners()
    }

    private fun initializeComponents() {
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = BlurredProgressDialog(this, R.style.CustomProgressDialogTheme)
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener { attemptLogin() }
        binding.register.setOnClickListener { navigateToRegister() }
        binding.forgotpassword.setOnClickListener { showResetPasswordDialog() }
    }

    private fun attemptLogin() {
        val email = binding.emailinput.text.toString().trim()
        val password = binding.pwdid.text.toString().trim()

        if (validateInput(email, password)) {
            progressDialog.show()
            signInWithEmailAndPassword(email, password)
        } else {
            showToast("Invalid email or password. Please try again.")
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.emailinput.error = "Email cannot be empty"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailinput.error = "Invalid email format"
                false
            }
            password.isEmpty() -> {
                binding.pwdid.error = "Password cannot be empty"
                false
            }
            password.length < 6 -> {
                binding.pwdid.error = "Password must be at least 6 characters long"
                false
            }
            else -> true
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    handleSuccessfulLogin()
                } else {
                    handleLoginFailure(task.exception)
                }
            }
    }

    private fun handleSuccessfulLogin() {
        val user = firebaseAuth.currentUser
        user?.let {
            if (it.isEmailVerified) {
                navigateToHome()
            } else {
                showToast("Please verify your email address.")
                sendVerificationEmail(it)
            }
        }
    }

    private fun handleLoginFailure(exception: Exception?) {
        when (exception) {
            is FirebaseAuthInvalidUserException -> showToast("No account found with this email.")
            is FirebaseAuthInvalidCredentialsException -> showToast("Invalid email or password.")
            else -> showToast("Authentication failed: ${exception?.message}")
        }
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Verification email sent. Please check your inbox.")
                } else {
                    showToast("Failed to send verification email: ${task.exception?.message}")
                }
            }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showResetPasswordDialog() {
        val resetDialog = ResetPasswordFragment()
        resetDialog.show(supportFragmentManager, resetDialog.tag)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}