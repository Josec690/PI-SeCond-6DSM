package second.project.ui.encomendas

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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import second.project.ui.components.CrudDesign
import second.project.viewmodel.EncomendaViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState

@Composable
fun ListaEncomendasScreen(viewModel: EncomendaViewModel, onAddClick: () -> Unit) {
    val total = viewModel.listaEncomendas.size
    val pendentes = viewModel.listaEncomendas.count { !it.statusRetirada }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val listaFiltrada by remember(viewModel.listaEncomendas, selectedTab) {
        androidx.compose.runtime.derivedStateOf {
            when (selectedTab) {
                0 -> viewModel.listaEncomendas.filter { !it.statusRetirada }
                else -> viewModel.listaEncomendas.filter { it.statusRetirada }
            }
        }
    }

    Scaffold(
        containerColor = CrudDesign.page,
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.End) {
                if (listState.firstVisibleItemIndex > 0) {
                    FloatingActionButton(
                        onClick = { scope.launch { listState.animateScrollToItem(0) } },
                        containerColor = CrudDesign.surfaceAlt
                    ) {
                        Text("↑", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium)
                    }
                }
                FloatingActionButton(
                    onClick = { viewModel.limparCampos(); onAddClick() },
                    containerColor = CrudDesign.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = CrudDesign.textPrimary)
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text("Encomendas", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("Gerencie suas entregas recebidas.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryBadge("Total", total.toString(), Modifier.weight(1f))
                SummaryBadge("Pendentes", pendentes.toString(), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CrudDesign.surfaceAlt,
                contentColor = CrudDesign.textPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Pendentes", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Recebidas", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaFiltrada) { encomenda ->
                    val retirada = encomenda.statusRetirada
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
                        border = BorderStroke(1.dp, if (retirada) CrudDesign.primary.copy(alpha = 0.2f) else CrudDesign.primary.copy(alpha = 0.5f)),
                        shape = CrudDesign.cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(encomenda.destinatarioNome, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium)
                                    Text("Pedido: ${encomenda.codigoRastreio}", color = CrudDesign.textSecondary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                                }
                                ChipStatus(if (retirada) "Retirada" else "Pendente", retirada)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                MiniInfo("Transportadora", encomenda.transportadora, Modifier.weight(1f))
                                MiniInfo("Recebido", encomenda.dataRecebimento, Modifier.weight(1f))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val local = buildString {
                                    append("Apto ${encomenda.apartamento}")
                                    if (encomenda.blocoTorre.isNotBlank()) append(" - Bloco/Torre ${encomenda.blocoTorre}")
                                }
                                Text(local, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                IconButton(onClick = { viewModel.editar(encomenda); onAddClick() }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar encomenda", tint = CrudDesign.textPrimary)
                                }
                                IconButton(onClick = { viewModel.apagar(encomenda.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Apagar encomenda", tint = CrudDesign.danger)
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
private fun MiniInfo(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.primary.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(label, color = CrudDesign.textSecondary.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall)
            Text(value.ifBlank { "-" }, color = CrudDesign.textPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
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

