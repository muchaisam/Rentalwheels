package com.msdc.rentalwheels.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String,
    val primaryColor: Color = Color.Unspecified
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onGetStarted: () -> Unit, onSkip: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val onboardingPages =
        listOf(
            OnboardingPage(
                imageRes = R.drawable.landcruiser,
                title = "Discover Your Perfect Ride",
                description =
                "Choose from our premium collection of vehicles tailored to your journey and lifestyle preferences.",
                primaryColor = MaterialTheme.colorScheme.primary
            ),
            OnboardingPage(
                imageRes = R.drawable.landcruiser,
                title = "Seamless Booking Experience",
                description =
                "Book your ideal vehicle in just a few taps with our intuitive and secure reservation system.",
                primaryColor = MaterialTheme.colorScheme.tertiary
            ),
            OnboardingPage(
                imageRes = R.drawable.wheels,
                title = "24/7 Premium Support",
                description =
                "Enjoy peace of mind with our round-the-clock customer support and comprehensive roadside assistance.",
                primaryColor = MaterialTheme.colorScheme.secondary
            )
        )

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    var isAnimating by remember { mutableStateOf(false) }

    // Create animated background gradient based on current page
    val currentPage = pagerState.currentPage
    val targetPage = pagerState.targetPage
    val pageOffset = pagerState.currentPageOffsetFraction

    val colorScheme = MaterialTheme.colorScheme
    val backgroundBrush =
        remember(currentPage, pageOffset, colorScheme) {
            val currentColor =
                onboardingPages[currentPage].primaryColor.takeIf { it != Color.Unspecified }
                    ?: colorScheme.primary
            val nextColor =
                if (targetPage < onboardingPages.size) {
                    onboardingPages[targetPage].primaryColor.takeIf {
                        it != Color.Unspecified
                    }
                        ?: colorScheme.primary
                } else currentColor

            Brush.radialGradient(
                colors =
                listOf(
                    currentColor.copy(alpha = 0.1f),
                    nextColor.copy(alpha = 0.05f),
                    colorScheme.surface
                ),
                radius = 1200f
            )
        }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            // Skip Button
            AnimatedVisibility(
                visible = currentPage < onboardingPages.size - 1,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                TextButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSkip()
                    }
                ) {
                    Text(
                        text = "Skip",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Main Content
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(RentalWheelsDesignSystem.Spacing.extraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.Huge * 0.94f))

                // Pager Content
                HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                    OnboardingPageContent(
                        page = onboardingPages[page],
                        isVisible = page == currentPage,
                        pageOffset = if (page == currentPage) pageOffset else 0f
                    )
                }

                Spacer(
                    modifier =
                    Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f)
                )

                // Page Indicators
                OnboardingIndicators(
                    pageCount = onboardingPages.size,
                    currentPage = currentPage,
                    pageOffset = pageOffset
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.Huge * 0.75f))

                // Navigation Buttons
                OnboardingNavigation(
                    currentPage = currentPage,
                    totalPages = onboardingPages.size,
                    isAnimating = isAnimating,
                    onPrevious = {
                        if (currentPage > 0 && !isAnimating) {
                            isAnimating = true
                            scope.launch {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                pagerState.animateScrollToPage(currentPage - 1)
                                delay(300)
                                isAnimating = false
                            }
                        }
                    },
                    onNext = {
                        if (currentPage < onboardingPages.size - 1 && !isAnimating) {
                            isAnimating = true
                            scope.launch {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                pagerState.animateScrollToPage(currentPage + 1)
                                delay(300)
                                isAnimating = false
                            }
                        }
                    },
                    onGetStarted = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onGetStarted()
                    }
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage, isVisible: Boolean, pageOffset: Float) {
    val scale by
    animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.9f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "PageScale"
    )

    val alpha by
    animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.7f,
        animationSpec = tween(300),
        label = "PageAlpha"
    )

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                translationX = pageOffset * 100f
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hero Image Card
        Card(
            modifier =
            Modifier
                .size(RentalWheelsDesignSystem.Spacing.Huge * 5f)
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.Spacing.extraLarge),
            elevation =
            CardDefaults.cardElevation(
                defaultElevation = RentalWheelsDesignSystem.Spacing.small
            ),
            colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge * 1.67f))

        // Title
        Text(
            text = page.title,
            style = RentalWheelsDesignSystem.Typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.medium)
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

        // Description
        Text(
            text = page.description,
            style = RentalWheelsDesignSystem.Typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = RentalWheelsDesignSystem.Typography.bodyLarge.lineHeight,
            modifier =
            Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.extraLarge)
        )
    }
}

@Composable
private fun OnboardingIndicators(pageCount: Int, currentPage: Int, pageOffset: Float) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            val width by
            animateDpAsState(
                targetValue =
                if (isSelected)
                    RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f
                else RentalWheelsDesignSystem.Spacing.small,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "IndicatorWidth"
            )

            val alpha by
            animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.4f,
                animationSpec = tween(300),
                label = "IndicatorAlpha"
            )

            Box(
                modifier =
                Modifier
                    .width(width)
                    .height(RentalWheelsDesignSystem.Spacing.small)
                    .padding(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.Spacing.extraSmall
                        )
                    )
                    .clickable(
                        interactionSource =
                        remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Optional: Allow jumping to specific page
                    }
            )
        }
    }
}

@Composable
private fun OnboardingNavigation(
    currentPage: Int,
    totalPages: Int,
    isAnimating: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onGetStarted: () -> Unit
) {
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Button
        AnimatedVisibility(
            visible = currentPage > 0,
            enter = fadeIn() + slideInHorizontally { -it / 2 },
            exit = fadeOut() + slideOutHorizontally { -it / 2 }
        ) {
            FilledTonalButton(
                onClick = onPrevious,
                enabled = !isAnimating,
                modifier = Modifier.padding(end = RentalWheelsDesignSystem.Spacing.small)
            ) { Text(text = "Previous", style = RentalWheelsDesignSystem.Typography.labelLarge) }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next/Get Started Button
        Button(
            onClick = if (isLastPage) onGetStarted else onNext,
            enabled = !isAnimating,
            modifier = Modifier.padding(start = RentalWheelsDesignSystem.Spacing.small),
            colors =
            ButtonDefaults.buttonColors(
                containerColor =
                if (isLastPage) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large)
        ) {
            AnimatedVisibility(
                visible = !isLastPage,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Next",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier =
                        Modifier.size(RentalWheelsDesignSystem.Spacing.medium * 1.125f)
                    )
                }
            }

            AnimatedVisibility(
                visible = isLastPage,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier =
                        Modifier.size(RentalWheelsDesignSystem.Spacing.medium * 1.125f)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Text(
                        text = "Get Started",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
