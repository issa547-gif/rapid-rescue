package com.example.rapidrescue.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(

    val route: String,
    val label: String,
    val icon: ImageVector,

) {
    object Home : BottomNavItem(
        route = ROUTES.Home.name,
        label = "Home",
        icon = Icons.Default.Home
    )
    object Map : BottomNavItem(
        route = ROUTES.Map.name,
        label = "Map",
        icon = Icons.Default.LocationOn
    )
    object Guardians : BottomNavItem(
        route = ROUTES.Guardians.name,
        label = "Guardians",
        icon = Icons.Default.People
    )
    object Alerts : BottomNavItem(
        route = ROUTES.Alerts.name,
        label = "Alerts",
        icon = Icons.Default.Notifications
    )
    object Profile : BottomNavItem(
        route = ROUTES.Profile.name,
        label = "Profile",
        icon = Icons.Default.Person
    )
    object Settings : BottomNavItem(
        route = ROUTES.Settings.name,
        label = "Profile",
        icon = Icons.Default.Settings
    )
    object Contacts : BottomNavItem(
        route = ROUTES.Contacts.name,
        label = "Contacts",
        icon = Icons.Default.Call
    )

}
