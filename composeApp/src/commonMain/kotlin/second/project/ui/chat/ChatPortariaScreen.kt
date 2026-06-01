package second.project.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import second.project.model.ChatConversation
import second.project.model.ChatMessage
import second.project.model.UserRole
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors
import second.project.viewmodel.ChatPortariaViewModel

@Composable
fun ChatPortariaScreen(viewModel: ChatPortariaViewModel, userRole: UserRole, onBack: () -> Unit) {
    val isAdmin = userRole == UserRole.ADMIN

    LaunchedEffect(userRole) {
        viewModel.carregar(userRole)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CrudDesign.page)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader(
            title = "Chat com a Portaria",
            subtitle = if (isAdmin) "Selecione um morador para responder em uma conversa privada." else "Sua conversa com a portaria e privada.",
            onBack = onBack
        )

        if (viewModel.mensagemErro.isNotBlank()) {
            Text(viewModel.mensagemErro, color = CrudDesign.danger)
        }

        if (isAdmin) {
            ConversationsPanel(
                pendentes = viewModel.conversas,
                historico = viewModel.historicoConversas,
                selected = viewModel.conversaSelecionada,
                carregando = viewModel.carregando,
                onSelect = viewModel::selecionarConversa
            )
        }

        val selectedConversation = viewModel.conversaSelecionada
        if (isAdmin && selectedConversation == null) {
            EmptyConversationHint()
        } else {
            selectedConversation?.let {
                ConversationHeader(conversation = it, isAdmin = isAdmin)
            }
            MessagesList(
                messages = viewModel.mensagens,
                carregando = viewModel.carregando,
                isCurrentUser = viewModel::isCurrentUser,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            MessageInput(
                value = viewModel.texto,
                onValueChange = { viewModel.texto = it },
                enabled = !viewModel.enviando && (!isAdmin || selectedConversation != null),
                onSend = { viewModel.enviar(userRole) }
            )
        }
    }
}

