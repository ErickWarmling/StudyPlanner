package com.example.studyplanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas",
        foreignKeys = [
            ForeignKey(
                entity = Disciplina::class,
                parentColumns = ["disciplina_id"],
                childColumns = ["tarefa_disciplina_id"],
                onDelete = ForeignKey.CASCADE
            )
        ],
    indices = [Index("tarefa_disciplina_id")]
)
data class Tarefa (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tarefa_id")
    val id: Int = 0,

    @ColumnInfo(name = "tarefa_nome")
    val nome: String,

    @ColumnInfo(name = "tarefa_descricao")
    val descricao: String?,

    @ColumnInfo(name = "tarefa_data_entrega")
    val dataEntrega: Long? = null,

    @ColumnInfo(name = "tarefa_concluida")
    val concluida: Boolean = false,

    @ColumnInfo(name = "tarefa_disciplina_id")
    val disciplinaId: Int
)