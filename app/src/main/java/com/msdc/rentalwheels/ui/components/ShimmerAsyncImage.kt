package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
fun ShimmerAsyncImage(
    model: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize()
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    isLoading = true
                    isError = false
                    ShimmerEffect()
                }
                is AsyncImagePainter.State.Error -> {
                    isLoading = false
                    isError = true
                    ShimmerEffect()
                }
                else -> {
                    isLoading = false
                    isError = false
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}