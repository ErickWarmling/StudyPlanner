package com.example.studyplanner.ui.telas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyplanner.model.Disciplina
import com.example.studyplanner.viewModel.DisciplinaViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.studyplanner.ui.theme.BlueMedium

@Composable
fun TelaDisciplinas(
    navController: NavController,
    viewModel: DisciplinaViewModel = viewModel()
) {
    val disciplinas = viewModel.disciplinasComProgresso.collectAsState()
    var disciplinaParaExcluirNome by remember { mutableStateOf<Disciplina?>(null) }

    if (disciplinaParaExcluirNome != null) {
        AlertDialog(
            onDismissRequest = { disciplinaParaExcluirNome = null},
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir a disciplina ${disciplinaParaExcluirNome!!.nome.trim()}?") },
            confirmButton = {
                Text(
                    "Excluir",
                    modifier = Modifier
                        .clickable {
                            viewModel.excluirDisciplina(disciplinaParaExcluirNome!!)
                            disciplinaParaExcluirNome = null
                        }
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            },
            dismissButton = {
                Text(
                    "Cancelar",
                    modifier = Modifier
                        .clickable { disciplinaParaExcluirNome = null }
                        .padding(16.dp)
                )
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            BotaoAdicionarDisciplina{
                navController.navigate("cadastro_disciplina")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 10.dp
                )
        ) {

            if (disciplinas.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Nenhuma disciplina",
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Nenhuma disciplina cadastrada",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn {
                    items(disciplinas.value) { disciplinaComProgresso ->
                        DisciplinaCard(
                            disciplina = disciplinaComProgresso.disciplina,
                            progresso = disciplinaComProgresso.progresso,
                            progressoPercentual = disciplinaComProgresso.progressoPercentual,
                            onClick = {
                                navController.navigate("tarefa_disciplina/${disciplinaComProgresso.disciplina.id}")
                            },
                            onDelete = {
                                disciplinaParaExcluirNome = disciplinaComProgresso.disciplina
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BotaoAdicionarDisciplina(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = BlueMedium,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Adicionar Disciplina"
        )
    }
}

@Composable
fun DisciplinaCard(
    disciplina: Disciplina,
    progresso: Float,
    progressoPercentual: Int,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    val painter = rememberAsyncImagePainter(
        model = disciplina.urlImagem
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = disciplina.nome,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = progresso,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = BlueMedium
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "$progressoPercentual% completo",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir disciplina",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}