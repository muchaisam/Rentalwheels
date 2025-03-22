package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.ui.theme.Typography

@Composable
fun CategoryList(categories: List<Category>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()  // Added wrapContentHeight
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Car Categories", style = Typography.titleLarge)
            Text(
                "See All >",
                color = MaterialTheme.colorScheme.secondary,
                style = Typography.labelLarge
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
            modifier = Modifier.height(120.dp),  // Added wrapContentHeight
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category)
            }
        }
    }
}