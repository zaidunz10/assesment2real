package com.zidansyahidagrifasa0072.assesment2.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.zidansyahidagrifasa0072.assesment2.navigation.Screen
import com.zidansyahidagrifasa0072.assesment2.viewmodel.CctvViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProjectScreen(
    navController: NavHostController,
    viewModel: CctvViewModel,
    projectId: Long,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val project by viewModel.getProjectById(projectId).collectAsState(initial = null)

    // State untuk mengontrol tampilan Alert Dialog
    var showDeleteDialog by remember { mutableStateOf(false) }

    // --- POP-UP ALERT DIALOG KONFIRMASI HAPUS ---
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = "Konfirmasi Hapus", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Anda yakin mau hapus data ini? Data akan dipindahkan ke Recycle Bin.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false // Tutup dialog
                        viewModel.moveToTrash(projectId) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Proyek dipindahkan ke tempat sampah")
                                navController.popBackStack() // Balik ke Home
                            }
                        }
                    }
                ) {
                    Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pemasangan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    // Tombol Edit
                    IconButton(onClick = { navController.navigate(Screen.EditProject.createRoute(projectId)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    // Tombol Hapus (Sekarang memicu dialog muncul)
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                    }
                }
            )
        }
    ) { innerPadding ->
        project?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Nama Pelanggan:", fontWeight = FontWeight.Bold)
                Text(data.customerName)

                Text("No. Telepon:", fontWeight = FontWeight.Bold)
                Text(data.phone)

                Text("Alamat:", fontWeight = FontWeight.Bold)
                Text(data.address)

                Text("Jumlah Kamera:", fontWeight = FontWeight.Bold)
                Text("${data.totalCamera} Unit")

                Text("Tipe CCTV:", fontWeight = FontWeight.Bold)
                Text(data.cctvType)

                Text("Teknisi:", fontWeight = FontWeight.Bold)
                Text(data.technician)

                Text("Tanggal Pasang:", fontWeight = FontWeight.Bold)
                Text(data.installDate)

                Text("Status Saat Ini:", fontWeight = FontWeight.Bold)
                Text(data.progressStatus)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(Screen.UpdateProgress.createRoute(projectId)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Progress Kerja")
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}