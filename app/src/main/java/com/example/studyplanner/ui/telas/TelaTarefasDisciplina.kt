package com.example.studyplanner.ui.telas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EventBusy

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.studyplanner.model.DisciplinaComTarefas
import com.example.studyplanner.model.Tarefa
import com.example.studyplanner.ui.theme.BlueMedium
import com.example.studyplanner.viewModel.TarefasDisciplinaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TelaTarefasDisciplina(
    navController: NavController,
    viewModel: TarefasDisciplinaViewModel = viewModel()
) {
    val disciplinaComTarefas by viewModel.disciplinaComTarefas.collectAsState(initial = null)
    val context = LocalContext.current
    val mensagem by viewModel.mensagem.collectAsState()

    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limparMensagem()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (disciplinaComTarefas != null) {
                FloatingActionButton(
                    onClick = {

                        navController.navigate("cadastro_tarefa/${disciplinaComTarefas!!.disciplina.id}")
                    },
                    containerColor = BlueMedium,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (disciplinaComTarefas == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                val disciplina = disciplinaComTarefas!!.disciplina
                val tarefas = disciplinaComTarefas!!.tarefas

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    item {
                        HeaderDisciplina(disciplina = disciplina.nome, imagemUrl = disciplina.urlImagem)
                        Text(
                            text = "Tarefas",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                    }

                    // 2. Lista de Tarefas
                    if (tarefas.isEmpty()) {
                        item {
                            Text(
                                "Nenhuma tarefa cadastrada.",
                                modifier = Modifier.padding(16.dp),
                                color = Color.Gray
                            )
                        }
                    } else {
                        items(tarefas) { tarefa ->
                            TarefaCard(
                                tarefa = tarefa,
                                onCheckChange = { concluida ->
                                    viewModel.marcarTarefaConcluida(tarefa, concluida)
                                },
                                onDelete = {
                                    viewModel.excluirTarefa(tarefa)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderDisciplina(disciplina: String, imagemUrl: String) {
    val painter = rememberAsyncImagePainter(model = imagemUrl)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Text(
        text = disciplina,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun TarefaCard(
    tarefa: Tarefa,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarefa.concluida,
                onCheckedChange = onCheckChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tarefa.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (tarefa.descricao != null) {
                    Text(
                        text = tarefa.descricao,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                if (tarefa.dataEntrega != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EditCalendar,
                            contentDescription = "Data de entrega",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Entrega: ${formatarData(tarefa.dataEntrega)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EventBusy,
                            contentDescription = "Sem data",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sem data de entrega",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir tarefa",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


private fun formatarData(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}