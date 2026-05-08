package com.example.rapidrescue.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rapidrescue.ui.screens.alerts.AlertsScreen
import com.example.rapidrescue.ui.screens.authentication.forgotpassword.ForgotPasswordScreen
import com.example.rapidrescue.ui.screens.authentication.login.LoginScreen
import com.example.rapidrescue.ui.screens.authentication.signup.SignUpScreen
import com.example.rapidrescue.ui.screens.contacts.ContactsScreen
import com.example.rapidrescue.ui.screens.home.HomeScreen
import com.example.rapidrescue.ui.screens.onboarding.OnboardingScreen
import com.example.rapidrescue.ui.screens.About.AboutScreen
import com.example.rapidrescue.ui.screens.Privacy.PrivacyScreen
import com.example.rapidrescue.ui.screens.profile.ProfileScreen
import com.example.rapidrescue.ui.screens.Settings.SettingsScreen
import com.example.rapidrescue.ui.screens.mainscreen.MainScreen
import com.example.rapidrescue.ui.screens.receiver.ReceiverScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTES.onboarding.name
    ) {
        composable(ROUTES.onboarding.name) {
            OnboardingScreen(
                onGetStartedClick = { navController.navigate(ROUTES.Login.name) }
            )
        }

        composable(ROUTES.Login.name) {
            LoginScreen(
                onLoginClick = { _, _ ->
                    navController.navigate("main") {
                        popUpTo(ROUTES.onboarding.name) { inclusive = true }
                    }
                },
                onNavigateToForgot = { navController.navigate(ROUTES.ForgotPassword.name) },
                onNavigateToSignUp = { navController.navigate(ROUTES.signup.name) }
            )
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    navController.navigate(ROUTES.Login.name) {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        composable(ROUTES.signup.name) {
            SignUpScreen(
                onSignUpClick = { _, _, _ ->
                    navController.navigate(ROUTES.Login.name) {
                        popUpTo(ROUTES.signup.name) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(ROUTES.ForgotPassword.name) {
            ForgotPasswordScreen(
                onResetClick = {},
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(ROUTES.Home.name) {
            HomeScreen(
                onSOSTrigger = { lat, lng ->
                    navController.navigate(ROUTES.receiverRoute(lat, lng))
                },
                onNavigateToContacts = { navController.navigate(ROUTES.Contacts.name) },
                onNavigateToAlerts = { navController.navigate(ROUTES.Alerts.name) }
            )
        }

        composable(ROUTES.Contacts.name) {
            ContactsScreen(onBack = { navController.popBackStack() })
        }

        composable(ROUTES.Alerts.name) {
            AlertsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = ROUTES.ReceiverWithArgs,
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType; defaultValue = "0.0" },
                navArgument("lng") { type = NavType.StringType; defaultValue = "0.0" }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lng = backStackEntry.arguments?.getString("lng")?.toDoubleOrNull()
            ReceiverScreen(
                lat = lat,
                lng = lng,
                onBack = {
                    navController.navigate(ROUTES.Home.name) {
                        popUpTo(ROUTES.Home.name) { inclusive = true }
                    }
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateToMedical = { navController.navigate("medical") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToPrivacy = { navController.navigate("privacy") },
                onNavigateToHelp = { navController.navigate("help") },
                onNavigateToAbout = { navController.navigate("about") },
                onLogout = {
                    navController.navigate(ROUTES.Login.name) {
                        popUpTo(ROUTES.Home.name) { inclusive = true }
                    }
                }
            )
        }
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable("privacy") {
            PrivacyScreen(onBack = { navController.popBackStack() })
        }

        composable("about") {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable("contacts")  {
            ContactsScreen(onBack = { navController.popBackStack() })
        }



    }
}









//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.rapidrescue.ui.screens.alerts.AlertsScreen
//import com.example.rapidrescue.ui.screens.authentication.forgotpassword.ForgotPasswordScreen
//import com.example.rapidrescue.ui.screens.authentication.login.LoginScreen
//import com.example.rapidrescue.ui.screens.authentication.register.RegisterScreen
//import com.example.rapidrescue.ui.screens.authentication.signup.SignUpScreen
//import com.example.rapidrescue.ui.screens.contacts.ContactsScreen
//import com.example.rapidrescue.ui.screens.onboarding.OnboardingScreen
//import com.example.rapidrescue.ui.screens.receiver.ReceiverScreen

//@Composable
//fun AppNavigation(
//    navController: NavHostController,
//    modifier: Modifier
//){
//    NavHost(
//        navController = navController,
//        startDestination = ROUTES.Register.name
//    ){
//        composable(ROUTES.Login.name) { LoginScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.ForgotPassword.name) { ForgotPasswordScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.Register.name) { RegisterScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.Home.name) {LoginScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.signup.name) { SignUpScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.onboarding.name) { OnboardingScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.Contacts.name) { ContactsScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.Alerts.name) { AlertsScreen( navController= navController,modifier = modifier) }
//        composable(ROUTES.Receiver.name) {ReceiverScreen( navController= navController,modifier = modifier) }
//    }
//}


