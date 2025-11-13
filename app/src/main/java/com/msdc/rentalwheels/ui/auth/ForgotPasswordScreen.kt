package com.msdc.rentalwheels.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    isLoading: Boolean = false,
    isEmailSent: Boolean = false,
    errorMessage: String? = null,
    onSendResetEmail: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Animation states
    val headerScale by
    animateFloatAsState(
        targetValue = if (isEmailSent) 0.9f else 1f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "headerScale"
    )

    val formScale by
    animateFloatAsState(
        targetValue = if (isLoading) 0.95f else 1f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "formScale"
    )

    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        // Content
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(RentalWheelsDesignSystem.Spacing.extraLarge)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(bottom = RentalWheelsDesignSystem.Spacing.medium)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f))

            // Header Section
            Column(
                modifier = Modifier.scale(headerScale),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = !isEmailSent,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // App Icon
                        Card(
                            modifier =
                            Modifier.size(
                                RentalWheelsDesignSystem.Spacing.Huge * 1.25f
                            ),
                            shape = RoundedCornerShape(RentalWheelsDesignSystem.Spacing.large),
                            colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation =
                            CardDefaults.cardElevation(
                                defaultElevation =
                                RentalWheelsDesignSystem.Elevation.medium
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.mipmap.launc),
                                    contentDescription = "App Logo",
                                    modifier =
                                    Modifier.size(
                                        RentalWheelsDesignSystem.Spacing.Huge *
                                                0.75f
                                    )
                                )
                            }
                        }

                        Spacer(
                            modifier =
                            Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge)
                        )

                        Text(
                            text = "Reset Password",
                            style = RentalWheelsDesignSystem.Typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                        Text(
                            text =
                            "Enter your email address and we'll send you a link to reset your password",
                            style = RentalWheelsDesignSystem.Typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight =
                            RentalWheelsDesignSystem.Typography.bodyLarge.lineHeight
                        )
                    }
                }

                // Success State
                AnimatedVisibility(
                    visible = isEmailSent,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            modifier =
                            Modifier.size(
                                RentalWheelsDesignSystem.Spacing.Huge * 1.25f
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier =
                            Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge)
                        )

                        Text(
                            text = "Email Sent!",
                            style = RentalWheelsDesignSystem.Typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                        Text(
                            text =
                            "We've sent a password reset link to your email address. Please check your inbox and follow the instructions.",
                            style = RentalWheelsDesignSystem.Typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight =
                            RentalWheelsDesignSystem.Typography.bodyLarge.lineHeight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.Huge * 0.75f))

            // Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(formScale),
                shape = RoundedCornerShape(RentalWheelsDesignSystem.Spacing.extraLarge),
                colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation =
                CardDefaults.cardElevation(
                    defaultElevation = RentalWheelsDesignSystem.Elevation.medium
                )
            ) {
                Column(
                    modifier =
                    Modifier.padding(
                        RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Error Message
                    AnimatedVisibility(
                        visible = errorMessage != null,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        errorMessage?.let {
                            Card(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom =
                                        RentalWheelsDesignSystem.Spacing
                                            .medium
                                    ),
                                colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                    MaterialTheme.colorScheme.errorContainer
                                ),
                                shape =
                                RoundedCornerShape(
                                    RentalWheelsDesignSystem.CornerRadius.medium
                                )
                            ) {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                    modifier =
                                    Modifier.padding(
                                        RentalWheelsDesignSystem.Spacing.medium
                                    )
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = !isEmailSent,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Column {
                            // Email Input
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    isEmailValid =
                                        android.util.Patterns.EMAIL_ADDRESS
                                            .matcher(it)
                                            .matches()
                                },
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                label = { Text("Email Address") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email"
                                    )
                                },
                                keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        if (email.isNotBlank() && isEmailValid) {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                            onSendResetEmail(email)
                                        }
                                    }
                                ),
                                singleLine = true,
                                isError = !isEmailValid && email.isNotBlank(),
                                supportingText = {
                                    if (!isEmailValid && email.isNotBlank()) {
                                        Text(
                                            text = "Please enter a valid email address",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            )

                            Spacer(
                                modifier =
                                Modifier.height(
                                    RentalWheelsDesignSystem.Spacing.extraLarge *
                                            1.33f
                                )
                            )

                            // Send Reset Email Button
                            Button(
                                onClick = {
                                    if (email.isNotBlank() && isEmailValid) {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                        onSendResetEmail(email)
                                    }
                                },
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        RentalWheelsDesignSystem.Spacing.Huge *
                                                0.875f
                                    ),
                                enabled = !isLoading && email.isNotBlank() && isEmailValid,
                                colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                    MaterialTheme.colorScheme.primary,
                                    contentColor =
                                    MaterialTheme.colorScheme.onPrimary
                                ),
                                shape =
                                RoundedCornerShape(
                                    RentalWheelsDesignSystem.CornerRadius.large
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier =
                                        Modifier.size(
                                            RentalWheelsDesignSystem.Spacing
                                                .extraLarge
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = RentalWheelsDesignSystem.Spacing.XXS
                                    )
                                } else {
                                    Text(
                                        text = "Send Reset Link",
                                        style = RentalWheelsDesignSystem.Typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    // Success State Buttons
                    AnimatedVisibility(
                        visible = isEmailSent,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Column {
                            // Retry Button
                            Button(
                                onClick = onRetryClick,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(
                                        RentalWheelsDesignSystem.Spacing.Huge *
                                                0.875f
                                    ),
                                colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                    MaterialTheme.colorScheme.secondary,
                                    contentColor =
                                    MaterialTheme.colorScheme.onSecondary
                                ),
                                shape =
                                RoundedCornerShape(
                                    RentalWheelsDesignSystem.CornerRadius.large
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Resend",
                                    modifier =
                                    Modifier.size(
                                        RentalWheelsDesignSystem.Spacing.large
                                    )
                                )
                                Spacer(
                                    modifier =
                                    Modifier.width(
                                        RentalWheelsDesignSystem.Spacing.small
                                    )
                                )
                                Text(
                                    text = "Resend Email",
                                    style = RentalWheelsDesignSystem.Typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(
                                modifier =
                                Modifier.height(RentalWheelsDesignSystem.Spacing.medium)
                            )

                            // Back to Login Button
                            TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Back to Login",
                                    style = RentalWheelsDesignSystem.Typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f))
        }
    }

    // Auto-focus email field when screen loads
    LaunchedEffect(Unit) {
        delay(300)
        if (!isEmailSent) {
            focusRequester.requestFocus()
        }
    }
}
