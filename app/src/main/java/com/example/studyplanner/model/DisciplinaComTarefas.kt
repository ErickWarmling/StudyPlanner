package com.example.studyplanner.model

import androidx.room.Embedded
import androidx.room.Relation

data class DisciplinaComTarefas (
    @Embedded
    val disciplina: Disciplina,
    @Relation(
        parentColumn = "disciplina_id",
        entityColumn = "tarefa_disciplina_id"
    )
    val tarefas: List<Tarefa>
)