package second.project.model

data class ChatConversation(
    val residentUid: String,
    val residentName: String,
    val residentEmail: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L,
    val messageCount: Int = 0,
    val lastAuthorRole: String = UserRole.MORADOR.value,
    val hasAdminResponse: Boolean = false,
    val lastAnswerTimestamp: Long = 0L,
    val answeredMessageCount: Int = 0
) {
    val isPending: Boolean
        get() = lastAuthorRole != UserRole.ADMIN.value
}
