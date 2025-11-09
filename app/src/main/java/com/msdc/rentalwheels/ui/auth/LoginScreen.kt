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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.components.PremiumButton
import com.msdc.rentalwheels.ui.components.PremiumButtonVariant
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
        isLoading: Boolean = false,
        errorMessage: String? = null,
        onLoginClick: (email: String, password: String) -> Unit,
        onRegisterClick: () -> Unit,
        onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isFormAnimated by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Trigger animation after composition
    LaunchedEffect(Unit) {
        delay(300)
        isFormAnimated = true
    }

    // Animated scale for the form
    val formScale by
            animateFloatAsState(
                    targetValue = if (isFormAnimated) 1f else 0.8f,
                    animationSpec =
                            spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                            ),
                    label = "FormScale"
            )

    // Dynamic background gradient
    val backgroundBrush =
            Brush.verticalGradient(
                    colors =
                            listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
            )
    Box(
            modifier =
                    Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .imePadding()
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(RentalWheelsDesignSystem.Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            // App Logo/Title Section
            AnimatedVisibility(
                    visible = isFormAnimated,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
            ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier =
                                Modifier.padding(
                                        bottom = RentalWheelsDesignSystem.Spacing.extraLarge * 2
                                )
                ) { // App Logo
                    Card(
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f),
                            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                            colors =
                                    CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                    ),
                            elevation =
                                    CardDefaults.cardElevation(
                                            defaultElevation =
                                                    RentalWheelsDesignSystem.Elevation.small
                                    )
                    ) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) {
                            Image(
                                    painter = painterResource(id = R.drawable.rental_logo),
                                    contentDescription = "App Logo",
                                    modifier =
                                            Modifier.size(
                                                    RentalWheelsDesignSystem.Spacing.Huge * 0.75f
                                            )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                    Text(
                            text = "Welcome Back",
                            style = RentalWheelsDesignSystem.Typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                            text = "Sign in to your account",
                            style = RentalWheelsDesignSystem.Typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                    )
                }
            }

            // Login Form Card
            Card(
                    modifier = Modifier.fillMaxWidth().scale(formScale),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                            ),
                    elevation =
                            CardDefaults.cardElevation(
                                    defaultElevation = RentalWheelsDesignSystem.Elevation.small
                            )
            ) {
                Column(
                        modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.extraLarge),
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
                                            Modifier.fillMaxWidth()
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
                                        modifier =
                                                Modifier.padding(
                                                        RentalWheelsDesignSystem.Spacing.medium
                                                ),
                                        style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Email Field
                    OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                isEmailValid =
                                        android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() ||
                                                it.isEmpty()
                            },
                            label = { Text("Email Address") },
                            leadingIcon = {
                                Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint =
                                                if (isEmailValid) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.error
                                )
                            },
                            isError = !isEmailValid,
                            supportingText =
                                    if (!isEmailValid) {
                                        { Text("Please enter a valid email address") }
                                    } else null,
                            keyboardOptions =
                                    KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next
                                    ),
                            keyboardActions =
                                    KeyboardActions(
                                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                    ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large)
                    )

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                    // Password Field
                    OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                isPasswordValid = it.length >= 6 || it.isEmpty()
                            },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint =
                                                if (isPasswordValid)
                                                        MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.error
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                            imageVector =
                                                    if (passwordVisible) Icons.Default.Visibility
                                                    else Icons.Default.VisibilityOff,
                                            contentDescription =
                                                    if (passwordVisible) "Hide password"
                                                    else "Show password"
                                    )
                                }
                            },
                            visualTransformation =
                                    if (passwordVisible) VisualTransformation.None
                                    else PasswordVisualTransformation(),
                            isError = !isPasswordValid,
                            supportingText =
                                    if (!isPasswordValid) {
                                        { Text("Password must be at least 6 characters") }
                                    } else null,
                            keyboardOptions =
                                    KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Done
                                    ),
                            keyboardActions =
                                    KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                                if (email.isNotEmpty() &&
                                                                password.isNotEmpty() &&
                                                                isEmailValid &&
                                                                isPasswordValid
                                                ) {
                                                    onLoginClick(email, password)
                                                }
                                            }
                                    ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large)
                    )

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                    // Forgot Password
                    TextButton(
                            onClick = onForgotPasswordClick,
                            modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                                text = "Forgot Password?",
                                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                    // Login Button - Premium Design
                    PremiumButton(
                            onClick = {
                                if (email.isNotEmpty() &&
                                                password.isNotEmpty() &&
                                                isEmailValid &&
                                                isPasswordValid
                                ) {
                                    onLoginClick(email, password)
                                }
                            },
                            variant = PremiumButtonVariant.Primary,
                            enabled =
                                    !isLoading &&
                                            email.isNotEmpty() &&
                                            password.isNotEmpty() &&
                                            isEmailValid &&
                                            isPasswordValid,
                            modifier = Modifier.fillMaxWidth()
                    ) { Text(if (isLoading) "Signing In..." else "Sign In") }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                    // Register Link
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                                text = "Don't have an account? ",
                                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        TextButton(onClick = onRegisterClick) {
                            Text(
                                    text = "Sign Up",
                                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
