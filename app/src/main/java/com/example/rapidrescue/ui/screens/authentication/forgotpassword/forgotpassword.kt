package com.example.rapidrescue.ui.screens.authentication.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.theme.black
import com.example.rapidrescue.ui.theme.maroon
import com.example.rapidrescue.ui.theme.white

@Composable
fun ForgotPasswordScreen(
    onResetClick: (String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.security)
    )
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(maroon)
    ) {

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(500.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineMedium,
            color = black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter your email to reset your password",
            style = MaterialTheme.typography.bodyMedium,
            color = white
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                if (email.isNotBlank()) {
                    onResetClick(email)
                    message = "Reset link sent (check your email)"
                } else {
                    message = "Please enter your email"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Reset Link")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBackToLogin,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF1E88E5) // text color
                ),
            shape = RoundedCornerShape(12.dp),
            )
        {
            Text("Back to Login")
        }
        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message)
        }
    }
}