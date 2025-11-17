package com.example.studyplanner.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyplanner.ui.theme.BlueLight
import com.example.studyplanner.ui.theme.BluePrimary
import com.example.studyplanner.viewModel.ResumoEstudoViewModel

@Composable
fun TelaResumoEstudo(
    viewModel: ResumoEstudoViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ResumoCard(
            titulo = "Tarefas Cadastradas",
            valor = estado.totalTarefas.toString(),
            subtitulo = "total de tarefas registradas"
        )

        Spacer(modifier = Modifier.height(16.dp))

        ResumoCard(
            titulo = "Tarefas Concluídas",
            valor = estado.tarefasConcluidas.toString(),
            subtitulo = "total marcado como concluído"
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProgressoGeralCard(
            progresso = estado.progressoGeral
        )
    }
}

/**
 * Card para "Tarefas Cadastradas" e "Tarefas Concluídas".
 */
@Composable
fun ResumoCard(
    titulo: String,
    valor: String,
    subtitulo: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = BluePrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Card para o "Progresso Geral" com o gráfico circular.
 */
@Composable
fun ProgressoGeralCard(
    progresso: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Progresso Geral",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Box para sobrepor o texto e o gráfico
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Trilha de fundo do gráfico
                CircularProgressIndicator(
                    progress = 1f, // Completo
                    modifier = Modifier.fillMaxSize(),
                    color = BlueLight, // Cor de fundo (trilha)
                    strokeWidth = 16.dp
                )

                // Progresso real
                CircularProgressIndicator(
                    progress = progresso,
                    modifier = Modifier.fillMaxSize(),
                    color = BluePrimary, // Cor do progresso
                    strokeWidth = 16.dp,
                    strokeCap = StrokeCap.Round // Borda arredondada
                )

                // Texto da porcentagem
                Text(
                    text = "${(progresso * 100).toInt()}%",
                    fontSize = 56.sp, // <-- MUDANÇA PRINCIPAL AQUI
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }
        }
    }
}