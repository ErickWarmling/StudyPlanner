package com.example.studyplanner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.studyplanner.database.AppDatabase
import com.example.studyplanner.model.DisciplinaComTarefas
import com.example.studyplanner.model.Tarefa
import com.example.studyplanner.repository.DisciplinaRepository
import com.example.studyplanner.repository.TarefaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class TarefasDisciplinaViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    private val disciplinaRepository: DisciplinaRepository
    private val tarefaRepository: TarefaRepository

    // Pega o ID da disciplina passado pela navegação
    private val disciplinaId: Int = savedStateHandle.get<Int>("disciplinaId") ?: 0

    val disciplinaComTarefas: Flow<DisciplinaComTarefas>

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studyplanner.db"
        ).build()

        disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
        tarefaRepository = TarefaRepository(db.tarefaDao())

        // Inicia o fluxo para buscar a disciplina e suas tarefas
        disciplinaComTarefas = disciplinaRepository.getDisciplinaComTarefas(disciplinaId).filterNotNull()
    }

    fun excluirTarefa(tarefa: Tarefa) {
        viewModelScope.launch(Dispatchers.IO) {
            tarefaRepository.deleteTarefa(tarefa)
            _mensagem.value = "Tarefa excluída"
        }
    }

    fun marcarTarefaConcluida(tarefa: Tarefa, concluida: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // A sua DAO `tarefaConcluida` usa IDs, mas é mais seguro usar o objeto
            // Vamos usar o `editTarefa` que você já tem
            val tarefaAtualizada = tarefa.copy(concluida = concluida)
            tarefaRepository.updateTarefa(tarefaAtualizada)
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}