@Composable
private fun ConversationsPanel(
    pendentes: List<ChatConversation>,
    historico: List<ChatConversation>,
    selected: ChatConversation?,
    carregando: Boolean,
    onSelect: (ChatConversation) -> Unit
) {
    var selectedTab by remember { mutableStateOf(ConversationTab.PENDENTES) }

    LaunchedEffect(pendentes.size, historico.size) {
        if (selectedTab == ConversationTab.PENDENTES && pendentes.isEmpty() && historico.isNotEmpty()) {
            selectedTab = ConversationTab.HISTORICO
        }
    }

    val activeList = if (selectedTab == ConversationTab.PENDENTES) pendentes else historico
    val emptyMessage = if (selectedTab == ConversationTab.PENDENTES) {
        "Nenhuma mensagem pendente no momento."
    } else {
        "Nenhuma conversa respondida ainda."
    }
    val activeSubtitle = if (selectedTab == ConversationTab.PENDENTES) {
        "Mensagens que ainda precisam de resposta da portaria."
    } else {
        "Conversas ja respondidas, separadas por morador."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surface),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Fila da portaria", color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ConversationTabChip(
                    title = "Pendentes",
                    count = pendentes.size,
                    selected = selectedTab == ConversationTab.PENDENTES,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = ConversationTab.PENDENTES }
                )
                ConversationTabChip(
                    title = "Historico",
                    count = historico.size,
                    selected = selectedTab == ConversationTab.HISTORICO,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedTab = ConversationTab.HISTORICO }
                )
            }

            Text(
                if (carregando) "Carregando conversas..." else activeSubtitle,
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(170.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (activeList.isEmpty() && !carregando) {
                    item { EmptySectionText(emptyMessage) }
                } else {
                    items(activeList, key = { "${selectedTab.name}-${it.residentUid}" }) { conversa ->
                        ConversationItem(
                            conversation = conversa,
                            selected = selected?.residentUid == conversa.residentUid,
                            status = conversationStatus(conversa, selectedTab),
                            onClick = { onSelect(conversa) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationTabChip(
    title: String,
    count: Int,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) CrudDesign.primary.copy(alpha = 0.35f) else CrudDesign.surfaceAlt
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(title, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
            Text("$count conversa(s)", color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun EmptySectionText(text: String) {
    Text(text, color = CrudDesign.textSecondary, style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun ConversationItem(
    conversation: ChatConversation,
    selected: Boolean,
    status: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) CrudDesign.primary.copy(alpha = 0.22f) else CrudDesign.surfaceAlt
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(conversation.residentName, color = CrudDesign.textPrimary, fontWeight = FontWeight.Bold)
            Text(
                conversation.lastMessage,
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "$status - ${formatChatTimestamp(conversation.lastTimestamp)}",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "${conversation.messageCount} mensagem(ns)",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun EmptyConversationHint() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.surfaceAlt),
        shape = CrudDesign.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            "Selecione uma conversa pendente ou do historico para ver as mensagens do morador.",
            color = CrudDesign.textSecondary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ConversationHeader(conversation: ChatConversation, isAdmin: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CrudDesign.primary.copy(alpha = 0.14f)),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            if (isAdmin) {
                val status = if (conversation.isPending) "pendente" else "historico respondido"
                "Conversa com ${conversation.residentName} - $status"
            } else {
                "Conversa privada com a portaria"
            },
            color = CrudDesign.textPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
private fun MessagesList(
    messages: List<ChatMessage>,
    carregando: Boolean,
    isCurrentUser: (ChatMessage) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (carregando) {
            item { Text("Carregando mensagens...", color = CrudDesign.textSecondary) }
        }
        if (messages.isEmpty() && !carregando) {
            item { Text("Nenhuma mensagem enviada ainda.", color = CrudDesign.textSecondary) }
        }
        items(messages, key = { it.id }) { item ->
            val fromCurrentUser = isCurrentUser(item)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (fromCurrentUser) Arrangement.End else Arrangement.Start
            ) {
                MessageBubble(item = item, fromCurrentUser = fromCurrentUser)
            }
        }
    }
}

@Composable
private fun MessageInput(value: String, onValueChange: (String) -> Unit, enabled: Boolean, onSend: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            label = { Text("Mensagem") },
            modifier = Modifier.weight(1f),
            shape = CrudDesign.fieldShape,
            colors = crudOutlinedTextFieldColors(),
            singleLine = false,
            minLines = 1,
            maxLines = 4
        )
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = onSend,
            enabled = enabled && value.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = CrudDesign.textPrimary)
        }
    }
}

@Composable
private fun MessageBubble(item: ChatMessage, fromCurrentUser: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (fromCurrentUser) CrudDesign.primary.copy(alpha = 0.24f) else CrudDesign.surface
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(item.author, color = CrudDesign.textSecondary, fontWeight = FontWeight.Bold)
            Text(item.text, color = CrudDesign.textPrimary)
            Text(
                "${if (fromCurrentUser) "Enviada" else "Recebida"} em ${formatChatTimestamp(item.timestamp)}",
                color = CrudDesign.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private enum class ConversationTab {
    PENDENTES,
    HISTORICO
}

private fun conversationStatus(conversation: ChatConversation, tab: ConversationTab): String {
    return if (tab == ConversationTab.PENDENTES) {
        "Aguardando resposta"
    } else {
        "${conversation.answeredMessageCount} resposta(s) da portaria"
    }
}

private fun formatChatTimestamp(timestamp: Long): String {
    if (timestamp <= 0L) return "data nao registrada"

    val brasiliaOffsetSeconds = -3 * 60 * 60
    val totalSeconds = timestamp / 1_000L + brasiliaOffsetSeconds
    val days = floorDiv(totalSeconds, 86_400L)
    val secondsOfDay = floorMod(totalSeconds, 86_400L)
    val (year, month, day) = civilFromDays(days)
    val hour = (secondsOfDay / 3_600L).toInt()
    val minute = ((secondsOfDay % 3_600L) / 60L).toInt()

    return "${day.twoDigits()}/${month.twoDigits()}/$year ${hour.twoDigits()}:${minute.twoDigits()}"
}

private fun civilFromDays(daysSinceEpoch: Long): Triple<Int, Int, Int> {
    var z = daysSinceEpoch + 719_468L
    val era = if (z >= 0) z / 146_097L else (z - 146_096L) / 146_097L
    val dayOfEra = z - era * 146_097L
    val yearOfEra = (dayOfEra - dayOfEra / 1_460L + dayOfEra / 36_524L - dayOfEra / 146_096L) / 365L
    var year = (yearOfEra + era * 400L).toInt()
    val dayOfYear = dayOfEra - (365L * yearOfEra + yearOfEra / 4L - yearOfEra / 100L)
    val monthPart = (5L * dayOfYear + 2L) / 153L
    val day = (dayOfYear - (153L * monthPart + 2L) / 5L + 1L).toInt()
    val month = (monthPart + if (monthPart < 10L) 3L else -9L).toInt()
    if (month <= 2) year += 1
    return Triple(year, month, day)
}

private fun floorDiv(value: Long, divisor: Long): Long {
    var result = value / divisor
    if ((value xor divisor) < 0 && result * divisor != value) result--
    return result
}

private fun floorMod(value: Long, divisor: Long): Long {
    return value - floorDiv(value, divisor) * divisor
}

private fun Int.twoDigits(): String {
    return if (this < 10) "0$this" else toString()
}
