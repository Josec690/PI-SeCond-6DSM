package second.project.ui.avisos

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.viewmodel.AvisoViewModel

@Composable
fun ListaAvisosScreen(viewModel: AvisoViewModel, onAddClick: () -> Unit) {
    val total = viewModel.listaAvisos.size
    val urgentes = viewModel.listaAvisos.count { it.prioridade }
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = CrudDesign.page,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.limparCampos(); onAddClick() },
                containerColor = CrudDesign.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = CrudDesign.textPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text("Mural da Comunidade", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("Fique atualizado com os últimos avisos do SeCond.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                QuickCounter("Total", total.toString(), Modifier.weight(1f))
                QuickCounter("Urgentes", urgentes.toString(), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CrudDesign.primary.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip("Todos", true, Modifier.weight(1f))
                FilterChip("Urgente", false, Modifier.weight(1f))
                FilterChip("Eventos", false, Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.listaAvisos) { aviso ->
                    val urgente = aviso.prioridade
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
                        border = BorderStroke(1.dp, if (urgente) CrudDesign.danger.copy(alpha = 0.45f) else CrudDesign.primary.copy(alpha = 0.25f)),
                        shape = CrudDesign.cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(aviso.titulo, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium)
                                    Text("${aviso.categoria} - ${aviso.autor}", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                                }
                                AlertChip(if (urgente) "Urgente" else "Normal", urgente)
                            }

                            Text(
                                text = aviso.mensagem,
                                color = CrudDesign.textSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Postado: ${aviso.dataPublicacao}",
                                    color = CrudDesign.textSecondary.copy(alpha = 0.85f),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { viewModel.editar(aviso); onAddClick() }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar aviso",
                                        tint = CrudDesign.textPrimary,
                                        modifier = Modifier.padding(1.dp)
                                    )
                                }
                                IconButton(onClick = { viewModel.apagar(aviso.id) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Apagar aviso",
                                        tint = CrudDesign.danger,
                                        modifier = Modifier.padding(1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickCounter(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        border = BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.25f)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(label, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            Text(value, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FilterChip(text: String, active: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (active) CrudDesign.primary.copy(alpha = 0.45f) else CrudDesign.surfaceAlt
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text,
            color = if (active) CrudDesign.textPrimary else CrudDesign.textSecondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun AlertChip(text: String, urgent: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (urgent) CrudDesign.danger.copy(alpha = 0.2f) else CrudDesign.primary.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(999.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            color = CrudDesign.textPrimary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}



