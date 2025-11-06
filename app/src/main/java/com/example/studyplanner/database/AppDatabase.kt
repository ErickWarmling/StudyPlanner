package com.example.studyplanner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studyplanner.dao.DisciplinaDao
import com.example.studyplanner.dao.TarefaDao
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.model.Tarefa

@Database(
    entities = [Disciplina::class, Tarefa::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun disciplinaDao(): DisciplinaDao
    abstract fun tarefaDao(): TarefaDao
}