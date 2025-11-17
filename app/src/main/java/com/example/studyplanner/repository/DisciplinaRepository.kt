package com.example.studyplanner.repository

import com.example.studyplanner.dao.DisciplinaDao
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.model.DisciplinaComTarefas
import kotlinx.coroutines.flow.Flow

class DisciplinaRepository(private val disciplinaDao: DisciplinaDao) {

    fun saveDisciplina(disciplina: Disciplina): Int {
        return disciplinaDao.addDisciplina(disciplina).toInt()
    }

    fun deleteDisciplina(disciplina: Disciplina) {
        disciplinaDao.deleteDisciplina(disciplina)
    }

    fun getAll(): Flow<List<Disciplina>> {
        return disciplinaDao.getAll()
    }

    fun getDisciplinasComTarefas(): Flow<List<DisciplinaComTarefas>> {
        return disciplinaDao.getDisciplinasComTarefas()
    }

    fun getDisciplinaComTarefas(disciplinaId: Int): Flow<DisciplinaComTarefas?> {
        return disciplinaDao.getDisciplinaComTarefas(disciplinaId)
    }
}