package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val residentName: String = "",
    val author: String = "",
    val authorUid: String = "",
    val authorRole: String = UserRole.MORADOR.value,
    val text: String = "",
    val timestamp: Long = 0L
)
