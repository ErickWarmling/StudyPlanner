package com.example.studyplanner.ui.telas

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyplanner.ui.theme.BluePrimary
import com.example.studyplanner.viewModel.CadastroTarefaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroTarefa(
    navController: NavController,
    viewModel: CadastroTarefaViewModel = viewModel()
) {
    val context = LocalContext.current


    val nome by viewModel.nome.collectAsState()
    val descricao by viewModel.descricao.collectAsState()
    val dataEntrega by viewModel.dataEntrega.collectAsState()
    val disciplinaSelecionada by viewModel.disciplinaSelecionada.collectAsState()
    val todasDisciplinas by viewModel.todasDisciplinas.collectAsState()


    val mensagem by viewModel.mensagem.collectAsState()
    val sucesso by viewModel.sucesso.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }


    var expanded by remember { mutableStateOf(false) }


    LaunchedEffect(mensagem) {
        mensagem?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limparMensagem()
        }
    }

    LaunchedEffect(sucesso) {
        if (sucesso) {
            navController.popBackStack()
        }
    }


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dataEntrega ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    viewModel.onDataEntregaChange(datePickerState.selectedDateMillis)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        OutlinedTextField(
            value = nome,
            onValueChange = viewModel::onNomeChange,
            label = { Text("Nome da tarefa") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = descricao,
            onValueChange = viewModel::onDescricaoChange,
            label = { Text("Descrição (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = dataEntrega?.let { formatarData(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Data de entrega (opcional)") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Selecionar Data",
                    modifier = Modifier.clickable { showDatePicker = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )
        Spacer(modifier = Modifier.height(16.dp))


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = disciplinaSelecionada?.nome ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Disciplina") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                todasDisciplinas.forEach { disciplina ->
                    DropdownMenuItem(
                        text = { Text(disciplina.nome) },
                        onClick = {
                            viewModel.onDisciplinaChange(disciplina)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = { viewModel.salvarTarefa() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Cadastrar",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun formatarData(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}