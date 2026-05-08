package com.example.rapidrescue.ui.screens.authentication.signup

import android.util.Patterns
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthState
import com.example.rapidrescue.ui.screens.authentication.authviewmodel.AuthViewModel
import com.example.rapidrescue.ui.theme.Charcoal
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.PurpleGrey80
import com.example.rapidrescue.ui.theme.SlateWhite
import com.example.rapidrescue.ui.theme.TrustBlue
import com.example.rapidrescue.ui.theme.grey
import com.example.rapidrescue.ui.theme.primaryColor

@Composable
fun SignUpScreen(
    onSignUpClick: (String, String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 6
    val passwordsMatch = password == confirmPassword
    val isFormValid = name.isNotBlank() && isEmailValid && isPasswordValid && passwordsMatch

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.security))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onSignUpClick(name, email, password)
            authViewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleGrey80)
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(140.dp)
    )
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp
        ),

        colors = CardDefaults.elevatedCardColors(
            containerColor = Charcoal
        ),
        shape = RoundedCornerShape(14.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create account",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Join RapidRescue today",
                fontSize = 14.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full name" , color = grey) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email" ,color = grey) },
                singleLine = true,
                isError = email.isNotBlank() && !isEmailValid,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.email),
                        contentDescription = "Email",
                        tint = primaryColor
                    )
                }
            )

            if (email.isNotBlank() && !isEmailValid) {
                Text(
                    text = "Invalid email format",
                    color = Color(0xFFB91C1C),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = grey) },
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
                            contentDescription = null,
                            tint = primaryColor
                        )
                    }
                }
            )

            if (password.isNotBlank() && !isPasswordValid) {
                Text(
                    text = "At least 6 characters required",
                    color = Color(0xFFB91C1C),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm password", color = grey) },
                singleLine = true,
                isError = confirmPassword.isNotBlank() && !passwordsMatch,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            imageVector = if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = primaryColor
                        )
                    }
                }
            )

            if (confirmPassword.isNotBlank() && !passwordsMatch) {
                Text(
                    text = "Passwords do not match",
                    color = Color(0xFFB91C1C),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = Color(0xFFB91C1C),
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.register(name, email, password) },
                enabled = isFormValid && authState !is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TrustBlue)
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Create account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account? ", color = Color(0xFF64748B), fontSize = 13.sp,fontWeight = FontWeight.Bold,)
                Text("Sign in", color = primaryColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}