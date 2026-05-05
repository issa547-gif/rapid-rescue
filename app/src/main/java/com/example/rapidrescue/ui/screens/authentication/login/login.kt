package com.example.rapidrescue.ui.screens.authentication.login

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.theme.black
import com.example.rapidrescue.ui.theme.primaryColor
import androidx.compose.ui.graphics.Color
import com.example.rapidrescue.ui.theme.secondaryColor




@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onNavigateToForgot: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.security)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(500.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = black
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.email),
                    contentDescription = "Email",
                    tint = primaryColor
                )
            },

        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.logo),
                    contentDescription = "password",
                    tint = primaryColor
                )
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.fillMaxWidth(),

        ) {
            Text("Login")
        }
    }
}