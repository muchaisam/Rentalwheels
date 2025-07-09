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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.Typography
import kotlinx.coroutines.delay

data class RegisterFormData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

@Composable
fun RegisterScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRegisterClick: (RegisterFormData) -> Unit,
    onLoginClick: () -> Unit
) {
    var formData by remember { mutableStateOf(RegisterFormData()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isFormAnimated by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Form validation states
    val isEmailValid by remember {
        derivedStateOf {
            android.util.Patterns.EMAIL_ADDRESS.matcher(formData.email).matches() ||
                    formData.email.isEmpty()
        }
    }
    val isPhoneValid by remember {
        derivedStateOf { formData.phone.length >= 10 || formData.phone.isEmpty() }
    }
    val isPasswordValid by remember {
        derivedStateOf { formData.password.length >= 6 || formData.password.isEmpty() }
    }
    val doPasswordsMatch by remember {
        derivedStateOf {
            formData.password == formData.confirmPassword || formData.confirmPassword.isEmpty()
        }
    }

    // Password strength calculation
    val passwordStrength by remember {
        derivedStateOf {
            if (formData.password.isEmpty()) return@derivedStateOf 0f
            var strength = 0f
            if (formData.password.length >= 6) strength += 0.25f
            if (formData.password.length >= 8) strength += 0.25f
            if (formData.password.any { it.isUpperCase() }) strength += 0.25f
            if (formData.password.any { it.isDigit() }) strength += 0.25f
            strength
        }
    }

    val isFormValid by remember {
        derivedStateOf {
            formData.firstName.isNotEmpty() &&
                    formData.lastName.isNotEmpty() &&
                    formData.email.isNotEmpty() &&
                    formData.phone.isNotEmpty() &&
                    formData.password.isNotEmpty() &&
                    formData.confirmPassword.isNotEmpty() &&
                    isEmailValid &&
                    isPhoneValid &&
                    isPasswordValid &&
                    doPasswordsMatch
        }
    }

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
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
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
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    // Logo placeholder                    // App Logo
                    Card(
                        modifier = Modifier.size(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.rental_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Create Account",
                        style = Typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Join us for seamless car rental experience",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Register Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(formScale),
                shape = RoundedCornerShape(24.dp),
                colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                    MaterialTheme.colorScheme.errorContainer
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = it,
                                    modifier = Modifier.padding(16.dp),
                                    style = Typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Name Fields Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // First Name
                        OutlinedTextField(
                            value = formData.firstName,
                            onValueChange = { formData = formData.copy(firstName = it) },
                            label = { Text("First Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Right)
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        )

                        // Last Name
                        OutlinedTextField(
                            value = formData.lastName,
                            onValueChange = { formData = formData.copy(lastName = it) },
                            label = { Text("Last Name") },
                            keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    OutlinedTextField(
                        value = formData.email,
                        onValueChange = { formData = formData.copy(email = it) },
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
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Field
                    OutlinedTextField(
                        value = formData.phone,
                        onValueChange = { formData = formData.copy(phone = it) },
                        label = { Text("Phone Number") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint =
                                if (isPhoneValid) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                            )
                        },
                        isError = !isPhoneValid,
                        supportingText =
                        if (!isPhoneValid) {
                            { Text("Phone number must be at least 10 digits") }
                        } else null,
                        keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions =
                        KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = formData.password,
                        onValueChange = { formData = formData.copy(password = it) },
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
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions =
                        KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    // Password Strength Indicator
                    if (formData.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text =
                                when {
                                    passwordStrength < 0.5f -> "Weak"
                                    passwordStrength < 0.75f -> "Medium"
                                    else -> "Strong"
                                },
                                style = Typography.bodySmall,
                                color =
                                when {
                                    passwordStrength < 0.5f ->
                                        MaterialTheme.colorScheme.error

                                    passwordStrength < 0.75f -> Color(0xFFFF9800)
                                    else -> Color(0xFF4CAF50)
                                }
                            )
                            LinearProgressIndicator(
                                progress = passwordStrength,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp),
                                color =
                                when {
                                    passwordStrength < 0.5f ->
                                        MaterialTheme.colorScheme.error

                                    passwordStrength < 0.75f -> Color(0xFFFF9800)
                                    else -> Color(0xFF4CAF50)
                                },
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Field
                    OutlinedTextField(
                        value = formData.confirmPassword,
                        onValueChange = { formData = formData.copy(confirmPassword = it) },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint =
                                if (doPasswordsMatch)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }
                            ) {
                                Icon(
                                    imageVector =
                                    if (confirmPasswordVisible)
                                        Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription =
                                    if (confirmPasswordVisible) "Hide password"
                                    else "Show password"
                                )
                            }
                        },
                        visualTransformation =
                        if (confirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        isError = !doPasswordsMatch,
                        supportingText =
                        if (!doPasswordsMatch) {
                            { Text("Passwords do not match") }
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
                                if (isFormValid) {
                                    onRegisterClick(formData)
                                }
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Register Button
                    Button(
                        onClick = { onRegisterClick(formData) },
                        enabled = !isLoading && isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        if (isLoading) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Creating Account...",
                                    style = Typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Text(
                                text = "Create Account",
                                style = Typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Link
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onLoginClick) {
                            Text(
                                text = "Sign In",
                                style = Typography.bodyMedium,
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
