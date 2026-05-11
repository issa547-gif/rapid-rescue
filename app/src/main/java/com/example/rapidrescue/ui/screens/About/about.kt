package com.example.rapidrescue.ui.screens.About

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.Charcoal
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About RapidRescue",color = CardWhite,  fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy
                )
            )
        },
        containerColor = DeepNavy
    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // App identity card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = grey),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFB91C1C)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RR",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = "RapidRescue",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A2233)
                    )
                    Text(
                        text = "Version 1.0.0 (Build 1)",
                        fontSize = 13.sp,
                        color = DeepNavy
                    )
                    Text(
                        text = "One tap sends your location to your emergency contacts when you're in danger.",
                        fontSize = 13.sp,
                        color = DeepNavy,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            // Tech stack credits
            Column {
                Text(
                    text = "Built with",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = DeepNavy,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = grey),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        CreditRow(label = "Language", value = "Kotlin")
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        CreditRow(label = "UI framework", value = "Jetpack Compose")
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        CreditRow(label = "Backend", value = "Supabase")
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        CreditRow(label = "Architecture", value = "MVVM")
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        CreditRow(label = "Navigation", value = "Navigation Compose")
                        HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        CreditRow(label = "Location", value = "Google Play Services")
                    }
                }
            }

            // Links
            Column {
                Text(
                    text = "Links",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = CardWhite,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = grey),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        AboutLinkRow(icon = Icons.Default.Lock, label = "Privacy policy")
                        HorizontalDivider(modifier = Modifier.padding(start = 64.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        AboutLinkRow(icon = Icons.Default.Info, label = "Terms of service")
                        HorizontalDivider(modifier = Modifier.padding(start = 64.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        AboutLinkRow(icon = Icons.Default.Star, label = "Rate on Play Store")
                        HorizontalDivider(modifier = Modifier.padding(start = 64.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        AboutLinkRow(icon = Icons.Default.Share, label = "Share RapidRescue")
                    }
                }
            }

            // Developer credit
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E5FA5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("IS", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(
                            text = "Designed & built by Issa",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF185FA5)
                        )
                        Text(
                            text = "Full-stack Android developer",
                            fontSize = 12.sp,
                            color = Color(0xFF378ADD),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "© 2026 RapidRescue. All rights reserved.",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CreditRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF64748B))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A2233))
    }
}

@Composable
private fun AboutLinkRow(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF64748B).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
        }
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A2233), modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
    }
}