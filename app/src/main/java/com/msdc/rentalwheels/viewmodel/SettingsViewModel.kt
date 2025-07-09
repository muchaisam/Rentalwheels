package com.msdc.rentalwheels.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.msdc.rentalwheels.models.UserData
import com.msdc.rentalwheels.ui.theme.ThemePreference
import com.msdc.rentalwheels.ui.theme.ThemePreferencesManager
import com.msdc.rentalwheels.uistates.Result
import com.msdc.rentalwheels.uistates.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _userData = MutableStateFlow<Result<UserData>>(Result.Loading)
    val userData = _userData.asStateFlow()

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    private val themePreferencesManager = ThemePreferencesManager(context)

    init {
        loadUserData()
        loadSettings()
    }

    private fun loadSettings() {
        // Load theme preference from storage
        val savedTheme = themePreferencesManager.getThemePreference()
        val isDarkMode =
            when (savedTheme) {
                ThemePreference.DARK -> true
                ThemePreference.LIGHT -> false
                ThemePreference.SYSTEM_DEFAULT -> {
                    // You can implement system theme detection here
                    // For now, default to light theme
                    false
                }
            }

        _settingsState.value = SettingsState(isDarkMode = isDarkMode, themePreference = savedTheme)
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        val newTheme = if (isDarkMode) ThemePreference.DARK else ThemePreference.LIGHT
        themePreferencesManager.setThemePreference(newTheme)
        _settingsState.value =
            _settingsState.value.copy(isDarkMode = isDarkMode, themePreference = newTheme)
    }

    fun updateThemePreference(preference: ThemePreference) {
        themePreferencesManager.setThemePreference(preference)
        val isDarkMode =
            when (preference) {
                ThemePreference.DARK -> true
                ThemePreference.LIGHT -> false
                ThemePreference.SYSTEM_DEFAULT -> {
                    // You can implement system theme detection here
                    false
                }
            }
        _settingsState.value =
            _settingsState.value.copy(isDarkMode = isDarkMode, themePreference = preference)
    }

    fun updateNotifications(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(notificationsEnabled = enabled)
    }

    fun updateBiometrics(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(biometricsEnabled = enabled)
    }

    fun updateLanguage(language: String) {
        _settingsState.value = _settingsState.value.copy(selectedLanguage = language)
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                auth.currentUser?.let { user ->
                    firestore
                        .collection("users")
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val userData =
                                    UserData(
                                        email = document.getString("email") ?: "",
                                        firstName = document.getString("firstName")
                                            ?: "",
                                        lastName = document.getString("lastName") ?: "",
                                        phoneNumber = document.getString("phoneNumber")
                                            ?: ""
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
