package com.example.studyplanner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.studyplanner.database.AppDatabase
import com.example.studyplanner.repository.TarefaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Data class para guardar o estado calculado
data class ResumoEstado(
    val totalTarefas: Int = 0,
    val tarefasConcluidas: Int = 0,
    val progressoGeral: Float = 0f
)

class ResumoEstudoViewModel(application: Application) : AndroidViewModel(application) {

    private val tarefaRepository: TarefaRepository

    val estado: StateFlow<ResumoEstado>

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studyplanner.db"
        ).build()

        tarefaRepository = TarefaRepository(db.tarefaDao())

        // Mapeia o Flow<List<Tarefa>> para o Flow<ResumoEstado>
        estado = tarefaRepository.getAll()
            .map { tarefas ->
                val total = tarefas.size
                val concluidas = tarefas.count { it.concluida }
                val progresso = if (total > 0) {
                    (concluidas.toFloat() / total.toFloat())
                } else {
                    0f
                }

                ResumoEstado(
                    totalTarefas = total,
                    tarefasConcluidas = concluidas,
                    progressoGeral = progresso
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResumoEstado() // Estado inicial
            )
    }
}