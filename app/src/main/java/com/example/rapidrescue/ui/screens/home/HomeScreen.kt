package com.example.rapidrescue.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onSOSTrigger: (Double, Double) -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToAlerts: () -> Unit
) {
    // placeholder lat/lng for now — replace with real GPS later
    val placeholderLat = -1.2921
    val placeholderLng = 36.8219

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text("RapidRescue", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = { onSOSTrigger(placeholderLat, placeholderLng) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.size(180.dp)
            ) {
                Text("SOS", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(onClick = onNavigateToContacts) { Text("Contacts") }
                OutlinedButton(onClick = onNavigateToAlerts) { Text("History") }
            }
        }
    }
}






//
//
//fun HomeScreen(
//    onNavigateToAlerts: () -> Unit,
//    onNavigateToContacts: () -> Unit,
//    onSOSTrigger: Function<Unit>
//) {
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text(text="WELCOME TO RAPID RESCUE ")
//    }
//}




