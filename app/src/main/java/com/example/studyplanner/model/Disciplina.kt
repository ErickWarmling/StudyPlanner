package com.example.studyplanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disciplinas")
data class Disciplina (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "disciplina_id")
    val id: Int = 0,

    @ColumnInfo(name = "disciplina_nome")
    val nome: String,

    @ColumnInfo(name = "disciplina_url_imagem")
    val urlImagem: String
)
