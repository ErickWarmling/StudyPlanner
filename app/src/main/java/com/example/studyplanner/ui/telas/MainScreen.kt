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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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

    // Lógica de Título e Botão "Voltar"
    val title: String
    val isBack: Boolean

    when {
        currentRoute == "disciplinas" -> {
            title = "Disciplinas"
            isBack = false
        }
        currentRoute == "resumo_estudo" -> {
            title = "Resumo do Estudo"
            isBack = false
        }
        currentRoute == "cadastro_disciplina" -> {
            title = "Cadastro de Disciplina"
            isBack = true
        }
        currentRoute?.startsWith("tarefa_disciplina") == true -> {
            title = "Tarefas da Disciplina"
            isBack = true
        }
        currentRoute?.startsWith("cadastro_tarefa") == true -> {
            title = "Cadastro de Tarefa"
            isBack = true
        }
        else -> {
            title = "Disciplinas" // Padrão
            isBack = false
        }
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
                TopBar(
                    title = title,
                    onMenuClick = {
                        if (isBack) {
                            navController.popBackStack()
                        } else {
                            scope.launch { drawerState.open() }
                        }
                    },
                    isBack = isBack
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "disciplinas",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("disciplinas") {
                    TelaDisciplinas(navController)
                }
                composable("resumo_estudo") {
                    TelaResumoEstudo()
                }
                composable("cadastro_disciplina") {
                    TelaCadastroDisciplina(navController)
                }

                // NOVA ROTA: TELA DE TAREFAS
                composable(
                    route = "tarefa_disciplina/{disciplinaId}",
                    arguments = listOf(navArgument("disciplinaId") { type = NavType.IntType })
                ) {
                    // O ViewModel vai pegar o ID automaticamente pelo SavedStateHandle
                    TelaTarefasDisciplina(navController = navController)
                }

                // NOVA ROTA: CADASTRO DE TAREFA
                composable(
                    route = "cadastro_tarefa/{disciplinaId}",
                    arguments = listOf(navArgument("disciplinaId") { type = NavType.IntType })
                ) {
                    // O ViewModel vai pegar o ID automaticamente
                    TelaCadastroTarefa(navController = navController)
                }
            }
        }
    }
}