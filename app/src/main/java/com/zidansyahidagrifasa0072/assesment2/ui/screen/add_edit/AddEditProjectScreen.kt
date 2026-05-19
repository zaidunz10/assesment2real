package com.zidansyahidagrifasa0072.assesment2.ui.screen.add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun AddEditProjectScreen(
    navController: NavHostController,
    viewModel: CctvViewModel,
    snackbarHostState: SnackbarHostState,
    projectId: Long = 0L
) {
    val scope = rememberCoroutineScope()
    val isEditMode = projectId != 0L

    // Observasi data project jika dalam mode edit
    val projectExisting by viewModel.getProjectById(projectId).collectAsState(initial = null)

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cameraCount by remember { mutableStateOf("") }
    var cctvType by remember { mutableStateOf("") }
    var technician by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Set form jika data existing ditemukan (Mode Edit)
    LaunchedEffect(projectExisting) {
        projectExisting?.let {
            name = it.customerName
            phone = it.phone
            address = it.address
            cameraCount = it.totalCamera.toString()
            cctvType = it.cctvType
            technician = it.technician
            date = it.installDate
            notes = it.notes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Berkas Proyek" else "Registrasi Pasang Baru") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Pelanggan") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("No. Telepon") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Alamat Lokasi") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = cameraCount, onValueChange = { cameraCount = it }, label = { Text("Jumlah Kamera (Unit)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = cctvType, onValueChange = { cctvType = it }, label = { Text("Jenis/Type CCTV") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = technician, onValueChange = { technician = it }, label = { Text("Nama Teknisi") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Tanggal Instalasi") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Catatan Tambahan") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && address.isNotEmpty()) {
                        viewModel.upsertProject(
                            id = projectId,
                            name = name,
                            phone = phone,
                            address = address,
                            count = cameraCount.toIntOrNull() ?: 0,
                            type = cctvType,
                            tech = technician,
                            date = date,
                            notes = notes
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar(if (isEditMode) "Data proyek diperbarui!" else "Proyek baru didaftarkan!")
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Daftarkan Pemasangan", fontWeight = FontWeight.Bold)
            }
        }
    }
}