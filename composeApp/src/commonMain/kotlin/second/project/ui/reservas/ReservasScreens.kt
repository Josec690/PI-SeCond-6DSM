package second.project.ui.reservas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import second.project.model.UserRole
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.ReservaViewModel

@Composable
fun ListaReservasScreen(viewModel: ReservaViewModel, onAddClick: () -> Unit, onBack: () -> Unit, userRole: UserRole) {
    val isAdmin = userRole == UserRole.ADMIN
    LaunchedEffect(Unit) { viewModel.carregar() }

    val total = viewModel.listaReservas.size
    val confirmadas = viewModel.listaReservas.count { it.confirmada }

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
            ScreenHeader(
                title = "Reservas",
                subtitle = if (isAdmin) "Aprove ou acompanhe pedidos de reserva." else "Solicite areas comuns e acompanhe a aprovacao.",
                onBack = onBack
            )

            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryBadge("Total", total.toString(), Modifier.weight(1f))
                SummaryBadge("Confirmadas", confirmadas.toString(), Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            HeroScheduleCard()

            Spacer(Modifier.height(14.dp))

            Text("Áreas Reserváveis", color = CrudDesign.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Escolha uma área e siga para a reserva.", color = CrudDesign.textSecondary, fontSize = 12.sp)

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.listaReservas, key = { it.id }) { reserva ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.18f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = CrudDesign.primary)
                                Spacer(Modifier.size(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(reserva.area, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
                                    Text("Morador: ${reserva.morador}", color = CrudDesign.textSecondary)
                                }
                                Card(
                                    modifier = if (isAdmin) Modifier.clickable { viewModel.alternarStatus(reserva) } else Modifier,
                                    colors = CardDefaults.cardColors(containerColor = if (reserva.confirmada) CrudDesign.primary.copy(alpha = 0.2f) else CrudDesign.danger.copy(alpha = 0.2f)),
                                    shape = CrudDesign.cardShape,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Text(
                                        if (reserva.confirmada) "Confirmada" else "Pendente",
                                        color = CrudDesign.textPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Text("Data: ${reserva.dataReserva} • Horário: ${reserva.horario}", color = CrudDesign.textSecondary)
                            if (reserva.observacoes.isNotBlank()) {
                                Text("Obs.: ${reserva.observacoes}", color = CrudDesign.textSecondary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(Modifier.weight(1f))
                                if (isAdmin) {
                                    IconButton(onClick = { viewModel.editar(reserva); onAddClick() }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar reserva", tint = CrudDesign.textPrimary)
                                    }
                                    IconButton(onClick = { viewModel.apagar(reserva.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Apagar reserva", tint = CrudDesign.danger)
                                    }
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
fun FormularioReservaScreen(viewModel: ReservaViewModel, onSaved: () -> Unit, onBack: () -> Unit, userRole: UserRole) {
    val isAdmin = userRole == UserRole.ADMIN
    Column(
        Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader(
            title = "Gestão de Reservas",
            subtitle = if (isAdmin) "Cadastre, atualize ou aprove uma reserva." else "Solicite uma reserva para aprovacao da administracao.",
            onBack = onBack
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CrudDesign.surface.copy(alpha = 0.95f)),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CrudDesign.primary.copy(alpha = 0.16f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = viewModel.area, onValueChange = { viewModel.area = it }, label = { Text("Área") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.morador, onValueChange = { viewModel.morador = it }, label = { Text("Morador") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.dataReserva, onValueChange = { viewModel.dataReserva = it }, label = { Text("Data") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.horario, onValueChange = { viewModel.horario = it }, label = { Text("Horário") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), singleLine = true)
                OutlinedTextField(value = viewModel.observacoes, onValueChange = { viewModel.observacoes = it }, label = { Text("Observações") }, modifier = Modifier.fillMaxWidth(), colors = crudOutlinedTextFieldColors(), minLines = 3)

                if (isAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Confirmada", color = CrudDesign.textPrimary)
                        Spacer(Modifier.weight(1f))
                        Switch(checked = viewModel.confirmada, onCheckedChange = { viewModel.confirmada = it })
                    }
                } else {
                    Text("A reserva sera enviada como pendente para aprovacao.", color = CrudDesign.textSecondary)
                }

                Button(
                    onClick = {
                        if (!isAdmin) viewModel.confirmada = false
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

@Composable
private fun HeroScheduleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(Color(0xFF4A3B8B), Color(0xFF1C045D), Color(0xFF0A0A0A)))
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Selecionar Data", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Explore o calendário, horários disponíveis e áreas comuns.", color = Color(0xFFE0DBFF), fontSize = 12.sp)

                Spacer(Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TimePill(text = "08:00", highlighted = true)
                    TimePill(text = "12:00")
                    TimePill(text = "16:00", disabled = true)
                    TimePill(text = "20:00")
                }
            }
        }
    }
}

@Composable
private fun TimePill(text: String, highlighted: Boolean = false, disabled: Boolean = false) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when {
                highlighted -> Color.White.copy(alpha = 0.18f)
                disabled -> Color.White.copy(alpha = 0.06f)
                else -> Color.White.copy(alpha = 0.12f)
            }
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            color = if (disabled) Color.White.copy(alpha = 0.45f) else Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}





