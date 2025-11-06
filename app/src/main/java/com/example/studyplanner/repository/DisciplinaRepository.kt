package com.example.studyplanner.repository

import com.example.studyplanner.dao.DisciplinaDao
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.model.DisciplinaComTarefas

class DisciplinaRepository(private val disciplinaDao: DisciplinaDao) {

    fun saveDisciplina(disciplina: Disciplina): Int {
        return disciplinaDao.addDisciplina(disciplina).toInt()
    }

    fun deleteDisciplin(disciplina: Disciplina) {
        disciplinaDao.deleteDisciplina(disciplina)
    }

    fun getAll(): List<Disciplina> {
        return disciplinaDao.getAll()
    }

    fun getDisciplinasComTarefas(): List<DisciplinaComTarefas> {
        return disciplinaDao.getDisciplinasComTarefas()
    }
}