package com.example.studyplanner.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.studyplanner.database.AppDatabase
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.repository.DisciplinaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroDisciplinaViewModel (application: Application) : AndroidViewModel(application) {

    private val _nome = MutableStateFlow("")
    val nomeDisciplina: StateFlow<String> = _nome

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    private val _sucesso = MutableStateFlow(false)
    val sucesso = _sucesso

    private val disciplinaRepository: DisciplinaRepository

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studyplanner.db"
        ).build()

        disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
    }

    fun onNomeChange(nome: String) {
        _nome.value = nome
    }

    fun onImageSelecionada(uri: Uri?) {
        _imageUri.value = uri
    }

    fun salvarDisciplina() {
        val nomeAtual = nomeDisciplina.value
        val imagemAtual = imageUri.value

        if (nomeAtual.isBlank()) {
            _mensagem.value = "Informe o nome da disciplina"
            return
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val disciplina = Disciplina(
                    nome = nomeAtual,
                    urlImagem = imagemAtual?.toString() ?: ""
                )
                disciplinaRepository.saveDisciplina(disciplina)
            }

            _mensagem.value = "Disciplina cadastrada com sucesso!"
            _sucesso.value = true
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}