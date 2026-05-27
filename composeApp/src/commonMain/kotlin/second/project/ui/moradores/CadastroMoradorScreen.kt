package second.project.ui.moradores

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors

private data class MoradorCadastro(
    val nome: String,
    val email: String,
    val bloco: String,
    val apartamento: String,
    val telefone: String,
    val veiculo: String,
    val placa: String,
    val reservas: String,
    val comunicados: String,
    val prestadores: String
)

@Composable
fun CadastroMoradorScreen(onBack: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bloco by remember { mutableStateOf("") }
    var apartamento by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var consulta by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }
    val moradores = remember { mutableStateListOf<MoradorCadastro>() }
    val moradoresFiltrados = moradores.filter { morador ->
        val termo = consulta.trim()
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
            subtitle = "Cadastre moradores e consulte os dados vinculados a cada unidade.",
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
                Text("Preencha os dados basicos da unidade para liberar o acesso do morador.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome completo") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = bloco, onValueChange = { bloco = it }, label = { Text("Bloco") }, modifier = Modifier.weight(1f), colors = crudOutlinedTextFieldColors(), singleLine = true)
                    OutlinedTextField(value = apartamento, onValueChange = { apartamento = it }, label = { Text("Apartamento") }, modifier = Modifier.weight(1f), colors = crudOutlinedTextFieldColors(), singleLine = true)
                }

                OutlinedTextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)

                Button(
                    onClick = {
                        if (nome.isBlank() || email.isBlank() || telefone.isBlank() || bloco.isBlank() || apartamento.isBlank()) {
                            mensagem = "Preencha nome, e-mail, telefone, bloco e apartamento."
                            return@Button
                        }

                        moradores.add(
                            MoradorCadastro(
                                nome = nome,
                                email = email,
                                bloco = bloco,
                                apartamento = apartamento,
                                telefone = telefone,
                                veiculo = "Nenhum veiculo vinculado",
                                placa = "Sem placa",
                                reservas = "Nenhuma reserva vinculada",
                                comunicados = "Nenhum comunicado individual",
                                prestadores = "Nenhum prestador vinculado"
                            )
                        )
                        nome = ""
                        email = ""
                        bloco = ""
                        apartamento = ""
                        telefone = ""
                        mensagem = "Morador cadastrado para primeiro acesso."
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text("CADASTRAR MORADOR", color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        nome = ""
                        email = ""
                        bloco = ""
                        apartamento = ""
                        telefone = ""
                        mensagem = ""
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("LIMPAR CAMPOS")
                }

                if (mensagem.isNotBlank()) {
                    Text(mensagem, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
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
                Text("Consulta de Moradores", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("Busque um morador para visualizar os dados da unidade e vínculos cadastrados.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(
                    value = consulta,
                    onValueChange = { consulta = it },
                    label = { Text("Buscar por nome, e-mail, bloco ou apto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )

                if (moradoresFiltrados.isEmpty()) {
                    InfoCard(if (moradores.isEmpty()) "Nenhum morador cadastrado nesta sessao." else "Nenhum morador encontrado para a consulta.")
                } else {
                    moradoresFiltrados.forEach { morador ->
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
                                Text("Veiculo: ${morador.veiculo} / Placa: ${morador.placa}", color = CrudDesign.textSecondary)
                                Text("Reservas: ${morador.reservas}", color = CrudDesign.textSecondary)
                                Text("Comunicados: ${morador.comunicados}", color = CrudDesign.textSecondary)
                                Text("Prestadores: ${morador.prestadores}", color = CrudDesign.textSecondary)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
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
