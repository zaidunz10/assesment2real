package com.zidansyahidagrifasa0072.assesment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.zidansyahidagrifasa0072.assesment2.navigation.SetupNavGraph
import com.zidansyahidagrifasa0072.assesment2.ui.theme.CctvManagerTheme
import com.zidansyahidagrifasa0072.assesment2.viewmodel.CctvViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CctvViewModel = viewModel()
            val isDark = viewModel.isDarkMode.collectAsStateWithLifecycle()
            val colorOption = viewModel.themeColor.collectAsStateWithLifecycle()

            CctvManagerTheme(darkTheme = isDark.value, colorOption = colorOption.value) {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SetupNavGraph(
                            navController = navController,
                            viewModel = viewModel,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}