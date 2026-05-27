package second.project.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import second.project.model.UserRole
import second.project.ui.components.CrudDesign
import second.project.ui.components.ScreenHeader
import second.project.ui.components.crudOutlinedTextFieldColors

private data class ChatMessage(val author: String, val text: String, val fromCurrentUser: Boolean)

@Composable
fun ChatPortariaScreen(userRole: UserRole, onBack: () -> Unit) {
    val currentAuthor = if (userRole == UserRole.ADMIN) "Portaria" else "Morador"
    var message by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Portaria", "Bom dia. Como podemos ajudar?", userRole == UserRole.ADMIN)
        )
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
            subtitle = if (userRole == UserRole.ADMIN) "Atenda os moradores pelo canal interno." else "Envie mensagens diretamente para a portaria.",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (item.fromCurrentUser) Arrangement.End else Arrangement.Start
                ) {
                    MessageBubble(item)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensagem") },
                modifier = Modifier.weight(1f),
                shape = CrudDesign.fieldShape,
                colors = crudOutlinedTextFieldColors()
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = {
                    val cleanMessage = message.trim()
                    if (cleanMessage.isNotEmpty()) {
                        messages.add(ChatMessage(currentAuthor, cleanMessage, true))
                        message = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CrudDesign.primary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = CrudDesign.textPrimary)
            }
        }
    }
}

@Composable
private fun MessageBubble(item: ChatMessage) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (item.fromCurrentUser) CrudDesign.primary.copy(alpha = 0.24f) else CrudDesign.surface
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(item.author, color = CrudDesign.textSecondary, fontWeight = FontWeight.Bold)
            Text(item.text, color = CrudDesign.textPrimary)
        }
    }
}
