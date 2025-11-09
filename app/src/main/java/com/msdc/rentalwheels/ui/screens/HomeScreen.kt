package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.components.CategoryList
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.PromotionBanner
import com.msdc.rentalwheels.ui.components.home.FilterableCarList
import com.msdc.rentalwheels.ui.components.home.RecommendedCarsSection
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.uistates.HomeScreenState
import com.msdc.rentalwheels.viewmodel.CarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Premium Home Screen with staggered animations and polished UI */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: CarViewModel, onCarClick: (String) -> Unit) {
    val homeState by viewModel.homeState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    isRefreshing = true
                    viewModel.loadInitialData()
                    delay(300) // Brief delay for smooth animation
                    isRefreshing = false
                }
            }
        )

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Pull-to-refresh wrapper
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when (val state = homeState) {
                is HomeScreenState.Loading -> {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(RentalWheelsDesignSystem.Animation.Medium)),
                        exit = fadeOut(tween(RentalWheelsDesignSystem.Animation.Fast))
                    ) { LoadingScreen() }
                }

                is HomeScreenState.Error -> {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        fadeIn(tween(RentalWheelsDesignSystem.Animation.Medium)) +
                                slideInVertically {
                                    with(density) { 30.dp.roundToPx() }
                                },
                        exit = fadeOut(tween(RentalWheelsDesignSystem.Animation.Fast))
                    ) { ErrorScreen(message = state.message) }
                }

                is HomeScreenState.Success -> {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(RentalWheelsDesignSystem.Animation.Slow)),
                        exit = fadeOut(tween(RentalWheelsDesignSystem.Animation.Medium))
                    ) {
                        // Content sections with staggered animations
                        val sections = buildList {
                            if (state.deals.isNotEmpty()) add("deals" to state.deals.size)
                            if (state.categories.isNotEmpty())
                                add("categories" to state.categories.size)
                            if (state.recommendedCars.isNotEmpty())
                                add("recommended" to state.recommendedCars.size)
                            add("fuelType" to state.carsByFuelType.size)
                            add("year" to state.carsByYear.size)
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding =
                            PaddingValues(
                                start = RentalWheelsDesignSystem.Spacing.medium,
                                end = RentalWheelsDesignSystem.Spacing.medium,
                                top = RentalWheelsDesignSystem.Spacing.extraSmall,
                                bottom =
                                RentalWheelsDesignSystem.Spacing.Huge +
                                        RentalWheelsDesignSystem.Spacing
                                            .XXL // Space for bottom nav
                            ),
                            verticalArrangement =
                            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.large)
                        ) {
                            // Deals Section with staggered animation
                            if (state.deals.isNotEmpty()) {
                                item(key = "deals") {
                                    AnimatedSection(delay = 0L) {
                                        PromotionBanner(
                                            deals = state.deals,
                                            modifier =
                                            Modifier.padding(
                                                vertical =
                                                RentalWheelsDesignSystem
                                                    .Spacing.extraSmall
                                            )
                                        )
                                    }
                                }
                            }

                            // Categories Section with staggered animation
                            if (state.categories.isNotEmpty()) {
                                item(key = "categories") {
                                    AnimatedSection(delay = 100L) {
                                        CategoryList(categories = state.categories)
                                    }
                                }
                            }

                            // Recommended Cars Section with staggered animation
                            if (state.recommendedCars.isNotEmpty()) {
                                item(key = "recommended") {
                                    AnimatedSection(delay = 200L) {
                                        RecommendedCarsSection(
                                            cars = state.recommendedCars,
                                            onCarClick = onCarClick
                                        )
                                    }
                                }
                            }

                            // Cars by Fuel Type Section with staggered animation
                            item(key = "fuelType") {
                                AnimatedSection(delay = 300L) {
                                    FilterableCarList(
                                        carsByFilter = state.carsByFuelType,
                                        filterName = "Fuel Type",
                                        onCarClick = onCarClick
                                    )
                                }
                            }

                            // Cars by Year Section with staggered animation
                            item(key = "year") {
                                AnimatedSection(delay = 400L) {
                                    FilterableCarList(
                                        carsByFilter = state.carsByYear,
                                        filterName = "Year",
                                        onCarClick = onCarClick
                                    )
                                }
                            }
                        }

                        // Enhanced pull refresh indicator
                        PullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter),
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            scale = true
                        )
                    }
                }
            }
        }
    }
}

/** Reusable animated section wrapper with staggered delay */
@Composable
private fun AnimatedSection(
    delay: Long = 0L,
    content: @Composable () -> Unit
) {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay)
        animation.animateTo(
            targetValue = 1f,
            animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(
        modifier =
        Modifier.graphicsLayer {
            alpha = animation.value
            translationY = (1 - animation.value) * 50
        }
    ) { content() }
}