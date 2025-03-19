package com.msdc.rentalwheels.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.models.UserData
import com.msdc.rentalwheels.ui.theme.Typography

@Composable
fun SettingsContent(
    userData: UserData,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var biometricsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${userData.firstName} ${userData.lastName}",
            style = Typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = userData.email,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = userData.phoneNumber,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Preferences Section
        SettingsSection(title = "Preferences") {
            SettingsSwitch(
                text = "Dark Mode",
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it }
            )

            LanguageSelector(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it }
            )
        }

        // Notifications Section
        SettingsSection(title = "Notifications") {
            SettingsSwitch(
                text = "Push Notifications",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            SettingsSwitch(
                text = "Email Notifications",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            SettingsSwitch(
                text = "Promotional Updates",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        // Privacy & Security
        SettingsSection(title = "Privacy & Security") {
            SettingsSwitch(
                text = "Biometric Authentication",
                checked = biometricsEnabled,
                onCheckedChange = { biometricsEnabled = it }
            )
            SettingsItem(
                text = "Change Password",
                onClick = { /* Handle password change */ }
            )
            SettingsItem(
                text = "Privacy Policy",
                onClick = { /* Open privacy policy */ }
            )
        }

        // App Information
        SettingsSection(title = "App Information") {
            SettingsItem(
                text = "Version 1.0.0",
                subtitle = "Check for updates",
                onClick = { /* Handle update check */ }
            )
            SettingsItem(
                text = "Terms of Service",
                onClick = { /* Open terms */ }
            )
            SettingsItem(
                text = "Open Source Licenses",
                onClick = { /* Open licenses */ }
            )
        }

        // Account Management
        SettingsSection(title = "Account") {
            SettingsItem(
                text = "Delete Account",
                textColor = MaterialTheme.colorScheme.error,
                onClick = { /* Handle account deletion */ }
            )
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Logout")
            }
        }
    }
}