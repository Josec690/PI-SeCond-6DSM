package second.project.ui.prestadores

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import second.project.ui.components.CrudDesign
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.PrestadorViewModel

@Composable
fun ListaPrestadoresScreen(viewModel: PrestadorViewModel, onAddClick: () -> Unit) {
    LaunchedEffect(Unit) { viewModel.carregar() }

    val total = viewModel.listaPrestadores.size
    val autorizados = viewModel.listaPrestadores.count { it.autorizado }

    Scaffold(
        containerColor = CrudDesign.page,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.limparCampos()
                    onAddClick()
                },
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
            Text("Prestadores de Serviço", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
            Text("Gerencie e autorize manutenções e entregas agendadas.", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryBadge("Total", total.toString(), Modifier.weight(1f))
                SummaryBadge("Autorizados", autorizados.toString(), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            NextServiceBanner()

            Spacer(Modifier.height(14.dp))

            Text("Serviços Agendados", color = CrudDesign.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Acompanhe a programação dos prestadores na unidade.", color = CrudDesign.textSecondary, fontSize = 12.sp)

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.listaPrestadores, key = { it.id }) { prestador ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.18f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = CrudDesign.primary)
                                Spacer(Modifier.size(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(prestador.nome, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                                    Text(prestador.empresa, color = CrudDesign.textSecondary)
                                }
                                Card(
                                    modifier = Modifier.clickable { viewModel.alternarStatus(prestador) },
                                    colors = CardDefaults.cardColors(containerColor = if (prestador.autorizado) CrudDesign.primary.copy(alpha = 0.2f) else CrudDesign.danger.copy(alpha = 0.2f)),
                                    shape = CrudDesign.cardShape,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Text(
                                        if (prestador.autorizado) "Autorizado" else "Pendente",
                                        color = CrudDesign.textPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Text("Telefone: ${prestador.telefone}", color = CrudDesign.textSecondary)
                            Text("Serviço: ${prestador.servico}", color = CrudDesign.textSecondary)
                            if (prestador.dataVisita.isNotBlank()) {
                                Text("Visita: ${prestador.dataVisita}", color = CrudDesign.textSecondary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = { viewModel.editar(prestador); onAddClick() }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar prestador", tint = CrudDesign.textPrimary)
                                }
                                IconButton(onClick = { viewModel.apagar(prestador.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Apagar prestador", tint = CrudDesign.danger)
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
fun FormularioPrestadorScreen(viewModel: PrestadorViewModel, onSaved: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Gestão de Prestadores", color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Text("Cadastre ou atualize um prestador de serviço.", color = CrudDesign.textSecondary)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.16f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = viewModel.nome, onValueChange = { viewModel.nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.empresa, onValueChange = { viewModel.empresa = it }, label = { Text("Empresa") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.telefone, onValueChange = { viewModel.telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.servico, onValueChange = { viewModel.servico = it }, label = { Text("Serviço") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.dataVisita, onValueChange = { viewModel.dataVisita = it }, label = { Text("Data da visita") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Autorizado", color = CrudDesign.textPrimary)
                    Spacer(Modifier.weight(1f))
                    Switch(checked = viewModel.autorizado, onCheckedChange = { viewModel.autorizado = it })
                }

                Button(
                    onClick = {
                        viewModel.gravar()
                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary)
                ) {
                    Text("GRAVAR")
                }

                OutlinedButton(
                    onClick = viewModel::limparCampos,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("LIMPAR CAMPOS")
                }
            }
        }
    }
}

@Composable
private fun NextServiceBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF4A3B8B), Color(0xFF1C045D), Color(0xFF0A0A0A))))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Próximo Serviço", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("AC Maintenance", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text("Hoje às 14:30 • Apt 402", color = Color(0xFFE0DBFF), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SummaryBadge(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(label, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
            Text(value, color = CrudDesign.textPrimary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}





