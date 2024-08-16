package com.example.rentalwheels.auth

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rentalwheels.HomeActivity
import com.example.rentalwheels.R
import com.example.rentalwheels.databinding.ActivityRegisterBinding
import com.example.rentalwheels.ux.BlurredProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: BlurredProgressDialog

    private companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val USER_EMAIL = "user_email"
        const val USER_PHONE = "user_mobile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()
        setupListeners()
        checkCurrentUser()
    }

    private fun initializeComponents() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        progressDialog = BlurredProgressDialog(this, R.style.CustomProgressDialogTheme)
    }

    private fun setupListeners() {
        binding.signupuser.setOnClickListener { attemptRegistration() }
        binding.switchlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.pwdid.addTextChangedListener(passwordStrengthWatcher)
    }

    private fun checkCurrentUser() {
        auth.currentUser?.let {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            })
            finish()
        }
    }

    private fun attemptRegistration() {
        progressDialog.show()
        val email = binding.usermail.text.toString().trim()
        val password = binding.pwdid.text.toString().trim()
        val repeatPassword = binding.repeatpwd.text.toString().trim()
        val firstName = binding.fnameid.text.toString().trim()
        val lastName = binding.lnameid.text.toString().trim()
        val mobile = binding.phoneinput.text.toString().trim()

        if (validateInputs(email, password, repeatPassword, firstName, lastName, mobile)) {
            saveUserData(firstName, email, mobile)
            createUserWithEmailAndPassword(email, password, firstName, lastName, mobile)
        } else {
            progressDialog.dismiss()
        }
    }

    private fun validateInputs(email: String, password: String, repeatPassword: String,
                               firstName: String, lastName: String, mobile: String): Boolean {
        when {
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Please enter a valid email address")
                return false
            }
            password.isEmpty() || password.length < 8 -> {
                showToast("Password must be at least 8 characters long")
                return false
            }
            password != repeatPassword -> {
                showToast("Passwords do not match")
                return false
            }
            firstName.isEmpty() || lastName.isEmpty() -> {
                showToast("Please enter your full name")
                return false
            }
            mobile.isEmpty() || !android.util.Patterns.PHONE.matcher(mobile).matches() -> {
                showToast("Please enter a valid phone number")
                return false
            }
            else -> return true
        }
    }

    private fun saveUserData(firstName: String, email: String, mobile: String) {
        with(sharedPreferences.edit()) {
            putString(FIRST_NAME, firstName)
            putString(USER_EMAIL, email)
            putString(USER_PHONE, mobile)
            apply()
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String,
                                               firstName: String, lastName: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    handleSuccessfulRegistration(email, firstName, lastName, phone)
                } else {
                    handleRegistrationFailure(task.exception)
                }
            }
    }

    private fun handleSuccessfulRegistration(email: String, firstName: String, lastName: String, phone: String) {
        val user = auth.currentUser
        user?.let {
            updateUserProfile(it, lastName)
            saveUserToFirestore(it.uid, email, firstName, lastName, phone)
            sendVerificationEmail(it)
        }
    }

    private fun updateUserProfile(user: FirebaseUser, lastName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(lastName).build()
        user.updateProfile(profileUpdates)
    }

    private fun saveUserToFirestore(uid: String, email: String, firstName: String, lastName: String, phone: String) {
        val db = FirebaseFirestore.getInstance()
        val userDetails = hashMapOf(
            "email" to email,
            "firstName" to firstName,
            "lastName" to lastName,
            "phoneNumber" to phone
        )

        db.collection("users").document(uid)
            .set(userDetails)
            .addOnSuccessListener {
                Timber.d("User details added successfully")
            }
            .addOnFailureListener { e ->
                Timber.w(e, "Error adding user details")
            }
    }

    private fun sendVerificationEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { verificationTask ->
                progressDialog.dismiss()
                if (verificationTask.isSuccessful) {
                    showToast("Registration successful! Please check your email for verification.")
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    showToast("Failed to send verification email: ${verificationTask.exception?.message}")
                }
            }
    }

    private fun handleRegistrationFailure(exception: Exception?) {
        progressDialog.dismiss()
        when (exception) {
            is FirebaseAuthUserCollisionException -> showToast("The email address is already in use.")
            else -> showToast("Registration failed: ${exception?.message}")
        }
    }

    private val passwordStrengthWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            binding.passwordStrengthText.apply {
                if (s.isEmpty()) {
                    visibility = View.GONE
                } else {
                    visibility = View.VISIBLE
                    val strengthColor = getPasswordStrength(s.toString())
                    text = "Password strength: ${resources.getString(strengthColor)}"
                    setTextColor(ContextCompat.getColor(this@RegisterActivity, strengthColor))
                }
            }
        }
    }

    private fun getPasswordStrength(password: String): Int {
        var strengthPoints = 0
        if (password.length >= 8) strengthPoints++
        if (password.matches("(?=.*[a-z]).*".toRegex())) strengthPoints++
        if (password.matches("(?=.*[A-Z]).*".toRegex())) strengthPoints++
        if (password.matches("(?=.*\\d).*".toRegex())) strengthPoints++
        if (password.matches("(?=.*[@$!%*?&#]).*".toRegex())) strengthPoints++

        return when (strengthPoints) {
            0, 1 -> R.string.very_weak
            2 -> R.string.weak
            3 -> R.string.moderate
            4 -> R.string.strong
            5 -> R.string.very_strong
            else -> R.string.unknown_strength
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}