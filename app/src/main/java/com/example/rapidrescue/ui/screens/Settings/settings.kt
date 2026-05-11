package com.example.rapidrescue.ui.screens.Settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    var sosNotifications by remember { mutableStateOf(true) }
    var guardianAlerts by remember { mutableStateOf(true) }
    var checkInReminders by remember { mutableStateOf(false) }
    var locationSharing by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedTheme by remember { mutableStateOf("System default") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    // check real permission states
    var locationGranted by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    var smsGranted by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    var notificationsGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                androidx.core.content.ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            } else {
                NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        )
    }

    // permission launchers
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    val smsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        smsGranted = granted
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationsGranted = granted
    }

    val languages = listOf("English", "Swahili", "French", "Arabic")
    val themes = listOf("System default", "Light", "Dark")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        color = CardWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CardWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy
                )
            )
        },
        containerColor = DeepNavy
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
                    onCheckedChange = {
                        sosNotifications = it
                        if (it && !notificationsGranted) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                // open notification settings for older Android
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                                context.startActivity(intent)
                            }
                        }
                    }
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
                    checked = locationSharing && locationGranted,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (!locationGranted) {
                                locationLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            } else {
                                locationSharing = true
                            }
                        } else {
                            locationSharing = false
                        }
                    }
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

            // Permissions — now tappable to request/open settings
            SettingsSection(title = "App permissions") {
                SettingsActionClickable(
                    icon = Icons.Default.LocationOn,
                    label = "Location access",
                    subtitle = "Required for SOS and map features",
                    tint = Color(0xFF1E5FA5),
                    actionLabel = if (locationGranted) "Granted" else "Grant",
                    actionColor = if (locationGranted) Color(0xFF22C55E) else Color(0xFFB91C1C),
                    onClick = {
                        if (!locationGranted) {
                            locationLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        } else {
                            // open app settings to revoke if already granted
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    }
                )
                SettingsDivider()
                SettingsActionClickable(
                    icon = Icons.Default.Notifications,
                    label = "Notifications",
                    subtitle = "Required for alert delivery",
                    tint = Color(0xFFB91C1C),
                    actionLabel = if (notificationsGranted) "Granted" else "Grant",
                    actionColor = if (notificationsGranted) Color(0xFF22C55E) else Color(0xFFB91C1C),
                    onClick = {
                        if (!notificationsGranted) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                context.startActivity(
                                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    }
                                )
                            }
                        } else {
                            context.startActivity(
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                            )
                        }
                    }
                )
                SettingsDivider()
                SettingsActionClickable(
                    icon = Icons.Default.Phone,
                    label = "Phone / SMS",
                    subtitle = "For sending emergency SMS",
                    tint = Color(0xFF0D9488),
                    actionLabel = if (smsGranted) "Granted" else "Grant",
                    actionColor = if (smsGranted) Color(0xFF22C55E) else Color(0xFFB91C1C),
                    onClick = {
                        if (!smsGranted) {
                            smsLauncher.launch(Manifest.permission.SEND_SMS)
                        } else {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    // Language dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            containerColor = Color(0xFF0F1E35),
            title = { Text("Select language", color = Color(0xFFE8ECF0)) },
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
                            Text(lang, fontSize = 14.sp, color = Color(0xFFE8ECF0))
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
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color(0xFF1B3A5C)
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Theme dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            containerColor = Color(0xFF0F1E35),
            title = { Text("Select theme", color = Color(0xFFE8ECF0)) },
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
                            Text(theme, fontSize = 14.sp, color = Color(0xFFE8ECF0))
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
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color(0xFF1B3A5C)
                            )
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
            colors = CardDefaults.cardColors(containerColor = grey),
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
        color = Color(0xFF1B3A5C)
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
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE8ECF0))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1E5FA5),
                uncheckedThumbColor = Color(0xFF94A3B8),
                uncheckedTrackColor = Color(0xFF1B3A5C)
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
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE8ECF0),
            modifier = Modifier.weight(1f)
        )
        Text(value, fontSize = 13.sp, color = Color(0xFF94A3B8))
        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SettingsActionClickable(
    icon: ImageVector,
    label: String,
    subtitle: String,
    tint: Color,
    actionLabel: String,
    actionColor: Color,
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
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE8ECF0))
            Text(subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = actionColor.copy(alpha = 0.15f)
        ) {
            Text(
                text = actionLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = actionColor,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}