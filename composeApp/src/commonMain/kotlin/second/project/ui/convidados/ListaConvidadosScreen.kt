package second.project.ui.convidados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.dp
import second.project.ui.components.CrudDesign
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun ListaConvidadosScreen(viewModel: ConvidadoViewModel, onAddClick: () -> Unit) {
    val total = viewModel.listaConvidados.size
    val ativos = viewModel.listaConvidados.count { it.ativo }
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = CrudDesign.page,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.limparCampos(); onAddClick() },
                containerColor = CrudDesign.primary
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = CrudDesign.textPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text("Convidados", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("Gerencie os convidados cadastrados.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryBadge("Total", total.toString(), Modifier.weight(1f))
                SummaryBadge("Ativos", ativos.toString(), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CrudDesign.primary.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTab("Ativos", true, Modifier.weight(1f))
                StatusTab("Inativos", false, Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.listaConvidados) { convidado ->
                    val ativo = convidado.ativo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
                        border = BorderStroke(1.dp, if (ativo) CrudDesign.primary.copy(alpha = 0.25f) else CrudDesign.danger.copy(alpha = 0.45f)),
                        shape = CrudDesign.cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(convidado.nome, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium)
                                    Text("Telefone: ${convidado.telefone}", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                                }
                                ChipStatus(if (ativo) "Ativo" else "Inativo", ativo)
                            }

                            Text(
                                "Local: ${convidado.localAlugado}",
                                color = CrudDesign.textSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.weight(1f))
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
}

@Composable
private fun SummaryBadge(label: String, value: String, modifier: Modifier = Modifier) {
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
private fun StatusTab(text: String, selected: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) CrudDesign.primary.copy(alpha = 0.45f) else CrudDesign.surfaceAlt
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            color = if (selected) CrudDesign.textPrimary else CrudDesign.textSecondary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun ChipStatus(text: String, positive: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (positive) CrudDesign.primary.copy(alpha = 0.2f) else CrudDesign.danger.copy(alpha = 0.2f)
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
