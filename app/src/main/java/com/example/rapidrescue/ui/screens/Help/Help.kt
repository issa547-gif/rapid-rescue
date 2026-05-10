package com.example.rapidrescue.ui.screens.Help

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rapidrescue.ui.theme.DeepNavy

data class FaqItem(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {
    val faqs = listOf(
        FaqItem(
            question = "How do I trigger an SOS alert?",
            answer = "Tap the large red SOS button on the home screen. The app will get your GPS location and send an alert to all your trusted guardians instantly."
        ),
        FaqItem(
            question = "Who receives my SOS alert?",
            answer = "Only your trusted guardians receive your alert. Add guardians in the Guardians tab. They will see your live GPS location."
        ),
        FaqItem(
            question = "Does SOS work without internet?",
            answer = "GPS location works offline but sending the alert requires an internet connection. We recommend keeping mobile data on at all times."
        ),
        FaqItem(
            question = "How do I add emergency contacts?",
            answer = "Go to the Guardians tab and tap the + button. Fill in the name, phone number and relationship then tap Add guardian."
        ),
        FaqItem(
            question = "What is medical information used for?",
            answer = "Your medical info is shown on the receiver screen alongside your location. This helps first responders know your blood type, allergies and medications."
        ),
        FaqItem(
            question = "How do I update my profile?",
            answer = "Go to the Profile tab and tap Medical information to update your health details, or tap Privacy & security to change your password."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & support") },
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Contact support card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F1FB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF1E5FA5),
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Contact support",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E5FA5)
                        )
                        Text(
                            text = "support@rapidrescue.app",
                            fontSize = 13.sp,
                            color = Color(0xFF378ADD)
                        )
                    }
                }
            }

            // FAQs
            Text(
                text = "Frequently asked questions",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    faqs.forEachIndexed { index, faq ->
                        FaqRow(faq = faq)
                        if (index != faqs.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE2E6ED)
                            )
                        }
                    }
                }
            }

            // Emergency numbers
            Text(
                text = "Emergency numbers",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 4.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    EmergencyNumberRow(service = "Police", number = "999")
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                    EmergencyNumberRow(service = "Ambulance", number = "999")
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                    EmergencyNumberRow(service = "Fire", number = "999")
                    HorizontalDivider(modifier = Modifier.padding(start = 16.dp), thickness = 0.5.dp, color = Color(0xFFE2E6ED))
                    EmergencyNumberRow(service = "Kenya Red Cross", number = "1199")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FaqRow(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.question,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A2233),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = faq.answer,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun EmergencyNumberRow(service: String, number: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(service, fontSize = 14.sp, color = Color(0xFF1A2233))
        Text(
            text = number,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB91C1C)
        )
    }
}