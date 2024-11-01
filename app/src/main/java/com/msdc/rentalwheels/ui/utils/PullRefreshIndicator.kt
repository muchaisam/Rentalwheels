package com.msdc.rentalwheels.ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier
) {
    val trigger = state.progress
    Box(
        modifier = modifier
            .size(40.dp)
            .offset(y = (trigger * 80).dp)
    ) {
        if (refreshing) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            CircularProgressIndicator(
                progress = { trigger },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}