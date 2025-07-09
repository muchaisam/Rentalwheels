package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.ThemePreference
import com.msdc.rentalwheels.ui.theme.ThemePreferencesManager
import com.msdc.rentalwheels.ui.theme.Typography
import com.msdc.rentalwheels.ui.theme.rememberThemeState
import com.msdc.rentalwheels.uistates.Result
import com.msdc.rentalwheels.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val userDataState by viewModel.userData.collectAsState()
    val settingsState by viewModel.settingsState.collectAsState()
    val context = LocalContext.current
    val themeState = rememberThemeState()
    val themePreferencesManager = remember { ThemePreferencesManager(context) }
    var currentThemePreference by remember {
        mutableStateOf(themePreferencesManager.getThemePreference())
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        when (val result = userDataState) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            is Result.Error -> {
                Card(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .wrapContentSize(Alignment.Center),
                    colors =
                    CardDefaults.cardColors(
                        containerColor =
                        MaterialTheme.colorScheme
                            .errorContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Error: ${result.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            is Result.Success -> {
                val userData = result.data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile Header
                    item {
                        ProfileHeader(
                            userData = userData,
                            onEditClick = onEditProfileClick
                        )
                    }

                    // Theme Selection
                    item {
                        ThemeSelectionCard(
                            currentTheme = currentThemePreference,
                            onThemeChanged = { newTheme ->
                                currentThemePreference = newTheme
                                themePreferencesManager
                                    .setThemePreference(
                                        newTheme
                                    )

                                // Update the SettingsViewModel with
                                // dark mode state
                                val isDarkMode =
                                    when (newTheme) {
                                        ThemePreference
                                            .LIGHT ->
                                            false

                                        ThemePreference
                                            .DARK ->
                                            true

                                        ThemePreference
                                            .SYSTEM_DEFAULT -> {
                                            // For
                                            // system
                                            // default,
                                            // check
                                            // current
                                            // system
                                            // theme
                                            false // Let
                                            // system
                                            // decide,
                                            // we'll
                                            // update
                                            // this when
                                            // the
                                            // system
                                            // changes
                                        }
                                    }
                                viewModel.updateDarkMode(isDarkMode)

                                // Apply theme change to ThemeState
                                when (newTheme) {
                                    ThemePreference.LIGHT ->
                                        themeState.setTheme(
                                            com.msdc
                                                .rentalwheels
                                                .ui
                                                .theme
                                                .ThemeMode
                                                .Light
                                        )

                                    ThemePreference.DARK ->
                                        themeState.setTheme(
                                            com.msdc
                                                .rentalwheels
                                                .ui
                                                .theme
                                                .ThemeMode
                                                .Dark
                                        )

                                    ThemePreference
                                        .SYSTEM_DEFAULT ->
                                        themeState.setTheme(
                                            com.msdc
                                                .rentalwheels
                                                .ui
                                                .theme
                                                .ThemeMode
                                                .System
                                        )
                                }
                            }
                        )
                    }

                    // General Settings
                    item {
                        SettingsSection(
                            title = "General",
                            icon = Icons.Default.Settings
                        ) {
                            SettingsItem(
                                icon = Icons.Default.Notifications,
                                title = "Notifications",
                                subtitle =
                                if (settingsState
                                        .notificationsEnabled
                                )
                                    "Enabled"
                                else "Disabled",
                                onClick = {
                                    viewModel
                                        .updateNotifications(
                                            !settingsState
                                                .notificationsEnabled
                                        )
                                }
                            )

                            SettingsItem(
                                icon = Icons.Default.Language,
                                title = "Language",
                                subtitle =
                                settingsState
                                    .selectedLanguage,
                                onClick = { /* Handle language change */
                                }
                            )
                        }
                    }

                    // Privacy & Security
                    item {
                        SettingsSection(
                            title = "Privacy & Security",
                            icon = Icons.Default.Security
                        ) {
                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Biometric Authentication",
                                subtitle =
                                if (settingsState
                                        .biometricsEnabled
                                )
                                    "Enabled"
                                else "Disabled",
                                onClick = {
                                    viewModel.updateBiometrics(
                                        !settingsState
                                            .biometricsEnabled
                                    )
                                }
                            )

                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Change Password",
                                subtitle =
                                "Update your account password",
                                onClick = { /* Handle password change */
                                }
                            )

                            SettingsItem(
                                icon = Icons.Default.Policy,
                                title = "Privacy Policy",
                                subtitle =
                                "Read our privacy policy",
                                onClick = { /* Handle privacy policy */
                                }
                            )
                        }
                    }

                    // Support & About
                    item {
                        SettingsSection(
                            title = "Support & About",
                            icon = Icons.Default.Help
                        ) {
                            SettingsItem(
                                icon = Icons.Default.Email,
                                title = "Contact Support",
                                subtitle =
                                "Get help with your account",
                                onClick = { /* Handle support */ }
                            )

                            SettingsItem(
                                icon = Icons.Default.Info,
                                title = "About",
                                subtitle = "Version 1.0.0",
                                onClick = { /* Handle about */ }
                            )
                        }
                    }

                    // Logout Button
                    item {
                        Card(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            colors =
                            CardDefaults.cardColors(
                                containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .surface
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation =
                            CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {
                            Button(
                                onClick = onLogoutClick,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .error,
                                    contentColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onError
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(
                                    imageVector =
                                    Icons.Default
                                        .ExitToApp,
                                    contentDescription =
                                    "Logout",
                                    modifier =
                                    Modifier.size(20.dp)
                                )
                                Spacer(
                                    modifier =
                                    Modifier.width(8.dp)
                                )
                                Text(
                                    text = "Logout",
                                    style =
                                    Typography
                                        .bodyLarge,
                                    fontWeight =
                                    FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Bottom spacing
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    userData: com.msdc.rentalwheels.models.UserData,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Card(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.launc),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${userData.firstName} ${userData.lastName}",
                    style = Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userData.email,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = userData.phoneNumber,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Edit Button
            IconButton(
                onClick = onEditClick,
                modifier =
                Modifier
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionCard(
    currentTheme: ThemePreference,
    onThemeChanged: (ThemePreference) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brightness6,
                    contentDescription = "Theme",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Theme",
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ThemePreference.values()) { theme ->
                    ThemeOptionCard(
                        theme = theme,
                        isSelected = currentTheme == theme,
                        onSelected = { onThemeChanged(theme) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(theme: ThemePreference, isSelected: Boolean, onSelected: () -> Unit) {
    val (icon, title, subtitle) =
        when (theme) {
            ThemePreference.LIGHT ->
                Triple(Icons.Default.Brightness4, "Light", "Always light")

            ThemePreference.DARK ->
                Triple(Icons.Default.Brightness4, "Dark", "Always dark")

            ThemePreference.SYSTEM_DEFAULT ->
                Triple(Icons.Default.BrightnessAuto, "System", "Follow system")
        }

    val backgroundColor by
    animateColorAsState(
        targetValue =
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "backgroundColor"
    )

    val contentColor by
    animateColorAsState(
        targetValue =
        if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "contentColor"
    )

    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onSelected() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = contentColor,
                        modifier =
                        Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(backgroundColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = Typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = subtitle,
                style = Typography.labelSmall,
                color = contentColor.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun SettingsSection(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (trailing != null) {
            trailing()
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
