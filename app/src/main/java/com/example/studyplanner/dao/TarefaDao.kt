package com.example.studyplanner.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.studyplanner.model.Tarefa

@Dao
interface TarefaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTarefa(tarefa: Tarefa): Long

    @Update
    fun editTarefa(tarefa: Tarefa)

    @Delete
    fun deleteTarefa(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas")
    fun getAll(): List<Tarefa>

    // Buscar apenas as tarefas de uma determinada disciplina
    @Query("SELECT * FROM tarefas WHERE tarefa_disciplina_id = :disciplinaId")
    fun getTarefasDisciplina(disciplinaId: Int): List<Tarefa>

    // Marcar tarefa como concluída ou não
    @Query("UPDATE tarefas SET tarefa_concluida = :concluida WHERE tarefa_id = :tarefaId")
    fun tarefaConcluida(tarefaId: Int, concluida: Boolean)
}