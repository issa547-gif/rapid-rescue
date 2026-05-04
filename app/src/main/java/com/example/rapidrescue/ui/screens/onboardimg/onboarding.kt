package com.example.rapidrescue.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.example.rapidrescue.R

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit,
    navController: NavHostController
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.security)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Welcome to Rapid Rescue", style = MaterialTheme.typography.headlineMedium)

        Button(onClick = onGetStartedClick) {
            Text("Get Started")
        }
    }
}