package second.project.ui.convidados

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
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun FormularioConvidadoScreen(viewModel: ConvidadoViewModel, onSaved: () -> Unit, onBack: () -> Unit) {
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
            title = "Gestão de Convidados",
            subtitle = "Cadastre ou atualize os convidados do condomínio.",
            onBack = onBack
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Dados do Convidado", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("Registre os convidados com os dados essenciais.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.nome,
                    onValueChange = { viewModel.nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.telefone,
                    onValueChange = { viewModel.telefone = it },
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.localAlugado,
                    onValueChange = { viewModel.localAlugado = it },
                    label = { Text("Local Alugado") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )

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
