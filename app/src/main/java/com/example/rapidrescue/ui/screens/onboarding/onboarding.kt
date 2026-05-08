package com.example.rapidrescue.ui.screens.onboarding

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
import com.airbnb.lottie.compose.*
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.TextSecondaryDark
import com.example.rapidrescue.ui.theme.TrustBlue
import com.example.rapidrescue.ui.theme.maroon

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.security)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {

    }

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

        Text(
            text = "Welcome to\nRapid Rescue",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 32.sp
            ),
            textAlign = TextAlign.Center,
            color = (TextSecondaryDark),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = (TrustBlue),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = onGetStartedClick
        ) {
            Text("GET STARTED")
        }
    }
}