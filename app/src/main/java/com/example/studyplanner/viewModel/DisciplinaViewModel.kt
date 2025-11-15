package com.example.studyplanner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.studyplanner.database.AppDatabase
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.repository.DisciplinaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DisciplinaViewModel(application: Application) : AndroidViewModel(application){

    private val disciplinaRepository: DisciplinaRepository

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studyplanner.db"
        ).build()

        disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
    }

    val disciplinas: StateFlow<List<Disciplina>> =
        disciplinaRepository.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun excluirDisciplina(disciplina: Disciplina) {
        viewModelScope.launch(Dispatchers.IO) {
            disciplinaRepository.deleteDisciplina(disciplina)
            _mensagem.value = "Disciplina excluída"
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}