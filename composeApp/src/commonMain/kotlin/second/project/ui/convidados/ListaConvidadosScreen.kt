package second.project.ui.convidados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun ListaConvidadosScreen(viewModel: ConvidadoViewModel, onAddClick: () -> Unit) {
    Scaffold(
        backgroundColor = CrudDesign.page,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.limparCampos(); onAddClick() },
                backgroundColor = CrudDesign.primary
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = CrudDesign.textPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text("Lista de Convidados", color = CrudDesign.textPrimary, style = MaterialTheme.typography.h5)
            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.listaConvidados) { convidado ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = CrudDesign.surface,
                        shape = CrudDesign.cardShape,
                        elevation = 6.dp
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(convidado.nome, color = CrudDesign.textPrimary, style = MaterialTheme.typography.h6)
                                Spacer(Modifier.height(2.dp))
                                Text("Telefone: ${convidado.telefone}", color = CrudDesign.textSecondary)
                                Text("Local: ${convidado.localAlugado}", color = CrudDesign.textSecondary.copy(alpha = 0.8f))
                            }
                            IconButton(onClick = { viewModel.editar(convidado); onAddClick() }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar convidado",
                                    tint = CrudDesign.textPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            IconButton(onClick = { viewModel.apagar(convidado.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Apagar convidado",
                                    tint = CrudDesign.danger,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}