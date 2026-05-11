package com.example.rapidrescue.ui.screens.medical

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rapidrescue.data.models.Profile
import com.example.rapidrescue.data.repositories.ProfileRepository
import com.example.rapidrescue.ui.theme.DeepNavy
import com.example.rapidrescue.ui.theme.grey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MedicalViewModel : ViewModel() {
    private val repository = ProfileRepository()

    private val _profile = MutableStateFlow(Profile())
    val profile = _profile.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _saved = MutableStateFlow(false)
    val saved = _saved.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _profile.value = repository.getProfile() ?: Profile()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun save(
        fullName: String,
        bloodType: String,
        allergies: String,
        medications: String,
        emergencyNotes: String
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                repository.updateProfile(
                    Profile(
                        fullName = fullName,
                        bloodType = bloodType,
                        allergies = allergies,
                        medications = medications,
                        emergencyNotes = emergencyNotes
                    )
                )
                _saved.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to save"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun resetSaved() { _saved.value = false }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalScreen(
    onBack: () -> Unit,
    viewModel: MedicalViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saved by viewModel.saved.collectAsState()
    val error by viewModel.error.collectAsState()

    var fullName by remember(profile) { mutableStateOf(profile.fullName) }
    var bloodType by remember(profile) { mutableStateOf(profile.bloodType) }
    var allergies by remember(profile) { mutableStateOf(profile.allergies) }
    var medications by remember(profile) { mutableStateOf(profile.medications) }
    var emergencyNotes by remember(profile) { mutableStateOf(profile.emergencyNotes) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saved) {
        if (saved) {
            snackbarHostState.showSnackbar("Medical info saved")
            viewModel.resetSaved()
        }
    }

    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Unknown")
    var showBloodTypeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medical information") },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
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

            // Info banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = grey),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "This information is shown to first responders during an emergency. Keep it accurate and up to date.",
                    fontSize = 13.sp,
                    color = Color(0xFFB91C1C),
                    modifier = Modifier.padding(14.dp),
                    lineHeight = 20.sp
                )
            }

            // Personal
            MedicalSection(title = "Personal") {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Blood type picker
                OutlinedTextField(
                    value = bloodType.ifBlank { "Select blood type" },
                    onValueChange = {},
                    label = { Text("Blood type") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { showBloodTypeDialog = true }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Select",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                )
            }

            // Health info
            MedicalSection(title = "Health information") {
                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text("Allergies") },
                    placeholder = { Text("e.g. Penicillin, Peanuts") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = medications,
                    onValueChange = { medications = it },
                    label = { Text("Current medications") },
                    placeholder = { Text("e.g. Metformin 500mg") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = emergencyNotes,
                    onValueChange = { emergencyNotes = it },
                    label = { Text("Emergency notes") },
                    placeholder = { Text("e.g. Diabetic, carry glucose tablets") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (error != null) {
                Text(
                    text = error!!,
                    color = Color(0xFFB91C1C),
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = {
                    viewModel.save(fullName, bloodType, allergies, medications, emergencyNotes)
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5FA5))
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Save information",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showBloodTypeDialog) {
        AlertDialog(
            onDismissRequest = { showBloodTypeDialog = false },
            title = { Text("Select blood type") },
            text = {
                Column {
                    bloodTypes.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    bloodType = type
                                    showBloodTypeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(type, fontSize = 14.sp)
                            if (type == bloodType) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF1E5FA5),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (type != bloodTypes.last()) {
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
private fun MedicalSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Text(
            text = title,
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
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}