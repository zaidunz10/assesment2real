package com.zidansyahidagrifasa0072.assesment2.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val THEME_COLOR_KEY = stringPreferencesKey("theme_color")
        val LAYOUT_MODE_KEY = booleanPreferencesKey("layout_mode") // true = Grid, false = List
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    val themeColor: Flow<String> = context.dataStore.data.map { it[THEME_COLOR_KEY] ?: "Blue" }
    val isGridMode: Flow<Boolean> = context.dataStore.data.map { it[LAYOUT_MODE_KEY] ?: true }

    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { it[DARK_MODE_KEY] = isDark }
    }

    suspend fun saveThemeColor(color: String) {
        context.dataStore.edit { it[THEME_COLOR_KEY] = color }
    }

    suspend fun saveLayoutMode(isGrid: Boolean) {
        context.dataStore.edit { it[LAYOUT_MODE_KEY] = isGrid }
    }
}