package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.models.UserData
import com.msdc.rentalwheels.uistates.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _userData = MutableStateFlow<Result<UserData>>(Result.Loading)
    val userData = _userData.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                auth.currentUser?.let { user ->
                    firestore.collection("users")
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val userData = UserData(
                                    email = document.getString("email") ?: "",
                                    firstName = document.getString("firstName") ?: "",
                                    lastName = document.getString("lastName") ?: "",
                                    phoneNumber = document.getString("phoneNumber") ?: ""
                                )
                                _userData.value = Result.Success(userData)
                            } else {
                                _userData.value = Result.Error("User not found")
                            }
                        }
                        .addOnFailureListener { e ->
                            _userData.value = Result.Error(e.message ?: "Unknown error")
                        }
                }
            } catch (e: Exception) {
                _userData.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }
}