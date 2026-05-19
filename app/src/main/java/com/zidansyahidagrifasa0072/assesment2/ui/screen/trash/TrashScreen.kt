package com.zidansyahidagrifasa0072.assesment2.ui.screen.trash

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.zidansyahidagrifasa0072.assesment2.viewmodel.CctvViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(navController: NavHostController, viewModel: CctvViewModel, snackbarHostState: SnackbarHostState) {
    val trashList by viewModel.trashProjects.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recycle Bin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (trashList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Tempat sampah kosong", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trashList) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(14.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.customerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(item.address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row {
                                IconButton(onClick = {
                                    viewModel.restoreFromTrash(item.id)
                                    scope.launch { snackbarHostState.showSnackbar("Project berhasil dipulihkan") }
                                }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Restore")
                                }
                                IconButton(onClick = {
                                    viewModel.deletePermanently(item.id)
                                    scope.launch { snackbarHostState.showSnackbar("Project dihapus permanen") }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Permanent", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}