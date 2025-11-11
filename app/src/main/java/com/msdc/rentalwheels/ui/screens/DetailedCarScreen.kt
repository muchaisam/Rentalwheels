package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.ui.components.CarDescription
import com.msdc.rentalwheels.ui.components.CarFeatures
import com.msdc.rentalwheels.ui.components.CarImage
import com.msdc.rentalwheels.ui.components.CarSpecs
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedCarScreen(
    car: Car,
    onBackClick: () -> Unit,
    onBookNowClick: (Car) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Staggered entrance animations
    val specsAlpha = remember { Animatable(0f) }
    val descriptionAlpha = remember { Animatable(0f) }
    val featuresAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        specsAlpha.animateTo(
            targetValue = 1f,
            animationSpec =
            tween(
                durationMillis = RentalWheelsDesignSystem.Animation.Medium,
                delayMillis = 100
            )
        )
        descriptionAlpha.animateTo(
            targetValue = 1f,
            animationSpec =
            tween(
                durationMillis = RentalWheelsDesignSystem.Animation.Medium,
                delayMillis = 200
            )
        )
        featuresAlpha.animateTo(
            targetValue = 1f,
            animationSpec =
            tween(
                durationMillis = RentalWheelsDesignSystem.Animation.Medium,
                delayMillis = 300
            )
        )
    }

    val dominantColorState = remember { mutableStateOf(Color.Transparent) }
    val context = LocalContext.current
    // Get the surface color directly from MaterialTheme
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    LaunchedEffect(car.imageUrl) {
        // Extract dominant color from car image
        withContext(Dispatchers.IO) {
            val bitmap =
                context.imageLoader
                    .execute(
                        ImageRequest.Builder(context)
                            .data(car.imageUrl)
                            .allowHardware(false)
                            .build()
                    )
                    .drawable
                    ?.toBitmap()

            bitmap?.let {
                val palette = Palette.from(it).generate()
                dominantColorState.value = Color(palette.getDominantColor(surfaceColor))
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
                            text = car.make,
                            style = RentalWheelsDesignSystem.Typography.titleMedium
                        )
                        Text(
                            text = car.model,
                            style = RentalWheelsDesignSystem.Typography.headlineSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier =
                        Modifier.semantics {
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
                colors =
                TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor =
                    MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            item(key = "image") {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(RentalWheelsDesignSystem.Spacing.Huge * 4.6875f)
                ) { // ~300dp
                    CarImage(
                        imageUrl = car.imageUrl,
                        contentDescription = "${car.brand} ${car.model}",
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay for better text visibility
                    Box(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors =
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(
                                            alpha = 0.4f
                                        )
                                    )
                                )
                            )
                    )

                    // Price tag
                    Surface(
                        modifier =
                        Modifier
                            .padding(RentalWheelsDesignSystem.Spacing.medium)
                            .align(Alignment.TopEnd),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = CurrencyUtils.formatDailyRate(car.dailyRate),
                            style = RentalWheelsDesignSystem.Typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier =
                            Modifier.padding(
                                horizontal =
                                RentalWheelsDesignSystem.Spacing.medium,
                                vertical = RentalWheelsDesignSystem.Spacing.small
                            )
                        )
                    }
                }
            }

            item(key = "specs") {
                CarSpecs(
                    car = car,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(RentalWheelsDesignSystem.Spacing.medium)
                        .graphicsLayer {
                            alpha = specsAlpha.value
                            translationY = (1f - specsAlpha.value) * 50f
                        }
                )
            }

            item(key = "description") {
                AnimatedVisibility(visible = true, enter = fadeIn() + expandVertically()) {
                    CarDescription(
                        description = car.description,
                        modifier =
                        Modifier
                            .padding(
                                horizontal =
                                RentalWheelsDesignSystem.Spacing.medium
                            )
                            .graphicsLayer {
                                alpha = descriptionAlpha.value
                                translationY = (1f - descriptionAlpha.value) * 50f
                            }
                    )
                }
            }

            item(key = "features") {
                AnimatedVisibility(
                    visible = true,
                    enter =
                    fadeIn() +
                            expandVertically(
                                animationSpec =
                                tween(
                                    durationMillis = 300,
                                    delayMillis = 100
                                )
                            )
                ) {
                    CarFeatures(
                        features = car.features,
                        modifier =
                        Modifier
                            .padding(RentalWheelsDesignSystem.Spacing.medium)
                            .graphicsLayer {
                                alpha = featuresAlpha.value
                                translationY = (1f - featuresAlpha.value) * 50f
                            }
                    )
                }
            }

            // Add some space before the booking button
            item {
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge * 3))
            }
        }

        // Floating booking button with animation
        Box(modifier = Modifier.fillMaxSize()) {
            val fabInteractionSource = remember { MutableInteractionSource() }
            val isFabPressed = fabInteractionSource.collectIsPressedAsState()
            val fabScale by
            animateFloatAsState(
                targetValue = if (isFabPressed.value) 0.92f else 1f,
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "fabScale"
            )

            FloatingActionButton(
                onClick = { onBookNowClick(car) },
                modifier =
                Modifier
                    .padding(RentalWheelsDesignSystem.Spacing.medium)
                    .align(Alignment.BottomEnd)
                    .graphicsLayer {
                        scaleX = fabScale
                        scaleY = fabScale
                    },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                interactionSource = fabInteractionSource
            ) {
                Row(
                    modifier =
                    Modifier.padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium
                    ),
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.BookOnline, contentDescription = null)
                    Text(
                        text = "Book Now",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
