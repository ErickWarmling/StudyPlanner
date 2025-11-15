package com.example.studyplanner.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.model.DisciplinaComTarefas
import kotlinx.coroutines.flow.Flow

@Dao
interface DisciplinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDisciplina(disciplina: Disciplina): Long

    @Delete
    fun deleteDisciplina(disciplina: Disciplina)

    @Query("SELECT * FROM disciplinas")
    fun getAll(): Flow<List<Disciplina>>

    // Buscar disciplinas com suas tarefas
    @Transaction
    @Query("SELECT * FROM disciplinas")
    fun getDisciplinasComTarefas(): Flow<List<DisciplinaComTarefas>>
}