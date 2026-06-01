package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import second.project.model.ChatConversation
import second.project.model.ChatMessage
import second.project.model.UserRole
import second.project.repository.AuthSessionManager
import second.project.repository.RepositorioRemoto
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ChatPortariaViewModel(private val repo: RepositorioRemoto) {
    var texto by mutableStateOf("")
    var mensagemErro by mutableStateOf("")
        private set
    var carregando by mutableStateOf(false)
        private set
    var enviando by mutableStateOf(false)
        private set
    var conversaSelecionada by mutableStateOf<ChatConversation?>(null)
        private set

    val conversas = mutableStateListOf<ChatConversation>()
    val historicoConversas = mutableStateListOf<ChatConversation>()
    val mensagens = mutableStateListOf<ChatMessage>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun carregar(userRole: UserRole) {
        if (userRole == UserRole.ADMIN) {
            carregarConversas()
        } else {
            carregarConversaMorador()
        }
    }

    fun selecionarConversa(conversa: ChatConversation) {
        conversaSelecionada = conversa
        carregarMensagens(conversa.residentUid)
    }

    fun limparSelecao() {
        conversaSelecionada = null
        mensagens.clear()
        texto = ""
        mensagemErro = ""
    }

    private fun carregarConversas() {
        scope.launch {
            carregando = true
            mensagemErro = ""
            runCatching {
                repo.listarConversasChat()
            }.onSuccess { dados ->
                val pendentes = dados
                    .filter { it.isPending }
                    .sortedByDescending { it.lastTimestamp }
                val historico = dados
                    .filter { it.hasAdminResponse }
                    .sortedByDescending { it.lastAnswerTimestamp }

                conversas.clear()
                conversas.addAll(pendentes)
                historicoConversas.clear()
                historicoConversas.addAll(historico)

                val selected = conversaSelecionada
                if (selected != null) {
                    conversaSelecionada = dados.firstOrNull { it.residentUid == selected.residentUid }
                    conversaSelecionada?.let { carregarMensagens(it.residentUid) }
                }
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel carregar conversas."
            }
            carregando = false
        }
    }

    private fun carregarConversaMorador() {
        val session = AuthSessionManager.currentSession
        if (session == null) {
            mensagemErro = "Sessao expirada. Faca login novamente."
            return
        }

        conversaSelecionada = ChatConversation(
            residentUid = session.uid,
            residentName = session.perfil.nome.ifBlank { "Morador" },
            residentEmail = session.email
        )
        carregarMensagens(session.uid)
    }

    private fun carregarMensagens(conversationId: String) {
        scope.launch {
            carregando = true
            mensagemErro = ""
            runCatching {
                repo.listarMensagensChat(conversationId)
            }.onSuccess { dados ->
                mensagens.clear()
                mensagens.addAll(dados)
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel carregar mensagens."
            }
            carregando = false
        }
    }

    @OptIn(ExperimentalTime::class)
    fun enviar(userRole: UserRole) {
        val cleanText = texto.trim()
        if (cleanText.isBlank()) return

        scope.launch {
            enviando = true
            mensagemErro = ""
            runCatching {
                val session = AuthSessionManager.currentSession
                    ?: throw IllegalStateException("Sessao expirada. Faca login novamente.")
                val conversation = if (userRole == UserRole.ADMIN) {
                    conversaSelecionada ?: throw IllegalStateException("Selecione um morador para responder.")
                } else {
                    ChatConversation(
                        residentUid = session.uid,
                        residentName = session.perfil.nome.ifBlank { "Morador" },
                        residentEmail = session.email
                    )
                }
                val author = if (userRole == UserRole.ADMIN) "Portaria" else conversation.residentName

                repo.salvarMensagemChat(
                    conversationId = conversation.residentUid,
                    mensagem = ChatMessage(
                        conversationId = conversation.residentUid,
                        residentName = conversation.residentName,
                        author = author,
                        authorUid = session.uid,
                        authorRole = userRole.value,
                        text = cleanText,
                        timestamp = Clock.System.now().toEpochMilliseconds()
                    )
                )
            }.onSuccess {
                texto = ""
                if (userRole == UserRole.ADMIN) {
                    carregarConversas()
                } else {
                    carregarConversaMorador()
                }
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel enviar mensagem."
            }
            enviando = false
        }
    }

    fun isCurrentUser(message: ChatMessage): Boolean {
        return message.authorUid == AuthSessionManager.currentSession?.uid
    }
}
