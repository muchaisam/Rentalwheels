package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.BookOnline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.CarDescription
import com.msdc.rentalwheels.ui.components.CarFeatures
import com.msdc.rentalwheels.ui.components.CarImage
import com.msdc.rentalwheels.ui.components.CarSpecs
import com.msdc.rentalwheels.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedCarScreen(
    car: Car,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val dominantColorState = remember { mutableStateOf(Color.Transparent) }
    val context = LocalContext.current
    // Get the surface color directly from MaterialTheme
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    LaunchedEffect(car.imageUrl) {
        // Extract dominant color from car image
        withContext(Dispatchers.IO) {
            val bitmap = context.imageLoader.execute(
                ImageRequest.Builder(context)
                    .data(car.imageUrl)
                    .allowHardware(false)
                    .build()
            ).drawable?.toBitmap()

            bitmap?.let {
                val palette = Palette.from(it).generate()
                dominantColorState.value = Color(
                    palette.getDominantColor(surfaceColor)
                )
            }
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = car.brand,
                            style = Typography.titleMedium
                        )
                        Text(
                            text = car.model,
                            style = Typography.headlineSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.semantics {
                            contentDescription = "Navigate back"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item(key = "image") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    CarImage(
                        imageUrl = car.imageUrl,
                        contentDescription = "${car.brand} ${car.model}",
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay for better text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )

                    // Price tag
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "Ksh ${car.dailyRate}/day",
                            style = Typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            item(key = "specs") {
                CarSpecs(
                    car = car,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            item(key = "description") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically()
                ) {
                    CarDescription(
                        description = car.description,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            item(key = "features") {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(
                        animationSpec = tween(
                            durationMillis = 300,
                            delayMillis = 100
                        )
                    )
                ) {
                    CarFeatures(
                        features = car.features,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Add some space before the booking button
            item { Spacer(modifier = Modifier.height(72.dp)) }
        }

        // Floating booking button
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = { /* Handle booking */ },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.BookOnline, contentDescription = null)
                Text("Book Now")
            }
        }
            }
    }
}