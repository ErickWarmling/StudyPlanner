package com.example.studyplanner.ui.telas

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyplanner.ui.theme.BluePrimary
import com.example.studyplanner.viewModel.CadastroDisciplinaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroDisciplina(
    navController: NavController,
    viewModel: CadastroDisciplinaViewModel = viewModel()
) {
    val context = LocalContext.current

    val nome by viewModel.nomeDisciplina.collectAsState()
    val imagemUri by viewModel.imageUri.collectAsState()
    val mensagem by viewModel.mensagem.collectAsState()
    val sucesso by viewModel.sucesso.collectAsState()

    val selecionarImagem = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            viewModel.onImageSelecionada(uri)
        }
    }

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

    FormularioCadastroDisciplina(
        modifier = Modifier
            .padding(24.dp),
        nome = nome,
        onNomeChange = viewModel::onNomeChange,
        imagemUri = imagemUri,
        onSelecionarImagem = { selecionarImagem.launch("image/*") },
        onSalvar = { viewModel.salvarDisciplina() }
    )
}

@Composable
fun FormularioCadastroDisciplina(
    modifier: Modifier = Modifier,
    nome: String,
    onNomeChange: (String) -> Unit,
    imagemUri: Uri?,
    onSelecionarImagem: () -> Unit,
    onSalvar: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome da disciplina") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = imagemUri?.lastPathSegment ?: "",
            onValueChange = {},
            label = { Text("Foto de capa") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Selecionar imagem",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onSelecionarImagem() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelecionarImagem() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSalvar,
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