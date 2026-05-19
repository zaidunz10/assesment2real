package com.zidansyahidagrifasa0072.assesment2.ui.screen.update_progress

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zidansyahidagrifasa0072.assesment2.viewmodel.CctvViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProgressScreen(
    navController: NavHostController,
    viewModel: CctvViewModel,
    projectId: Long,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    // Mengambil data project secara reaktif lewat Flow
    val project by viewModel.getProjectById(projectId).collectAsState(initial = null)

    var currentStatus by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("Menunggu Survey", "Dalam Pemasangan", "Setting DVR/NVR", "Selesai")

    LaunchedEffect(project) {
        project?.let {
            if (currentStatus.isEmpty()) currentStatus = it.progressStatus
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Project: ${project?.customerName ?: "Memuat..."}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // --- DROPDOWN STATUS ---
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status Progress") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            Modifier.clickable { expanded = !expanded }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                currentStatus = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            // --- INPUT CATATAN ---
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan Progress (Opsional)") },
                placeholder = { Text("Contoh: Kabel coaxial sudah ditarik, sisa pasang kamera") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- TOMBOL SIMPAN ---
            Button(
                onClick = {
                    if (currentStatus.isNotEmpty()) {
                        // Memanggil fungsi update dengan callback onSuccess {} yang benar
                        viewModel.updateProjectProgress(projectId, currentStatus, notes) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Status berhasil diperbarui!")
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = project != null
            ) {
                Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
            }
        }
    }
}