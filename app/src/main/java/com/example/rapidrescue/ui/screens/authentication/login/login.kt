package com.example.rapidrescue.ui.screens.authentication.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthState
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthViewModel
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.Charcoal
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.PurpleGrey80
import com.example.rapidrescue.ui.theme.SlateWhite
import com.example.rapidrescue.ui.theme.TrustBlue
import com.example.rapidrescue.ui.theme.black
import com.example.rapidrescue.ui.theme.grey
import com.example.rapidrescue.ui.theme.navyBlue
import com.example.rapidrescue.ui.theme.primaryColor

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onNavigateToForgot: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.security))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginClick(email, password)
            authViewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(navyBlue),

        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp) // controls total card height
                .padding(horizontal = 16.dp),

            shape = RoundedCornerShape(24.dp),

            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 6.dp
            ),

            colors = CardDefaults.elevatedCardColors(
                containerColor = Charcoal
            )
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = "Welcome back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = CardWhite,
                    textAlign = TextAlign.Center,


                )
                Text(
                    text = "Sign in to your account",
                    fontSize = 14.sp,
                    color = Color(0xFF5F6C7E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = grey) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape =  RoundedCornerShape(16.dp) ,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.email),
                            contentDescription = "Email",
                            tint = primaryColor
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = grey) },
                    shape =  RoundedCornerShape(16.dp) ,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.logo),
                            contentDescription = "Password",
                            tint = primaryColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = primaryColor
                            )
                        }
                    }
                )

                TextButton(
                    onClick = onNavigateToForgot,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot password?", color = primaryColor, fontSize = 13.sp)
                }

                if (authState is AuthState.Error) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color(0xFFB91C1C),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { authViewModel.login(email, password) },
                    enabled = authState !is AuthState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TrustBlue
                    , disabledContainerColor = Color(0xFF1B3A5C))
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Sign in",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onNavigateToSignUp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Don't have an account? ",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "Sign up",
                        color = primaryColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}