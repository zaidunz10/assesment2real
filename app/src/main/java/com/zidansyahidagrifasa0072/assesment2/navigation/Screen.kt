package com.zidansyahidagrifasa0072.assesment2.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddProject : Screen("add_project")
    object DetailProject : Screen("detail_project/{projectId}") {
        fun createRoute(id: Long) = "detail_project/$id"
    }
    object UpdateProgress : Screen("update_progress/{projectId}") {
        fun createRoute(id: Long) = "update_progress/$id"
    }
    object EditProject : Screen("edit_project/{projectId}") {
        fun createRoute(id: Long) = "edit_project/$id"
    }
    object ProgressHistory : Screen("progress_history/{projectId}") {
        fun createRoute(id: Long) = "progress_history/$id"
    }
    object Trash : Screen("trash")
    object Settings : Screen("settings")
}