package com.example.rapidrescue.ui.screens.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var sosNotifications by remember { mutableStateOf(true) }
    var guardianAlerts by remember { mutableStateOf(true) }
    var checkInReminders by remember { mutableStateOf(false) }
    var locationSharing by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedTheme by remember { mutableStateOf("System default") }

    val languages = listOf("English", "Swahili", "French", "Arabic")
    val themes = listOf("System default", "Light", "Dark")
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF4F6F9)
                )
            )
        },
        containerColor = Color(0xFFF4F6F9)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Notifications
            SettingsSection(title = "Notifications") {
                SettingsToggle(
                    icon = Icons.Default.Notifications,
                    label = "SOS alerts",
                    subtitle = "Get notified when an SOS is triggered",
                    tint = Color(0xFFB91C1C),
                    checked = sosNotifications,
                    onCheckedChange = { sosNotifications = it }
                )
                SettingsDivider()
                SettingsToggle(
                    icon = Icons.Default.Person,
                    label = "Guardian alerts",
                    subtitle = "Notify guardians during emergencies",
                    tint = Color(0xFF1E5FA5),
                    checked = guardianAlerts,
                    onCheckedChange = { guardianAlerts = it }
                )
                SettingsDivider()
                SettingsToggle(
                    icon = Icons.Default.DateRange,
                    label = "Check-in reminders",
                    subtitle = "Remind me to check in on schedule",
                    tint = Color(0xFF0D9488),
                    checked = checkInReminders,
                    onCheckedChange = { checkInReminders = it }
                )
            }

            // Location
            SettingsSection(title = "Location") {
                SettingsToggle(
                    icon = Icons.Default.LocationOn,
                    label = "Background location",
                    subtitle = "Share location with guardians during SOS",
                    tint = Color(0xFF1E5FA5),
                    checked = locationSharing,
                    onCheckedChange = { locationSharing = it }
                )
            }

            // Appearance
            SettingsSection(title = "Appearance") {
                SettingsChoice(
                    icon = Icons.Default.Star,
                    label = "Theme",
                    value = selectedTheme,
                    tint = Color(0xFF64748B),
                    onClick = { showThemeDialog = true }
                )
                SettingsDivider()
                SettingsChoice(
                    icon = Icons.Default.List,
                    label = "Language",
                    value = selectedLanguage,
                    tint = Color(0xFF64748B),
                    onClick = { showLanguageDialog = true }
                )
            }

            // Permissions
            SettingsSection(title = "App permissions") {
                SettingsAction(
                    icon = Icons.Default.LocationOn,
                    label = "Location access",
                    subtitle = "Required for SOS and map features",
                    tint = Color(0xFF1E5FA5),
                    actionLabel = "Granted",
                    actionColor = Color(0xFF22C55E)
                )
                SettingsDivider()
                SettingsAction(
                    icon = Icons.Default.Notifications,
                    label = "Notifications",
                    subtitle = "Required for alert delivery",
                    tint = Color(0xFFB91C1C),
                    actionLabel = "Granted",
                    actionColor = Color(0xFF22C55E)
                )
                SettingsDivider()
                SettingsAction(
                    icon = Icons.Default.Phone,
                    label = "Phone / SMS",
                    subtitle = "For sending emergency SMS",
                    tint = Color(0xFF0D9488),
                    actionLabel = "Not set",
                    actionColor = Color(0xFFB91C1C)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Select language") },
            text = {
                Column {
                    languages.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLanguage = lang
                                    showLanguageDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(lang, fontSize = 14.sp)
                            if (lang == selectedLanguage) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF1E5FA5),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (lang != languages.last()) {
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Select theme") },
            text = {
                Column {
                    themes.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedTheme = theme
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(theme, fontSize = 14.sp)
                            if (theme == selectedTheme) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF1E5FA5),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (theme != themes.last()) {
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
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
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 64.dp),
        thickness = 0.5.dp,
        color = Color(0xFFE2E6ED)
    )
}

@Composable
private fun SettingsToggle(
    icon: ImageVector,
    label: String,
    subtitle: String,
    tint: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
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
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A2233))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1E5FA5)
            )
        )
    }
}

@Composable
private fun SettingsChoice(
    icon: ImageVector,
    label: String,
    value: String,
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
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A2233), modifier = Modifier.weight(1f))
        Text(value, fontSize = 13.sp, color = Color(0xFF94A3B8))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SettingsAction(
    icon: ImageVector,
    label: String,
    subtitle: String,
    tint: Color,
    actionLabel: String,
    actionColor: Color
) {
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
                .background(tint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A2233))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
        Text(
            text = actionLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = actionColor
        )
    }
}