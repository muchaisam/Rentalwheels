package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.bookings.AvailableCarsSection
import com.msdc.rentalwheels.ui.components.bookings.BookingsHeader
import com.msdc.rentalwheels.ui.components.bookings.BookingsList
import com.msdc.rentalwheels.ui.components.bookings.CartView
import com.msdc.rentalwheels.uistates.BookingStatus
import com.msdc.rentalwheels.uistates.BookingsScreenState
import com.msdc.rentalwheels.viewmodel.BookingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    onBookingClick: (String) -> Unit,
    onNewBookingClick: () -> Unit,
    onCarClick: (String) -> Unit,
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BookingsViewModel = hiltViewModel()
) {
    val bookingsState by viewModel.bookingsState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            floatingActionButton = {
                val state = bookingsState
                if (state is BookingsScreenState.Success) {
                    FloatingActionButton(
                        onClick = {
                            if (state.hasBookings) {
                                onNewBookingClick()
                            } else {
                                viewModel.toggleCartMode()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(
                            imageVector =
                            if (state.hasBookings || state.showCartMode) {
                                Icons.Default.Add
                            } else {
                                Icons.Default.ShoppingCart
                            },
                            contentDescription =
                            if (state.hasBookings) "New Booking" else "View Cart"
                        )
                    }
                }
            }
        ) { paddingValues ->
            when (val state = bookingsState) {
                is BookingsScreenState.Loading -> {
                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        LoadingScreen()
                    }
                }

                is BookingsScreenState.Error -> {
                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        ErrorScreen(message = state.message, onRetry = { viewModel.loadBookings() })
                    }
                }

                is BookingsScreenState.Success -> {
                    AnimatedContent(
                        targetState = state.hasBookings,
                        transitionSpec = {
                            slideInHorizontally { width -> width } togetherWith
                                    slideOutHorizontally { width -> -width }
                        },
                        label = "bookings_content"
                    ) { hasBookings ->
                        if (hasBookings) {
                            // User has bookings - show traditional booking management
                            BookingManagementView(
                                state = state,
                                onBookingClick = onBookingClick,
                                onCarClick = onCarClick,
                                onAnalyticsClick = onAnalyticsClick,
                                onCancelBooking = { bookingId ->
                                    viewModel.cancelBooking(bookingId)
                                    viewModel.trackBookingAction("cancelled", bookingId)
                                },
                                onExtendBooking = { bookingId ->
                                    viewModel.extendBooking(bookingId)
                                    viewModel.trackBookingAction("extended", bookingId)
                                },
                                onRebookCar = { carId -> onCarClick(carId) },
                                onToggleFavorite = { bookingId ->
                                    if (state.favoriteCarIds.contains(bookingId)) {
                                        viewModel.removeBookingFromFavorites(bookingId)
                                    } else {
                                        viewModel.saveBookingToFavorites(bookingId)
                                    }
                                    viewModel.trackBookingAction("toggle_favorite", bookingId)
                                },
                                modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(horizontal = 16.dp)
                            )
                        } else {
                            // User has no bookings - show available cars or cart
                            Column(
                                modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(horizontal = 16.dp)
                            ) {
                                AnimatedContent(
                                    targetState = state.showCartMode,
                                    transitionSpec = {
                                        slideInHorizontally { width ->
                                            if (targetState) width else -width
                                        } togetherWith
                                                slideOutHorizontally { width ->
                                                    if (targetState) -width else width
                                                }
                                    },
                                    label = "cart_mode_content"
                                ) { showCart ->
                                    if (showCart) {
                                        CartView(
                                            cartItems = state.cartItems,
                                            cartTotal = viewModel.getCartTotal(),
                                            onRemoveFromCart = { carId ->
                                                viewModel.removeFromCart(carId)
                                            },
                                            onEditCartItem = { cartItem ->
                                                // TODO: Implement cart item editing
                                            },
                                            onProcessCart = { viewModel.processCartBookings() },
                                            onClearCart = { viewModel.clearCart() }
                                        )
                                    } else {
                                        AvailableCarsSection(
                                            availableCars = state.availableCars,
                                            cartItems = state.cartItems,
                                            onAddToCart = { car -> viewModel.addToCart(car) },
                                            onCarClick = onCarClick,
                                            onViewCart = { viewModel.toggleCartMode() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Load initial data
    LaunchedEffect(Unit) { viewModel.loadBookings() }
}

@Composable
private fun BookingManagementView(
    state: BookingsScreenState.Success,
    onBookingClick: (String) -> Unit,
    onCarClick: (String) -> Unit,
    onAnalyticsClick: () -> Unit,
    onCancelBooking: (String) -> Unit,
    onExtendBooking: (String) -> Unit,
    onRebookCar: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Upcoming", "Active", "Past", "All")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    // Sync tab selection with pager
    LaunchedEffect(selectedTabIndex) { pagerState.animateScrollToPage(selectedTabIndex) }
    LaunchedEffect(pagerState.currentPage) { selectedTabIndex = pagerState.currentPage }

    Column(modifier = modifier.fillMaxSize()) {
        // Header with user stats
        BookingsHeader(
            totalBookings = state.allBookings.size,
            activeBookings = state.activeBookings.size,
            upcomingBookings = state.upcomingBookings.size,
            onAnalyticsClick = onAnalyticsClick,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight =
                            if (selectedTabIndex == index) FontWeight.Bold
                            else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Paged Content
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val bookingsToShow =
                when (page) {
                    0 -> state.upcomingBookings
                    1 -> state.activeBookings
                    2 -> state.pastBookings
                    3 -> state.allBookings
                    else -> emptyList()
                }

            val statusFilter =
                when (page) {
                    0 -> BookingStatus.CONFIRMED
                    1 -> BookingStatus.ACTIVE
                    2 -> BookingStatus.COMPLETED
                    else -> null
                }

            BookingsList(
                bookings = bookingsToShow,
                onBookingClick = onBookingClick,
                onCarClick = onCarClick,
                onCancelBooking = onCancelBooking,
                onExtendBooking = onExtendBooking,
                onRebookCar = onRebookCar,
                onToggleFavorite = onToggleFavorite,
                favoriteBookingIds = state.favoriteCarIds,
                statusFilter = statusFilter,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
