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
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.components.PremiumButton
import com.msdc.rentalwheels.ui.components.PremiumButtonVariant
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.ui.theme.ThemePreference
import com.msdc.rentalwheels.ui.theme.ThemePreferencesManager
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
                    modifier = Modifier
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
                        .padding(RentalWheelsDesignSystem.Spacing.medium)
                        .wrapContentSize(Alignment.Center),
                    colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Card),
                    elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = RentalWheelsDesignSystem.Elevation.medium
                    )
                ) {
                    Text(
                        text = "Error: ${result.message}",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = RentalWheelsDesignSystem.Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.large)
                    )
                }
            }

            is Result.Success -> {
                val userData = result.data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(RentalWheelsDesignSystem.Spacing.large),
                    verticalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
                ) {
                    // Profile Header
                    item { ProfileHeader(userData = userData, onEditClick = onEditProfileClick) }

                    // Theme Selection
                    item {
                        ThemeSelectionCard(
                            currentTheme = currentThemePreference,
                            onThemeChanged = { newTheme ->
                                currentThemePreference = newTheme
                                themePreferencesManager.setThemePreference(newTheme)

                                // Update the SettingsViewModel with
                                // dark mode state
                                val isDarkMode =
                                    when (newTheme) {
                                        ThemePreference.LIGHT -> false
                                        ThemePreference.DARK -> true
                                        ThemePreference.SYSTEM_DEFAULT -> {
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
                                            com.msdc.rentalwheels.ui.theme.ThemeMode
                                                .Light
                                        )

                                    ThemePreference.DARK ->
                                        themeState.setTheme(
                                            com.msdc.rentalwheels.ui.theme.ThemeMode
                                                .Dark
                                        )

                                    ThemePreference.SYSTEM_DEFAULT ->
                                        themeState.setTheme(
                                            com.msdc.rentalwheels.ui.theme.ThemeMode
                                                .System
                                        )
                                }
                            }
                        )
                    }

                    // General Settings
                    item {
                        SettingsSection(title = "General", icon = Icons.Default.Settings) {
                            SettingsItem(
                                icon = Icons.Default.Notifications,
                                title = "Notifications",
                                subtitle =
                                if (settingsState.notificationsEnabled) "Enabled"
                                else "Disabled",
                                onClick = {
                                    viewModel.updateNotifications(
                                        !settingsState.notificationsEnabled
                                    )
                                }
                            )

                            SettingsItem(
                                icon = Icons.Default.Language,
                                title = "Language",
                                subtitle = settingsState.selectedLanguage,
                                onClick = { /* Handle language change */ }
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
                                if (settingsState.biometricsEnabled) "Enabled"
                                else "Disabled",
                                onClick = {
                                    viewModel.updateBiometrics(!settingsState.biometricsEnabled)
                                }
                            )

                            SettingsItem(
                                icon = Icons.Default.Lock,
                                title = "Change Password",
                                subtitle = "Update your account password",
                                onClick = { /* Handle password change */ }
                            )

                            SettingsItem(
                                icon = Icons.Default.Policy,
                                title = "Privacy Policy",
                                subtitle = "Read our privacy policy",
                                onClick = { /* Handle privacy policy */ }
                            )
                        }
                    }

                    // Support & About
                    item {
                        SettingsSection(
                            title = "Support & About",
                            icon = Icons.AutoMirrored.Filled.Help
                        ) {
                            SettingsItem(
                                icon = Icons.Default.Email,
                                title = "Contact Support",
                                subtitle = "Get help with your account",
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
                                .padding(
                                    top = RentalWheelsDesignSystem.Spacing.large
                                ),
                            colors =
                            CardDefaults.cardColors(
                                containerColor =
                                RentalWheelsDesignSystem.Colors.Surface02
                            ),
                            shape =
                            RoundedCornerShape(
                                RentalWheelsDesignSystem.CornerRadius.Card
                            ),
                            elevation =
                            CardDefaults.cardElevation(
                                defaultElevation =
                                RentalWheelsDesignSystem.Elevation.medium
                            )
                        ) {
                            PremiumButton(
                                onClick = onLogoutClick,
                                variant = PremiumButtonVariant.Primary,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        RentalWheelsDesignSystem.Spacing.medium
                                    )
                            ) { Text("Logout") }
                        }
                    }

                    // Bottom spacing
                    item {
                        Spacer(
                            modifier =
                            Modifier.height(
                                RentalWheelsDesignSystem.Spacing.extraLarge * 3
                            )
                        )
                    }
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
        CardDefaults.cardColors(
            containerColor = RentalWheelsDesignSystem.Colors.Surface02
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Card),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Card(
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f),
                shape = CircleShape,
                colors =
                CardDefaults.cardColors(
                    containerColor = RentalWheelsDesignSystem.Colors.BrandPrimary
                ),
                elevation =
                CardDefaults.cardElevation(
                    defaultElevation = RentalWheelsDesignSystem.Elevation.medium
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.mipmap.launc),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${userData.firstName} ${userData.lastName}",
                    style = RentalWheelsDesignSystem.Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

                Text(
                    text = userData.email,
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Text(
                    text = userData.phoneNumber,
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            // Edit Button
            IconButton(
                onClick = onEditClick,
                modifier =
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
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
        CardDefaults.cardColors(
            containerColor = RentalWheelsDesignSystem.Colors.Surface02
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Card),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Column(modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.extraLarge)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Brightness6,
                    contentDescription = "Theme",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge)
                )

                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))

                Text(
                    text = "Theme",
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            LazyRow(
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
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
            ThemePreference.LIGHT -> Triple(Icons.Default.Brightness4, "Light", "Always light")
            ThemePreference.DARK -> Triple(Icons.Default.Brightness4, "Dark", "Always dark")
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
        modifier =
        Modifier
            .width(RentalWheelsDesignSystem.Spacing.Huge * 1.56f)
            .clickable {
                onSelected()
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Card),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation =
            if (isSelected) RentalWheelsDesignSystem.Elevation.large
            else RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = contentColor,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge)
                )

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = contentColor,
                        modifier =
                        Modifier
                            .size(RentalWheelsDesignSystem.Spacing.medium)
                            .clip(CircleShape)
                            .background(backgroundColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Text(
                text = title,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = subtitle,
                style = RentalWheelsDesignSystem.Typography.labelSmall,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun SettingsSection(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = RentalWheelsDesignSystem.Colors.Surface02
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Card),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Column(modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.medium)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = RentalWheelsDesignSystem.Spacing.small)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                )

                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))

                Text(
                    text = title,
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
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
            .padding(
                vertical = RentalWheelsDesignSystem.Spacing.small,
                horizontal = RentalWheelsDesignSystem.Spacing.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
        )

        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        if (trailing != null) {
            trailing()
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
            )
        }
    }
}
