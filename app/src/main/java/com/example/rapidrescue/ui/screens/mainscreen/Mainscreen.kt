package com.example.rapidrescue.ui.screens.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rapidrescue.ui.navigation.BottomNavItem
import com.example.rapidrescue.ui.navigation.ROUTES
import com.example.rapidrescue.ui.screens.About.AboutScreen
import com.example.rapidrescue.ui.screens.Help.HelpScreen
import com.example.rapidrescue.ui.screens.Privacy.PrivacyScreen
import com.example.rapidrescue.ui.screens.Settings.SettingsScreen
import com.example.rapidrescue.ui.screens.alerts.AlertsScreen
import com.example.rapidrescue.ui.screens.contacts.ContactsScreen
import com.example.rapidrescue.ui.screens.guardians.GuardiansScreen
import com.example.rapidrescue.ui.screens.home.HomeScreen
import com.example.rapidrescue.ui.screens.map.MapScreen
import com.example.rapidrescue.ui.screens.medical.MedicalScreen
import com.example.rapidrescue.ui.screens.profile.ProfileScreen
import com.example.rapidrescue.ui.screens.receiver.ReceiverScreen
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.PurpleGrey80
import com.example.rapidrescue.ui.theme.blue


@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Alerts,
        BottomNavItem.Contacts,
        BottomNavItem.Settings,
        BottomNavItem.Profile,


    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // screens where bottom nav should be hidden
    val hideBottomNav = currentDestination?.route in listOf(
        ROUTES.ReceiverWithArgs,
        ROUTES.Settings.name,
        ROUTES.Privacy.name,
        ROUTES.About.name,
        ROUTES.Map.name

    )

    Scaffold(
        bottomBar = {
            if (!hideBottomNav) {
                NavigationBar(
                    containerColor = DeepNavy,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = blue,
                                selectedTextColor = Color(0xFF1E5FA5),
                                unselectedIconColor = CardWhite,
                                unselectedTextColor = CardWhite,
                                indicatorColor = Color(0xFFE6F1FB)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTES.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTES.Home.name) {
                HomeScreen(
                    onSOSTrigger = { lat, lng ->
                        navController.navigate(ROUTES.receiverRoute(lat, lng))
                    },
                    onNavigateToAlerts = { navController.navigate(ROUTES.Alerts.name) },
                    onNavigateToContacts = { navController.navigate(ROUTES.Guardians.name) },
                    onNavigateToMaps = { navController.navigate(ROUTES.Map.name) },


                    )
            }

//            composable(ROUTES.Map.name) {
//                MapScreen()
//            }

//            composable(ROUTES.Guardians.name) {
//                GuardiansScreen(
//                    onBack = { navController.popBackStack() }
//                )
//            }

            composable(ROUTES.Alerts.name) {
                AlertsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ROUTES.Profile.name) {
                ProfileScreen(
                    onNavigateToMedical = { navController.navigate(ROUTES.Medical.name) },
                    onNavigateToSettings = { navController.navigate(ROUTES.Settings.name) },
                    onNavigateToPrivacy = { navController.navigate(ROUTES.Privacy.name) },
                    onNavigateToHelp = { navController.navigate(ROUTES.Help.name) },
                    onNavigateToAbout = { navController.navigate(ROUTES.About.name) },
                    onNavigateToMaps = { navController.navigate(ROUTES.Map.name) },
                    onLogout = onLogout
                )
            }
            composable(ROUTES.Map.name) {
                MapScreen()
            }
            composable(ROUTES.Settings.name) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(ROUTES.Privacy.name) {
                PrivacyScreen(onBack = { navController.popBackStack() })
            }

            composable(ROUTES.About.name) {
                AboutScreen(onBack = { navController.popBackStack() })
            }

            composable(ROUTES.Medical.name) {
                MedicalScreen(onBack = { navController.popBackStack() })
            }

            composable(ROUTES.Guardians.name) {
                GuardiansScreen(onBack = { navController.popBackStack() })
            }

            composable(ROUTES.Help.name) {
                HelpScreen(onBack = { navController.popBackStack() })
            }
            composable(ROUTES.Contacts.name) {
                ContactsScreen(onBack = { navController.popBackStack() })
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
        }
    }
}