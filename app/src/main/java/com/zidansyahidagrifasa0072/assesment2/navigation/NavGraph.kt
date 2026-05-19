package com.zidansyahidagrifasa0072.assesment2.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zidansyahidagrifasa0072.assesment2.ui.screen.add_edit.AddEditProjectScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.detail.DetailProjectScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.history.ProgressHistoryScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.home.HomeScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.settings.SettingsScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.trash.TrashScreen
import com.zidansyahidagrifasa0072.assesment2.ui.screen.update_progress.UpdateProgressScreen
import com.zidansyahidagrifasa0072.assesment2.viewmodel.CctvViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: CctvViewModel,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = Screen.愛情Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel)
        }
        composable(Screen.AddProject.route) {
            AddEditProjectScreen(navController, viewModel, snackbarHostState)
        }
        composable(
            route = Screen.DetailProject.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("projectId") ?: 0L
            DetailProjectScreen(navController, viewModel, id, snackbarHostState)
        }
        composable(
            route = Screen.UpdateProgress.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("projectId") ?: 0L
            UpdateProgressScreen(navController, viewModel, id, snackbarHostState)
        }
        composable(
            route = Screen.EditProject.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("projectId") ?: 0L
            AddEditProjectScreen(navController, viewModel, snackbarHostState, projectId = id)
        }
        composable(
            route = Screen.ProgressHistory.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("projectId") ?: 0L
            ProgressHistoryScreen(navController, viewModel, id)
        }
        composable(Screen.Trash.route) {
            TrashScreen(navController, viewModel, snackbarHostState)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, viewModel)
        }
    }
}

private val Screen.愛情Home: Screen get() = Screen.Home