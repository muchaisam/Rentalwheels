package com.msdc.rentalwheels.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.theme.Typography

@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier,
    onFilterSelected: (String) -> Unit = {},
    onDriverSwitchChanged: (Boolean) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var withDriver by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Browse",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        // Date and Location
        Row(
            modifier = Modifier
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "11 Nov 12:00 - 14 Nov 13:00",
                color = Color.Gray
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Berlin-Paris",
                color = Color.Gray
            )
        }

        // Filter Chips
        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf("All", "Budget Friendly", "Most Popular", "Off")
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        selectedFilter = filter
                        onFilterSelected(filter)
                    },
                    label = { Text(filter) }
                )
            }
        }

        // Vehicle Count
        Text(
            text = "96 vehicles available",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Driver Switch
        Row(
            modifier = Modifier
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "With driver")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = withDriver,
                onCheckedChange = {
                    withDriver = it
                    onDriverSwitchChanged(it)
                }
            )
        }

        // Vehicle List
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .weight(1f)
        ) {
            // Add vehicle items here
        }
    }
}