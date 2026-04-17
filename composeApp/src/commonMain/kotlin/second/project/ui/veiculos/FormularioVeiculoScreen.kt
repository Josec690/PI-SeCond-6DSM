package second.project.ui.veiculos

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.VeiculoViewModel

@Composable
fun FormularioVeiculoScreen(viewModel: VeiculoViewModel, onSaved: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Gestão de Veículos", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Cadastre ou atualize os veículos do condomínio.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

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
                    onValueChange = { viewModel.proprietario = it },
                    label = { Text("Proprietário") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )

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