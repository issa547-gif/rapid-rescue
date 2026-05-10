package com.example.rapidrescue.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.Charcoal
import com.example.rapidrescue.ui.theme.DeepNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToMedical: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val state by viewModel.state.collectAsState()

    val displayName = profile?.fullName?.ifBlank { "User" } ?: "User"
    val initials = displayName.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .take(2)
        .joinToString("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", color = CardWhite) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy
                )
            )
        },
        containerColor = DeepNavy
    ) { padding ->
        if (state is ProfileState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1E5FA5))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar + info card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E5FA5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials.ifBlank { "?" },
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1A2233)
                                )
                                Text(
                                    text = viewModel.userEmail,
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF22C55E))
                                    )
                                    Text(
                                        text = "Active",
                                        fontSize = 12.sp,
                                        color = Color(0xFF22C55E)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE2E6ED))

                        // Edit and delete account row
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextButton(
                                onClick = onNavigateToMedical,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF1E5FA5)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit profile", color = Color(0xFF1E5FA5), fontSize = 13.sp)
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .height(40.dp)
                                    .padding(vertical = 8.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE2E6ED)
                            )
                            TextButton(
                                onClick = { /* navigate to privacy → delete account */ onNavigateToPrivacy() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFB91C1C)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", color = Color(0xFFB91C1C), fontSize = 13.sp)
                            }
                        }
                    }
                }
                // Medical summary card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Charcoal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFB91C1C),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Medical summary",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB91C1C)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MedicalStat(
                                label = "Blood type",
                                value = profile?.bloodType?.ifBlank { "—" } ?: "—"
                            )
                            MedicalStat(
                                label = "Allergies",
                                value = if (profile?.allergies.isNullOrBlank()) "None" else "Yes"
                            )
                            MedicalStat(
                                label = "Medications",
                                value = if (profile?.medications.isNullOrBlank()) "None" else "Yes"
                            )
                        }
                    }
                }

                // Account section
                ProfileSection(title = "Account") {
                    ProfileMenuItem(
                        icon = Icons.Default.Favorite,
                        label = "Medical information",
                        subtitle = "Blood type, allergies, medications",
                        tint = Color(0xFFB91C1C),
                        onClick = onNavigateToMedical
                    )
                    ProfileMenuDivider()
                    ProfileMenuItem(
                        icon = Icons.Default.Lock,
                        label = "Privacy & security",
                        subtitle = "Password, data sharing",
                        tint = Color(0xFF1E5FA5),
                        onClick = onNavigateToPrivacy
                    )
                }

                // App section
                ProfileSection(title = "App") {
                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        subtitle = "Notifications, theme, language",
                        tint = Color(0xFF64748B),
                        onClick = onNavigateToSettings
                    )
                    ProfileMenuDivider()
                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        label = "Help & support",
                        subtitle = "FAQs, contact us",
                        tint = Color(0xFF64748B),
                        onClick = onNavigateToHelp
                    )
                    ProfileMenuDivider()
                    ProfileMenuItem(
                        icon = Icons.Default.Star,
                        label = "About RapidRescue",
                        subtitle = "Version 1.0.0",
                        tint = Color(0xFF64748B),
                        onClick = onNavigateToAbout
                    )
                }

                // Logout
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLogout() }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color(0xFFB91C1C),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Sign out",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFB91C1C)
                        )
                    }
                }

                if (state is ProfileState.Error) {
                    Text(
                        text = (state as ProfileState.Error).message,
                        color = Color(0xFFB91C1C),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MedicalStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB91C1C)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF94A3B8),
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column { content() }
        }
    }
}

@Composable
private fun ProfileMenuDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 64.dp),
        thickness = 0.5.dp,
        color = Color(0xFFE2E6ED)
    )
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(tint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A2233)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(18.dp)
        )
    }
}