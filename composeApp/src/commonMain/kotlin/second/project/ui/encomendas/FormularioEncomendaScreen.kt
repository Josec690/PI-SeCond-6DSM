package second.project.ui.encomendas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.EncomendaViewModel

@Composable
fun FormularioEncomendaScreen(viewModel: EncomendaViewModel, onSaved: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Gestão de Encomendas", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Cadastre ou atualize entregas do condomínio.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Dados da Encomenda", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("Use os dados do pedido para facilitar a retirada.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.destinatarioNome,
                    onValueChange = { viewModel.destinatarioNome = it },
                    label = { Text("Destinatário") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.apartamento,
                    onValueChange = { viewModel.apartamento = it },
                    label = { Text("Apartamento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.blocoTorre,
                    onValueChange = { viewModel.blocoTorre = it },
                    label = { Text("Bloco/Torre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.codigoRastreio,
                    onValueChange = { viewModel.codigoRastreio = it },
                    label = { Text("Código de Rastreio") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.transportadora,
                    onValueChange = { viewModel.transportadora = it },
                    label = { Text("Transportadora") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.dataRecebimento,
                    onValueChange = { viewModel.dataRecebimento = it },
                    label = { Text("Data de Recebimento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = CrudDesign.primary.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                        Checkbox(
                            checked = viewModel.statusRetirada,
                            onCheckedChange = { viewModel.statusRetirada = it }
                        )
                        Column(Modifier.padding(top = 6.dp)) {
                            Text("Encomenda retirada", color = CrudDesign.textPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(
                                if (viewModel.statusRetirada) "Status atual: Retirada" else "Status atual: Pendente",
                                color = CrudDesign.textSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.gravar(); onSaved() },
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


