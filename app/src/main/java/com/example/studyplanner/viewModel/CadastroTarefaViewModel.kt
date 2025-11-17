package com.example.studyplanner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.studyplanner.database.AppDatabase
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.model.Tarefa
import com.example.studyplanner.repository.DisciplinaRepository
import com.example.studyplanner.repository.TarefaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroTarefaViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    // Repos
    private val disciplinaRepository: DisciplinaRepository
    private val tarefaRepository: TarefaRepository

    // Campos do Formulário
    private val _nome = MutableStateFlow("")
    val nome: StateFlow<String> = _nome

    private val _descricao = MutableStateFlow("")
    val descricao: StateFlow<String> = _descricao

    private val _dataEntrega = MutableStateFlow<Long?>(null)
    val dataEntrega: StateFlow<Long?> = _dataEntrega

    // Carrega todas as disciplinas para o Dropdown
    val todasDisciplinas: StateFlow<List<Disciplina>>

    // Disciplina selecionada
    private val _disciplinaSelecionada = MutableStateFlow<Disciplina?>(null)
    val disciplinaSelecionada: StateFlow<Disciplina?> = _disciplinaSelecionada

    // Controle de UI
    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    private val _sucesso = MutableStateFlow(false)
    val sucesso: StateFlow<Boolean> = _sucesso

    // ID inicial (se vindo da tela de tarefas)
    private val initialDisciplinaId: Int = savedStateHandle.get<Int>("disciplinaId") ?: 0

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studyplanner.db"
        ).build()

        disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
        tarefaRepository = TarefaRepository(db.tarefaDao())

        // Inicia o fluxo de disciplinas
        todasDisciplinas = disciplinaRepository.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        // Se um ID foi passado, pré-seleciona a disciplina
        if (initialDisciplinaId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val disciplinas = disciplinaRepository.getAll().stateIn(this).value
                _disciplinaSelecionada.value = disciplinas.find { it.id == initialDisciplinaId }
            }
        }
    }

    // Funções de atualização dos campos
    fun onNomeChange(nome: String) { _nome.value = nome }
    fun onDescricaoChange(desc: String) { _descricao.value = desc }
    fun onDataEntregaChange(data: Long?) { _dataEntrega.value = data }
    fun onDisciplinaChange(disciplina: Disciplina) { _disciplinaSelecionada.value = disciplina }

    fun salvarTarefa() {
        val nomeAtual = _nome.value
        val disciplinaAtual = _disciplinaSelecionada.value

        if (nomeAtual.isBlank() || disciplinaAtual == null) {
            _mensagem.value = "Nome e Disciplina são obrigatórios"
            return
        }

        val novaTarefa = Tarefa(
            nome = nomeAtual,
            descricao = _descricao.value.ifBlank { null },
            dataEntrega = _dataEntrega.value,
            disciplinaId = disciplinaAtual.id,
            concluida = false
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tarefaRepository.saveTarefa(novaTarefa)
            }
            _mensagem.value = "Tarefa cadastrada com sucesso!"
            _sucesso.value = true
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}