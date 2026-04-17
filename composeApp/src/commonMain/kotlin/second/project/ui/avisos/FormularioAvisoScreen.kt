package second.project.ui.avisos

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
import second.project.viewmodel.AvisoViewModel

@Composable
fun FormularioAvisoScreen(viewModel: AvisoViewModel, onSaved: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Mural de Avisos", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Comunique avisos importantes para os moradores.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
            shape = CrudDesign.cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Dados do Aviso", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge)
                Text("Mensagens mais claras aumentam o engajamento no mural.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = viewModel.titulo,
                    onValueChange = { viewModel.titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.mensagem,
                    onValueChange = { viewModel.mensagem = it },
                    label = { Text("Mensagem") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    minLines = 3
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.categoria,
                    onValueChange = { viewModel.categoria = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.autor,
                    onValueChange = { viewModel.autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CrudDesign.fieldShape,
                    colors = crudOutlinedTextFieldColors(),
                    singleLine = true
                )
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.dataPublicacao,
                    onValueChange = { viewModel.dataPublicacao = it },
                    label = { Text("Data de Publicação") },
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
                            checked = viewModel.prioridade,
                            onCheckedChange = { viewModel.prioridade = it }
                        )
                        Column(Modifier.padding(top = 6.dp)) {
                            Text("Aviso urgente", color = CrudDesign.textPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(
                                if (viewModel.prioridade) "Prioridade atual: Urgente" else "Prioridade atual: Normal",
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


