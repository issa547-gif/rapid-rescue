package com.example.rapidrescue.ui.screens.authentication.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthState
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthViewModel
import com.example.rapidrescue.ui.theme.Charcoal
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.TrustBlue
import com.example.rapidrescue.ui.theme.grey

@Composable
fun ForgotPasswordScreen(
    onResetClick: (String) -> Unit,
    onBackToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.security)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            snackbarHostState.showSnackbar("Reset link sent — check your email")
            authViewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DeepNavy
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepNavy)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Charcoal
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Lottie animation
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(160.dp)
                    )

                    // Title
                    Text(
                        text = "Forgot password?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE8ECF0),
                        textAlign = TextAlign.Center
                    )

                    // Subtitle
                    Text(
                        text = "Enter your email address and we'll send you a link to reset your password.",
                        fontSize = 14.sp,
                        color = Color(0xFF94A3B8),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email address" , color = grey) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor = Color(0xFF1E5FA5),
//                            unfocusedBorderColor = Color(0xFF1B3A5C),
//                            focusedLabelColor = Color(0xFF1E5FA5),
//                            unfocusedLabelColor = Color(0xFF94A3B8),
//                            cursorColor = Color(0xFF1E5FA5),
//                            focusedTextColor = Color(0xFFE8ECF0),
//                            unfocusedTextColor = Color(0xFFE8ECF0)
//                        )
                    )

                    // Error message
                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = Color(0xFFFF5252),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Send reset link button
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                authViewModel.resetPassword(email)
                                onResetClick(email)
                            }
                        },
                        enabled = email.isNotBlank() && authState !is AuthState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrustBlue,
                            disabledContainerColor = Color(0xFF1B3A5C)
                        )
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Send reset link",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Back to login
                    TextButton(
                        onClick = onBackToLogin,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "← Back to login",
                            color = TrustBlue,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}