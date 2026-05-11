package com.example.rapidrescue.ui.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rapidrescue.data.locationhelper.getCurrentLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class MapState {
    object Idle : MapState()
    object Loading : MapState()
    data class Success(val lat: Double, val lng: Double) : MapState()
    data class Error(val message: String) : MapState()
}

class MapViewModel : ViewModel() {
    private val _mapState = MutableStateFlow<MapState>(MapState.Idle)
    val mapState = _mapState.asStateFlow()

    fun fetchLocation(context: android.content.Context) {
        viewModelScope.launch {
            _mapState.value = MapState.Loading
            try {
                val (lat, lng) = getCurrentLocation(context)
                _mapState.value = MapState.Success(lat, lng)
            } catch (e: Exception) {
                _mapState.value = MapState.Error(e.message ?: "Failed to get location")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {
    val context = LocalContext.current
    val mapState by viewModel.mapState.collectAsState()
    val time = remember { SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(Date()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) viewModel.fetchLocation(context)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency map") },
                actions = {
                    IconButton(onClick = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color(0xFF1E5FA5)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A1628),
                    titleContentColor = Color(0xFFE8ECF0)
                )
            )
        },
        containerColor = Color(0xFF0A1628)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (mapState) {
                is MapState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = Color(0xFF1E5FA5))
                            Text(
                                text = "Getting your location...",
                                color = Color(0xFF94A3B8),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                is MapState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFFB91C1C),
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = (mapState as MapState.Error).message,
                                color = Color(0xFFFF5252),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            OutlinedButton(
                                onClick = { viewModel.fetchLocation(context) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF1E5FA5)
                                )
                            ) {
                                Text("Try again")
                            }
                        }
                    }
                }

                is MapState.Success -> {
                    val state = mapState as MapState.Success
                    val mapsLink = "https://maps.google.com/?q=${state.lat},${state.lng}"

                    // Location card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E35)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1E5FA5).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF1E5FA5),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Current location",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFFE8ECF0)
                                    )
                                    Text(
                                        text = "Updated at $time",
                                        fontSize = 12.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }

                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color(0xFF1B3A5C)
                            )

                            // Coordinates
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CoordinateItem(
                                    label = "Latitude",
                                    value = "%.6f".format(state.lat)
                                )
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    thickness = 0.5.dp,
                                    color = Color(0xFF1B3A5C)
                                )
                                CoordinateItem(
                                    label = "Longitude",
                                    value = "%.6f".format(state.lng)
                                )
                            }
                        }
                    }

                    // Open in maps button
                    Button(
                        onClick = {
                            val intent = android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                android.net.Uri.parse(mapsLink)
                            )
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E5FA5)
                        )
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open in Google Maps",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }

                    // Nearby hospitals info card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E35)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Find nearby",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFE8ECF0)
                            )

                            NearbyButton(
                                label = "Hospitals",
                                emoji = "🏥",
                                query = "hospitals near me",
                                lat = state.lat,
                                lng = state.lng,
                                context = context
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF1B3A5C))
                            NearbyButton(
                                label = "Police stations",
                                emoji = "🚔",
                                query = "police stations near me",
                                lat = state.lat,
                                lng = state.lng,
                                context = context
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF1B3A5C))
                            NearbyButton(
                                label = "Pharmacies",
                                emoji = "💊",
                                query = "pharmacies near me",
                                lat = state.lat,
                                lng = state.lng,
                                context = context
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF1B3A5C))
                            NearbyButton(
                                label = "Fire stations",
                                emoji = "🚒",
                                query = "fire stations near me",
                                lat = state.lat,
                                lng = state.lng,
                                context = context
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun CoordinateItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFE8ECF0)
        )
    }
}

@Composable
private fun NearbyButton(
    label: String,
    emoji: String,
    query: String,
    lat: Double,
    lng: Double,
    context: android.content.Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 20.sp)
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFFE8ECF0),
                fontWeight = FontWeight.Medium
            )
        }
        TextButton(
            onClick = {
                val uri = android.net.Uri.parse(
                    "https://www.google.com/maps/search/$query/@$lat,$lng,14z"
                )
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW, uri
                )
                context.startActivity(intent)
            }
        ) {
            Text(
                text = "Search",
                color = Color(0xFF1E5FA5),
                fontSize = 13.sp
            )
        }
    }
}