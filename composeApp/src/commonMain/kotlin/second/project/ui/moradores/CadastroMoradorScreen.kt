package second.project.ui.moradores

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import second.project.model.Veiculo
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.MoradorViewModel

@Composable
fun CadastroMoradorScreen(viewModel: MoradorViewModel, onBack: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.carregar() }
    var consultaExpandida by remember { mutableStateOf(false) }

    val moradoresFiltrados = viewModel.listaMoradores.filter { morador ->
        val termo = viewModel.consulta.trim()
        termo.isBlank() ||
            morador.nome.contains(termo, ignoreCase = true) ||
            morador.email.contains(termo, ignoreCase = true) ||
            morador.apartamento.contains(termo, ignoreCase = true) ||
            morador.bloco.contains(termo, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader(
            title = "Cadastro de Moradores",
            subtitle = "Cadastre moradores no Firebase Auth e libere o primeiro acesso.",
            onBack = onBack
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Dados do Morador", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("O e-mail e a senha serao usados no login do morador.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(value = viewModel.nome, onValueChange = { viewModel.nome = it }, label = { Text("Nome completo") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.senha, onValueChange = { viewModel.senha = it }, label = { Text("Senha inicial") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true, visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(value = viewModel.confirmarSenha, onValueChange = { viewModel.confirmarSenha = it }, label = { Text("Confirmar senha") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true, visualTransformation = PasswordVisualTransformation())

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = viewModel.bloco, onValueChange = { viewModel.bloco = it }, label = { Text("Bloco") }, modifier = Modifier.weight(1f), colors = crudOutlinedTextFieldColors(), singleLine = true)
                    OutlinedTextField(value = viewModel.apartamento, onValueChange = { viewModel.apartamento = it }, label = { Text("Apartamento") }, modifier = Modifier.weight(1f), colors = crudOutlinedTextFieldColors(), singleLine = true)
                }

                OutlinedTextField(value = viewModel.telefone, onValueChange = { viewModel.telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)

                if (viewModel.mensagemErro.isNotBlank()) {
                    Text(viewModel.mensagemErro, color = CrudDesign.danger, style = MaterialTheme.typography.bodySmall)
                }
                if (viewModel.mensagemSucesso.isNotBlank()) {
                    Text(viewModel.mensagemSucesso, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = viewModel::gravar,
                    enabled = !viewModel.carregando,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text(
                        if (viewModel.carregando) "CADASTRANDO..." else "CADASTRAR MORADOR",
                        color = CrudDesign.textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = viewModel::limparCampos,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("LIMPAR CAMPOS")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { consultaExpandida = !consultaExpandida },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Consulta de Moradores", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                        Text(
                            "${viewModel.listaMoradores.size} morador(es) carregado(s). Abra somente quando precisar consultar.",
                            color = CrudDesign.textSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(
                        onClick = { consultaExpandida = !consultaExpandida },
                        colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                    ) {
                        Text(if (consultaExpandida) "RECOLHER" else "ABRIR CONSULTA")
                    }
                }

                if (consultaExpandida) {
                    Text("Os dados abaixo sao carregados de /usuarios e /veiculos no Firebase.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)

                    OutlinedTextField(
                        value = viewModel.consulta,
                        onValueChange = { viewModel.consulta = it },
                        label = { Text("Buscar por nome, e-mail, bloco ou apto") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = crudOutlinedTextFieldColors(),
                        singleLine = true
                    )

                    if (moradoresFiltrados.isEmpty()) {
                        InfoCard(if (viewModel.listaMoradores.isEmpty()) "Nenhum morador cadastrado no Firebase." else "Nenhum morador encontrado para a consulta.")
                    } else {
                        moradoresFiltrados.forEach { morador ->
                            val veiculos = viewModel.veiculosDoMorador(morador)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
                                shape = CrudDesign.cardShape,
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(morador.nome, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("${morador.email} - Bloco ${morador.bloco}, Apto ${morador.apartamento}", color = CrudDesign.textSecondary)
                                    Text("Telefone: ${morador.telefone}", color = CrudDesign.textSecondary)
                                    Spacer(Modifier.height(4.dp))
                                    Text("Veiculo(s)", color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                                    if (veiculos.isEmpty()) {
                                        Text("Nenhum veiculo vinculado.", color = CrudDesign.textSecondary)
                                    } else {
                                        veiculos.forEach { veiculo ->
                                            VehicleLine(veiculo)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun VehicleLine(veiculo: Veiculo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                "${veiculo.modelo.ifBlank { "Modelo nao informado" }} - ${veiculo.placa.ifBlank { "Sem placa" }}",
                color = CrudDesign.textPrimary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Cor: ${veiculo.cor.ifBlank { "nao informada" }} - Status: ${if (veiculo.ativo) "Ativo" else "Inativo"}",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun InfoCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(text, color = CrudDesign.textSecondary, modifier = Modifier.padding(14.dp))
    }
}
