package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun LoadingScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(5) {
            ShimmerCarItem()
        }
    }
}

@Composable
fun ShimmerCarItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp)
            .shimmer()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(150.dp)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(100.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}