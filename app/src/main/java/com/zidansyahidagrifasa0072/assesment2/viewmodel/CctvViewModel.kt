package com.zidansyahidagrifasa0072.assesment2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zidansyahidagrifasa0072.assesment2.data.database.CctvDatabase
import com.zidansyahidagrifasa0072.assesment2.data.datastore.SettingsDataStore
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProgressHistoryEntity
import com.zidansyahidagrifasa0072.assesment2.data.entity.ProjectEntity
import com.zidansyahidagrifasa0072.assesment2.data.repository.CctvRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CctvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CctvRepository
    private val settingsDataStore: SettingsDataStore

    val isDarkMode: StateFlow<Boolean>
    val themeColor: StateFlow<String>
    val isGridMode: StateFlow<Boolean>

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("Semua")
    val selectedFilter: StateFlow<String> = _selectedFilter

    init {
        val db = CctvDatabase.getDatabase(application)
        repository = CctvRepository(db.projectDao(), db.progressHistoryDao())
        settingsDataStore = SettingsDataStore(application)

        isDarkMode = settingsDataStore.isDarkMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
        themeColor = settingsDataStore.themeColor.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Blue")
        isGridMode = settingsDataStore.isGridMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

        triggerAutoDelete()
    }

    val filteredProjects: StateFlow<List<ProjectEntity>> = combine(
        repository.allActiveProjects, _searchQuery, _selectedFilter
    ) { projects, query, filter ->
        projects.filter {
            (it.customerName.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)) &&
                    (filter == "Semua" || it.progressStatus == filter)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashProjects: StateFlow<List<ProjectEntity>> = repository.trashProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setFilter(filter: String) { _selectedFilter.value = filter }

    fun upsertProject(
        id: Long = 0, name: String, phone: String, address: String,
        count: Int, type: String, tech: String, date: String, notes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val project = ProjectEntity(
                id = id, customerName = name, phone = phone, address = address,
                totalCamera = count, cctvType = type, technician = tech,
                installDate = date, progressStatus = "Menunggu Survey", notes = notes
            )
            val generatedId = repository.insertProject(project)
            if (id == 0L) {
                repository.insertHistory(
                    ProgressHistoryEntity(
                        projectId = generatedId,
                        progressTitle = "Project Dibuat",
                        progressNote = "Pemesanan instalasi sistem baru didaftarkan.",
                        updateDate = getCurrentFormattedDate()
                    )
                )
            }
            onSuccess()
        }
    }

    fun updateProgress(id: Long, newStatus: String, notes: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.getProjectById(id)?.let {
                val updated = it.copy(progressStatus = newStatus)
                repository.updateProject(updated)
                repository.insertHistory(
                    ProgressHistoryEntity(
                        projectId = id, progressTitle = newStatus,
                        progressNote = notes, updateDate = getCurrentFormattedDate()
                    )
                )
                onSuccess()
            }
        }
    }

    fun moveToTrash(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.getProjectById(id)?.let {
                repository.updateProject(it.copy(isDeleted = true, deletedAt = System.currentTimeMillis()))
                onSuccess()
            }
        }
    }

    fun restoreFromTrash(id: Long) {
        viewModelScope.launch {
            repository.getProjectById(id)?.let {
                repository.updateProject(it.copy(isDeleted = false, deletedAt = null))
            }
        }
    }

    fun deletePermanently(id: Long) {
        viewModelScope.launch { repository.deleteProjectPermanently(id) }
    }

    fun getHistory(projectId: Long): Flow<List<ProgressHistoryEntity>> = repository.getHistoryForProject(projectId)

    suspend fun getProjectById(id: Long): ProjectEntity? = repository.getProjectById(id)

    fun toggleDarkMode(isDark: Boolean) = viewModelScope.launch { settingsDataStore.saveDarkMode(isDark) }
    fun setThemeColor(color: String) = viewModelScope.launch { settingsDataStore.saveThemeColor(color) }
    fun toggleLayoutMode(isGrid: Boolean) = viewModelScope.launch { settingsDataStore.saveLayoutMode(isGrid) }

    private fun triggerAutoDelete() {
        viewModelScope.launch {
            val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000L
            repository.autoDeleteOldTrash(System.currentTimeMillis(), BettingConstantThreshold(sevenDaysInMillis))
        }
    }

    private fun BettingConstantThreshold(valIn: Long): Long = valIn

    private fun getCurrentFormattedDate(): String {
        return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
    }
}