package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.theme.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun BookingButton(price: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Total Price",
                style = Typography.bodyMedium
            )
            Text(
                text = "$$price",
                style = Typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Button(
            onClick = { /* Handle booking */ },
            modifier = Modifier
                .height(48.dp)
                .width(120.dp)
        ) {
            Text("Book Now")
        }
    }
}