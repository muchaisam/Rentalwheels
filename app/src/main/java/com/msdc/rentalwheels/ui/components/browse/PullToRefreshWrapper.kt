package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun PullToRefreshWrapper(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var pullOffset by remember { mutableFloatStateOf(0f) }
    var isThresholdReached by remember { mutableStateOf(false) }
    val refreshThreshold = 150f // Threshold in pixels to trigger refresh

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            pullOffset = 0f
            isThresholdReached = false
        }
    }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (isThresholdReached && !isRefreshing) {
                            onRefresh()
                        }
                        pullOffset = 0f
                        isThresholdReached = false
                    }
                ) { _, dragAmount ->
                    if (!isRefreshing) {
                        val newOffset = pullOffset + dragAmount.y
                        pullOffset =
                            if (newOffset > 0) {
                                newOffset.coerceAtMost(refreshThreshold * 1.5f)
                            } else {
                                0f
                            }
                        isThresholdReached = pullOffset >= refreshThreshold
                    }
                }
            }
    ) {
        // Main content
        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = pullOffset * 0.5f }) {
            content()
        }

        // Refresh indicator
        if (pullOffset > 0 || isRefreshing) {
            Column(
                modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .graphicsLayer {
                        alpha = (pullOffset / refreshThreshold).coerceAtMost(1f)
                        translationY = (pullOffset * 0.3f).coerceAtMost(60f)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Pull to refresh",
                        modifier =
                        Modifier
                            .size(24.dp)
                            .rotate(
                                if (isThresholdReached) 180f
                                else (pullOffset / refreshThreshold * 180f)
                            ),
                        tint =
                        if (isThresholdReached) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}
