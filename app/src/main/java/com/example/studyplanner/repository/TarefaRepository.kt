package com.example.studyplanner.repository

import com.example.studyplanner.dao.TarefaDao
import com.example.studyplanner.model.Tarefa
import kotlinx.coroutines.flow.Flow

class TarefaRepository(private val tarefaDao: TarefaDao) {

    fun saveTarefa(tarefa: Tarefa): Int {
        return tarefaDao.addTarefa(tarefa).toInt()
    }

    fun updateTarefa(tarefa: Tarefa) {
        tarefaDao.editTarefa(tarefa)
    }

    fun deleteTarefa(tarefa: Tarefa) {
        tarefaDao.deleteTarefa(tarefa)
    }

    fun getAll(): Flow<List<Tarefa>> {
        return tarefaDao.getAll()
    }

    fun getTarefasDisciplina(disciplinaId: Int): List<Tarefa> {
        return tarefaDao.getTarefasDisciplina(disciplinaId)
    }

    fun tarefaConcluida(tarefaId: Int, concluida: Boolean) {
        tarefaDao.tarefaConcluida(tarefaId, concluida)
    }
}