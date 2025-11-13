package com.example.studyplanner.ui.telas

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studyplanner.ui.components.MenuLateral
import com.example.studyplanner.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        "resumo_estudo" -> "Resumo do Estudo"
        else -> "Disciplinas"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(currentRoute = currentRoute) { route ->
                scope.launch {
                    drawerState.close()
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo("disciplinas") { inclusive = false }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    "cadastro_disciplina" -> {
                        TopBar(
                            title = "Cadastro de Disciplina",
                            onMenuClick = { navController.popBackStack() },
                            isBack = true
                        )
                    }

                    else -> {
                        TopBar(
                            title = title,
                            onMenuClick = { scope.launch { drawerState.open() } },
                            isBack = false
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "disciplinas",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("disciplinas") { TelaDisciplinas(navController) }
                composable("resumo_estudo") { TelaResumoEstudo() }
                composable("cadastro_disciplina") { TelaCadastroDisciplina(navController) }
            }
        }
    }
}