package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.components.settings.SettingsContent
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.uistates.Result
import com.msdc.rentalwheels.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val userDataState by viewModel.userData.collectAsState()

    when (val result = userDataState) {
        is Result.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        is Result.Error -> {
            Text(
                text = "Error: ${result.message}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
        is Result.Success -> {
            val userData = result.data
            SettingsContent(
                userData = userData,
                onEditProfileClick = onEditProfileClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}