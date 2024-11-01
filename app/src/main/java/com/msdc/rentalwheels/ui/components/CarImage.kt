package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.msdc.rentalwheels.R

@Composable
fun CarImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "Car Image",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cube_filled), // Replace with your cube icon
                contentDescription = "3D View",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }
    }
}