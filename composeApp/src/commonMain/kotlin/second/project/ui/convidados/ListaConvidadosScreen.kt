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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun ListaConvidadosScreen(viewModel: ConvidadoViewModel, onAddClick: () -> Unit) {
    Scaffold(
        backgroundColor = Color.Black,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.limparCampos(); onAddClick() }, backgroundColor = Color(0xFF4A3B8B)) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White)
            }
        }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Lista de Convidados", color = Color.White, style = MaterialTheme.typography.h5)
            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(viewModel.listaConvidados) { convidado ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), backgroundColor = Color(0xFF121212)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(convidado.nome, color = Color.White, style = MaterialTheme.typography.h6)
                                Text("Telefone: ${convidado.telefone}", color = Color.LightGray)
                                Text("Local: ${convidado.localAlugado}", color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.editar(convidado); onAddClick() }) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                            }
                            IconButton(onClick = { viewModel.apagar(convidado.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679))
                            }
                        }
                    }
                }
            }
        }
    }
}