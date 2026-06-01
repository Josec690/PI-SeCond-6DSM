package second.project.ui.veiculos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import second.project.model.UsuarioPerfil
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.VeiculoViewModel

@Composable
fun FormularioVeiculoScreen(viewModel: VeiculoViewModel, onSaved: () -> Unit, onBack: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.carregarMoradores()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader(
            title = "Gestão de Veículos",
            subtitle = "Cadastre ou atualize os veículos do condomínio.",
            onBack = onBack
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Dados do Veículo", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("Mantenha os dados do veículo sempre atualizados.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.placa,
                    onValueChange = { viewModel.placa = it },
                    label = { Text("Placa") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.modelo,
                    onValueChange = { viewModel.modelo = it },
                    label = { Text("Modelo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.cor,
                    onValueChange = { viewModel.cor = it },
                    label = { Text("Cor") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.proprietario,
                    onValueChange = {
                        viewModel.proprietario = it
                        viewModel.moradorVinculado = ""
                        viewModel.bloco = ""
                        viewModel.apartamento = ""
                    },
                    label = { Text("Proprietario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    supportingText = {
                        Text(
                            if (viewModel.carregandoMoradores) {
                                "Carregando moradores cadastrados..."
                            } else {
                                "Digite nome, e-mail, telefone, bloco ou apartamento e selecione o morador."
                            }
                        )
                    },
                    singleLine = true
                )

                if (viewModel.moradoresFiltrados.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        viewModel.moradoresFiltrados.forEach { morador ->
                            MoradorSearchResultCard(
                                morador = morador,
                                onClick = { viewModel.selecionarMorador(morador) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.moradorVinculado,
                    onValueChange = { },
                    label = { Text("Morador vinculado") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    readOnly = true,
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = viewModel.bloco,
                        onValueChange = { },
                        label = { Text("Bloco") },
                        modifier = Modifier.weight(1f),
                        shape = CrudDesign.fieldShape,
                        colors = crudOutlinedTextFieldColors(),
                        readOnly = true,
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = viewModel.apartamento,
                        onValueChange = { },
                        label = { Text("Apto") },
                        modifier = Modifier.weight(1f),
                        shape = CrudDesign.fieldShape,
                        colors = crudOutlinedTextFieldColors(),
                        readOnly = true,
                        singleLine = true
                    )
                }

                Spacer(Modifier.height(24.dp))

                if (viewModel.mensagemErro.isNotBlank()) {
                    Text(viewModel.mensagemErro, color = CrudDesign.danger, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(10.dp))
                }

                Button(
                    onClick = { viewModel.gravar(onSaved) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text("GRAVAR", color = CrudDesign.textPrimary)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { viewModel.limparCampos() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrudDesign.textSecondary)
                ) {
                    Text("LIMPAR CAMPOS")
                }
            }
        }
    }
}

@Composable
private fun MoradorSearchResultCard(morador: UsuarioPerfil, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(morador.nome, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
            Text(morador.email, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            Text(
                "Bloco ${morador.bloco.ifBlank { "-" }} - Apto ${morador.apartamento.ifBlank { "-" }} - ${morador.telefone.ifBlank { "sem telefone" }}",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
