package second.project.ui.veiculos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import second.project.viewmodel.VeiculoViewModel

@Composable
fun ListaVeiculosScreen(viewModel: VeiculoViewModel, onAddClick: () -> Unit) {
    val total = viewModel.listaVeiculos.size
    val ativos = viewModel.listaVeiculos.count { it.ativo }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val listaFiltrada by remember(viewModel.listaVeiculos, selectedTab) {
        androidx.compose.runtime.derivedStateOf {
            when (selectedTab) {
                0 -> viewModel.listaVeiculos.filter { it.ativo }
                else -> viewModel.listaVeiculos.filter { !it.ativo }
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
            Text("Veículos", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text("Gerencie os veículos cadastrados no condomínio.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryBadge("Total", total.toString(), Modifier.weight(1f))
                SummaryBadge("Ativos", ativos.toString(), Modifier.weight(1f))
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
                    text = { Text("Ativos", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Inativos", fontWeight = FontWeight.Bold) }
                )
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaFiltrada) { veiculo ->
                    val ativo = veiculo.ativo
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
                                    Text(veiculo.placa, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium)
                                    Text("${veiculo.modelo} (${veiculo.cor})", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
                                }
                                ChipStatus(
                                    text = if (ativo) "Ativo" else "Inativo",
                                    positive = ativo,
                                    onClick = { viewModel.alternarStatus(veiculo) }
                                )
                            }

                            Text(
                                "Proprietário: ${veiculo.proprietario}",
                                color = CrudDesign.textSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { viewModel.editar(veiculo); onAddClick() }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar veículo",
                                        tint = CrudDesign.textPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                IconButton(onClick = { viewModel.apagar(veiculo.id) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Apagar veículo",
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
private fun ChipStatus(text: String, positive: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
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
