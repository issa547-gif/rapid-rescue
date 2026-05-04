package com.example.rapidrescue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rapidrescue.ui.screens.alerts.AlertsScreen
import com.example.rapidrescue.ui.screens.authentication.forgotpassword.ForgotPasswordScreen
import com.example.rapidrescue.ui.screens.authentication.login.LoginScreen
import com.example.rapidrescue.ui.screens.authentication.register.RegisterScreen
import com.example.rapidrescue.ui.screens.authentication.signup.SignUpScreen
import com.example.rapidrescue.ui.screens.contacts.ContactsScreen
import com.example.rapidrescue.ui.screens.onboarding.OnboardingScreen
import com.example.rapidrescue.ui.screens.receiver.ReceiverScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier
){
    NavHost(
        navController = navController,
        startDestination = ROUTES.Register.name
    ){
        composable(ROUTES.Login.name) { LoginScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.ForgotPassword.name) { ForgotPasswordScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.Register.name) { RegisterScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.Home.name) {LoginScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.signup.name) { SignUpScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.onboarding.name) { OnboardingScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.Contacts.name) { ContactsScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.Alerts.name) { AlertsScreen( navController= navController,modifier = modifier) }
        composable(ROUTES.Receiver.name) {ReceiverScreen( navController= navController,modifier = modifier) }
    }
}



//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.rapidrescue.ui.screens.authentication.signup.SignUpScreen
//import com.example.rapidrescue.ui.screens.authentication.forgotpassword.ForgotPasswordScreen
//import com.example.rapidrescue.ui.screens.authentication.login.LoginScreen
//import com.example.rapidrescue.ui.screens.onboarding.OnboardingScreen
//import com.example.rapidrescue.ui.screens.home.HomeScreen
//import com.example.rapidrescue.ui.screens.contacts.ContactsScreen
//import com.example.rapidrescue.ui.screens.alerts.AlertsScreen
//import com.example.rapidrescue.ui.screens.receiver.ReceiverScreen
//
//@Composable
//fun Navigation() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = "onboarding"
//    ) {
//
//        composable("onboarding") {
//            OnboardingScreen(
//                onGetStartedClick = {
//                    navController.navigate("login")
//                }
//            )
//        }
//
//        composable("login") {
//            LoginScreen(
//                onLoginClick = { email, password ->
//                    navController.navigate("home") {
//                        popUpTo("onboarding") { inclusive = true } // can't go back to login after auth
//                    }
//                },
//                onNavigateToSignUp = {
//                    navController.navigate("signup")
//                },
//                onNavigateToForgot = {
//                    navController.navigate("forgot")
//                }
//            )
//        }
//
//        composable("signup") {
//            SignUpScreen(
//                onSignUpClick = { _, _, _ ->
//                    navController.navigate("login") {
//                        popUpTo("signup") { inclusive = true }
//                    }
//                },
//                onNavigateToLogin = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable("forgot") {
//            ForgotPasswordScreen(
//                onResetClick = {},
//                onBackToLogin = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable("home") {
//            HomeScreen(
//                onSOSTrigger = { lat, lng ->
//                    navController.navigate("receiver/$lat/$lng")
//                },
//                onNavigateToContacts = {
//                    navController.navigate("contacts")
//                },
//                onNavigateToAlerts = {
//                    navController.navigate("alerts")
//                }
//            )
//        }
//
//        composable("contacts") {
//            ContactsScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable("alerts") {
//            AlertsScreen(
//                onBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        // lat and lng passed as strings to avoid Double serialization issues in nav args
//        composable(
//            route = "receiver/{lat}/{lng}",
//            arguments = listOf(
//                navArgument("lat") { type = NavType.StringType; defaultValue = "0.0" },
//                navArgument("lng") { type = NavType.StringType; defaultValue = "0.0" }
//            )
//        ) { backStackEntry ->
//            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
//            val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull()
//            ReceiverScreen(
//                lat = lat,
//                lng = lng,
//                onBack = {
//                    navController.navigate("home") {
//                        popUpTo("home") { inclusive = true } // receiver shouldn't stack on home
//                    }
//                }
//            )
//        }
////    }
//}
