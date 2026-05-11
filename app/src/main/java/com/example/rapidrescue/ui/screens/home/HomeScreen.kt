package com.example.rapidrescue.ui.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.PurpleGrey80

@Composable
fun HomeScreen(
    onSOSTrigger: (Double, Double) -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToMaps: () -> Unit,

    alertViewModel: AlertViewModel = viewModel()
) {
    val context = LocalContext.current
    val sosState by alertViewModel.sosState.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(100),
        label = "sos-scale"
    )

    LaunchedEffect(sosState) {
        if (sosState is SOSState.Success) {
            val state = sosState as SOSState.Success
            onSOSTrigger(state.lat, state.lng)
            alertViewModel.resetState()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val smsGranted = permissions[Manifest.permission.SEND_SMS] == true

        if (locationGranted && smsGranted) {
            alertViewModel.triggerSOS(context)
        } else if (!locationGranted) {
            // show location denied message
        } else if (!smsGranted) {
            // show sms denied message
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy),
        contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "RapidRescue",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = CardWhite,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "One tap sends your location to all guardians",
                    fontSize = 13.sp,
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center
                )
            }

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB91C1C).copy(alpha = 0.15f))
                )
                Box(
                    modifier = Modifier
                        .size(225.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB91C1C).copy(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .scale(scale)
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB91C1C))
                        .border(3.dp, Color(0xFFFF5252), CircleShape)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = sosState !is SOSState.Loading
                        ) {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.SEND_SMS
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (sosState is SOSState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "CRITICAL",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 4.sp
                            )
                            Text(
                                text = "ALERT",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 4.sp
                            )
                            Text(
                                text = "TAP TO SEND",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 8.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

//            if (sosState is SOSState.Error) {
//                Text(
//                    text = (sosState as SOSState.Error).message,
//                    color = Color(0xFFFF5252),
//                    fontSize = 13.sp,
//                    textAlign = TextAlign.Center
//                )
//            } else {
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(6.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(8.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF4CAF50))
//                    )
//                    Text(
//                        text = "Ready to send alert",
//                        fontSize = 12.sp,
//                        color = Color(0xFF4CAF50)
//                    )
//                }
//            }
            if (sosState is SOSState.Error) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0A0A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = (sosState as SOSState.Error).message,
                        color = Color(0xFFFF5252),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else if (sosState is SOSState.Success) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1A0A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "✓ Alert sent to all contacts",
                        color = Color(0xFF22C55E),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HomeActionButton(
                    label = "Guardians",
                    icon =  Icons.Default.PeopleAlt,
                    onClick = onNavigateToContacts,
                    modifier = Modifier.weight(1f)
                )
                HomeActionButton(
                    label = "History",
                    icon = Icons.Default.List,
                    onClick = onNavigateToAlerts,
                    modifier = Modifier.weight(1f)
                )


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center



            ) {
                HomeActionButton(
                    label = "Maps",
                    icon = Icons.Default.Map,
                    onClick = onNavigateToMaps,
//                    modifier = Modifier.weight(1f),
                    modifier = Modifier
                        .width(120.dp)
                )
            }




        }
    }
}

@Composable
private fun HomeActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CardWhite),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF6E6A6A))
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF888888)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, fontSize = 12.sp, color = Color(0xFFCCCCCC))
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




