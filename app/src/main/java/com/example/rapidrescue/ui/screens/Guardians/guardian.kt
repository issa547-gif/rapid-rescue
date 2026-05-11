package com.example.rapidrescue.ui.screens.guardians

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rapidrescue.data.models.Contact
import com.example.rapidrescue.data.repositories.ContactRepository
import com.example.rapidrescue.ui.theme.CardWhite
import com.example.rapidrescue.ui.theme.DeepNavy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GuardiansViewModel : ViewModel() {
    private val repository = ContactRepository()

    private val _guardians = MutableStateFlow<List<Contact>>(emptyList())
    val guardians = _guardians.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _added = MutableStateFlow(false)
    val added = _added.asStateFlow()

    init { loadGuardians() }

    fun loadGuardians() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _guardians.value = repository.getContacts().filter { it.isGuardian }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addGuardian(name: String, phone: String, relationship: String) {
        if (name.isBlank() || phone.isBlank()) {
            _error.value = "Name and phone are required"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.addContact(
                    Contact(
                        name = name,
                        phone = phone,
                        relationship = relationship,
                        isGuardian = true
                    )
                )
                loadGuardians()
                _added.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add guardian"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteGuardian(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteContact(id)
                _guardians.value = _guardians.value.filter { it.id != id }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun resetAdded() { _added.value = false }
    fun resetError() { _error.value = null }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardiansScreen(
    onBack: () -> Unit,
    viewModel: GuardiansViewModel = viewModel()
) {
    val guardians by viewModel.guardians.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val added by viewModel.added.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(added) {
        if (added) {
            showSheet = false
            viewModel.resetAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trusted guardians") },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Color(0xFF1E5FA5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add guardian")
            }
        },
        containerColor = DeepNavy
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading && guardians.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF1E5FA5)
                )
            } else if (guardians.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No guardians yet",
                        fontSize = 16.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "Guardians are alerted when you send SOS",
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(guardians, key = { it.id }) { guardian ->
                        GuardianCard(
                            guardian = guardian,
                            onDelete = { viewModel.deleteGuardian(guardian.id) }
                        )
                    }
                }
            }

            error?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.resetError() }) {
                            Text("Dismiss", color = Color.White)
                        }
                    }
                ) { Text(it) }
            }
        }
    }

    if (showSheet) {
        AddGuardianSheet(
            isLoading = isLoading,
            onDismiss = { showSheet = false },
            onAdd = { name, phone, relationship ->
                viewModel.addGuardian(name, phone, relationship)
            }
        )
    }
}

@Composable
private fun GuardianCard(guardian: Contact, onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0D9488).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = guardian.name.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D9488)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = guardian.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A2233)
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE1F5EE)
                    ) {
                        Text(
                            text = "Guardian",
                            fontSize = 10.sp,
                            color = Color(0xFF0D9488),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = guardian.phone,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
                if (!guardian.relationship.isNullOrBlank()) {
                    Text(
                        text = guardian.relationship!!,
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Remove guardian") },
            text = { Text("Remove ${guardian.name} as a trusted guardian?") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDialog = false }) {
                    Text("Remove", color = Color(0xFFB91C1C))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGuardianSheet(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add trusted guardian",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = CardWhite
            )
            Text(
                text = "Guardians receive your location when you trigger SOS.",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = relationship,
                onValueChange = { relationship = it },
                label = { Text("Relationship (optional)") },
                placeholder = { Text("e.g. Mother, Friend") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = { onAdd(name, phone, relationship) },
                enabled = name.isNotBlank() && phone.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E5FA5))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Add guardian",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}