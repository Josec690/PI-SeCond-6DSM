package second.project.ui.veiculos

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
import second.project.viewmodel.VeiculoViewModel

@Composable
fun ListaVeiculosScreen(viewModel: VeiculoViewModel, onAddClick: () -> Unit) {
    Scaffold(
        backgroundColor = Color.Black,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.limparCampos(); onAddClick() }, backgroundColor = Color(0xFF4A3B8B)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Veículos Cadastrados", color = Color.White, style = MaterialTheme.typography.h5)
            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(viewModel.listaVeiculos) { veiculo ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), backgroundColor = Color(0xFF121212)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(veiculo.placa, color = Color.White, style = MaterialTheme.typography.h6)
                                Text("${veiculo.modelo} (${veiculo.cor})", color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.editar(veiculo); onAddClick() }) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                            }
                            IconButton(onClick = { viewModel.apagar(veiculo.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFCF6679))
                            }
                        }
                    }
                }
            }
        }
    }
}