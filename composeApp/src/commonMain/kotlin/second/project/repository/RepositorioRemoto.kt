package second.project.repository

import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.contentType
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.Serializable
import second.project.model.Aviso
import second.project.model.ChatConversation
import second.project.model.ChatMessage
import second.project.model.Convidado
import second.project.model.Documento
import second.project.model.Encomenda
import second.project.model.Prestador
import second.project.model.Reserva
import second.project.model.ResumoCondominio
import second.project.model.UserRole
import second.project.model.UsuarioPerfil
import second.project.model.Veiculo
import second.project.platform.SelectedDocumentFile

class RepositorioRemoto {
    private val baseUrl = FirebaseConfig.databaseUrl.trimEnd('/')
    private val storageBaseUrl = "https://firebasestorage.googleapis.com/v0/b"

    companion object {
        private const val INTERNAL_DOCUMENT_URL_PREFIX = "second-db-document://"
    }

    suspend fun salvarVeiculo(veiculo: Veiculo) = salvar("veiculos", veiculo.id, veiculo)
    suspend fun listarVeiculos(): List<Veiculo> = listar("veiculos") { item, id -> item.copy(id = id) }
    suspend fun excluirVeiculo(id: String) = excluir("veiculos", id)

    suspend fun salvarConvidado(convidado: Convidado) = salvar("convidados", convidado.id, convidado)
    suspend fun listarConvidados(): List<Convidado> = listar("convidados") { item, id -> item.copy(id = id) }
    suspend fun excluirConvidado(id: String) = excluir("convidados", id)

    suspend fun salvarEncomenda(encomenda: Encomenda) = salvar("encomendas", encomenda.id, encomenda)
    suspend fun listarEncomendas(): List<Encomenda> = listar("encomendas") { item, id -> item.copy(id = id) }
    suspend fun excluirEncomenda(id: String) = excluir("encomendas", id)

    suspend fun salvarAviso(aviso: Aviso) = salvar("avisos", aviso.id, aviso)
    suspend fun listarAvisos(): List<Aviso> = listar("avisos") { item, id -> item.copy(id = id) }
    suspend fun excluirAviso(id: String) = excluir("avisos", id)

    suspend fun salvarReserva(reserva: Reserva) = salvar("reservas", reserva.id, reserva)
    suspend fun listarReservas(): List<Reserva> = listar("reservas") { item, id -> item.copy(id = id) }
    suspend fun excluirReserva(id: String) = excluir("reservas", id)

    suspend fun salvarPrestador(prestador: Prestador) = salvar("prestadores", prestador.id, prestador)
    suspend fun listarPrestadores(): List<Prestador> = listar("prestadores") { item, id -> item.copy(id = id) }
    suspend fun excluirPrestador(id: String) = excluir("prestadores", id)

    suspend fun salvarDocumento(documento: Documento) = salvar("documentos", documento.id, documento)
    suspend fun listarDocumentos(): List<Documento> = listar("documentos") { item, id -> item.copy(id = id) }
    suspend fun excluirDocumento(id: String) = excluir("documentos", id)

    suspend fun enviarArquivoDocumento(file: SelectedDocumentFile): DocumentoArquivoUpload {
        if (!FirebaseConfig.isStorageConfigured()) {
            throw IllegalStateException("Firebase Storage nao configurado.")
        }

        val token = AuthSessionManager.requireIdToken()
        val fileName = sanitizeFileName(file.name)
        val storagePath = "documentos/${AuthSessionManager.currentSession?.uid.orEmpty()}/${randomStorageId()}-$fileName"
        val contentType = parseContentType(file.contentType)
        val downloadToken = randomDownloadToken()
        var storageCause: Throwable? = null

        for (bucket in storageBucketCandidates()) {
            runCatching {
                return uploadDocumentoToBucket(
                    bucket = bucket,
                    storagePath = storagePath,
                    fileName = fileName,
                    contentType = contentType,
                    bytes = file.bytes,
                    authToken = token,
                    downloadToken = downloadToken
                )
            }.onFailure { error ->
                if (error is ClientRequestException && canUseDatabaseFallback(error.response.status)) {
                    storageCause = error
                } else {
                    throw error
                }
            }
        }

        return salvarArquivoDocumentoNoDatabase(
            fileName = fileName,
            contentType = file.contentType.ifBlank { "application/octet-stream" },
            bytes = file.bytes,
            storageCause = storageCause
        )
    }

    fun isArquivoDocumentoInterno(url: String): Boolean {
        return url.startsWith(INTERNAL_DOCUMENT_URL_PREFIX)
    }

    suspend fun buscarArquivoDocumentoInterno(url: String): DocumentoArquivoDownload {
        val fileId = url.removePrefix(INTERNAL_DOCUMENT_URL_PREFIX).trim()
        if (fileId.isBlank()) {
            throw IllegalArgumentException("Referencia interna do arquivo invalida.")
        }

        val token = AuthSessionManager.requireIdToken()
        val file: DocumentoArquivoDatabase? = client.get("$baseUrl/documentFiles/$fileId.json") {
            parameter("auth", token)
        }.body()

        if (file == null || file.dataBase64.isBlank()) {
            throw IllegalStateException("Arquivo nao encontrado no banco de dados.")
        }

        return DocumentoArquivoDownload(
            nome = file.nome.ifBlank { "documento" },
            contentType = file.contentType.ifBlank { "application/octet-stream" },
            bytes = decodeBase64(file.dataBase64)
        )
    }

    private suspend fun uploadDocumentoToBucket(
        bucket: String,
        storagePath: String,
        fileName: String,
        contentType: ContentType,
        bytes: ByteArray,
        authToken: String,
        downloadToken: String
    ): DocumentoArquivoUpload {
        val response: FirebaseStorageUploadResponse = client.post("$storageBaseUrl/$bucket/o") {
            parameter("uploadType", "media")
            parameter("name", storagePath)
            header(HttpHeaders.Authorization, "Firebase $authToken")
            header("x-goog-meta-firebaseStorageDownloadTokens", downloadToken)
            setBody(ByteArrayContent(bytes, contentType))
        }.body()

        val responseDownloadToken = response.downloadTokens
            ?.split(",")
            ?.firstOrNull()
            ?.trim()
            .orEmpty()
            .ifBlank { downloadToken }
        val encodedPath = encodeStoragePath(response.name.ifBlank { storagePath })
        val downloadUrl = buildString {
            append("$storageBaseUrl/$bucket/o/$encodedPath?alt=media")
            if (responseDownloadToken.isNotBlank()) {
                append("&token=$responseDownloadToken")
            }
        }

        return DocumentoArquivoUpload(
            nome = fileName,
            url = downloadUrl,
            storagePath = response.name.ifBlank { storagePath }
        )
    }

    private suspend fun salvarArquivoDocumentoNoDatabase(
        fileName: String,
        contentType: String,
        bytes: ByteArray,
        storageCause: Throwable?
    ): DocumentoArquivoUpload {
        val token = AuthSessionManager.requireIdToken()
        val response: FirebaseRealtimePostResponse = client.post("$baseUrl/documentFiles.json") {
            parameter("auth", token)
            contentType(ContentType.Application.Json)
            setBody(
                DocumentoArquivoDatabase(
                    nome = fileName,
                    contentType = contentType,
                    dataBase64 = encodeBase64(bytes),
                    tamanhoBytes = bytes.size,
                    criadoEm = currentTimestampMillis(),
                    criadoPorUid = AuthSessionManager.currentSession?.uid.orEmpty(),
                    origem = if (storageCause == null) "database" else "database-fallback-storage-unavailable"
                )
            )
        }.body()

        val fileId = response.name
        if (fileId.isBlank()) {
            throw IllegalStateException("Nao foi possivel registrar o arquivo no banco de dados.")
        }

        return DocumentoArquivoUpload(
            nome = fileName,
            url = "$INTERNAL_DOCUMENT_URL_PREFIX$fileId",
            storagePath = fileId
        )
    }

    suspend fun buscarResumoCondominio(): ResumoCondominio {
        val token = AuthSessionManager.currentSession?.idToken
        return client.get("$baseUrl/resumoPublico/condominio.json") {
            if (!token.isNullOrBlank()) parameter("auth", token)
        }.body<ResumoCondominio?>()
            ?: ResumoCondominio()
    }

    suspend fun salvarTotalApartamentos(totalApartamentos: Int): ResumoCondominio {
        val token = AuthSessionManager.requireIdToken()
        client.put("$baseUrl/resumoPublico/condominio/totalApartamentos.json") {
            parameter("auth", token)
            contentType(ContentType.Application.Json)
            setBody(totalApartamentos)
        }
        val totalMoradores = sincronizarTotalMoradoresPublico()
        return ResumoCondominio(
            totalApartamentos = totalApartamentos,
            totalMoradores = totalMoradores
        )
    }

    suspend fun sincronizarTotalMoradoresPublico(): Int {
        val token = AuthSessionManager.requireIdToken()
        val totalMoradores = contarMoradores()
        client.put("$baseUrl/resumoPublico/condominio/totalMoradores.json") {
            parameter("auth", token)
            contentType(ContentType.Application.Json)
            setBody(totalMoradores)
        }
        return totalMoradores
    }

    suspend fun salvarMensagemChat(conversationId: String, mensagem: ChatMessage) {
        salvar("chatPortaria/$conversationId/mensagens", mensagem.id, mensagem.copy(conversationId = conversationId))
    }

    suspend fun listarMensagensChat(conversationId: String): List<ChatMessage> {
        return listar("chatPortaria/$conversationId/mensagens") { item: ChatMessage, id ->
            item.copy(id = id, conversationId = item.conversationId.ifBlank { conversationId })
        }
            .sortedBy { it.timestamp }
    }

    suspend fun listarConversasChat(): List<ChatConversation> {
        val token = AuthSessionManager.requireIdToken()
        val resposta: Map<String, ChatConversationBucket>? = client.get("$baseUrl/chatPortaria.json") {
            parameter("auth", token)
        }.body()

        return resposta
            ?.mapNotNull { (residentUid, bucket) ->
                val mensagens = bucket.mensagens
                    .map { (id, message) -> message.copy(id = id, conversationId = residentUid) }
                    .sortedBy { it.timestamp }
                val ultima = mensagens.lastOrNull() ?: return@mapNotNull null
                val respostasPortaria = mensagens.filter { it.isAdminMessage() }
                ChatConversation(
                    residentUid = residentUid,
                    residentName = ultima.residentName.ifBlank { "Morador" },
                    residentEmail = "",
                    lastMessage = ultima.text,
                    lastTimestamp = ultima.timestamp,
                    messageCount = mensagens.size,
                    lastAuthorRole = if (ultima.isAdminMessage()) UserRole.ADMIN.value else ultima.authorRole,
                    hasAdminResponse = respostasPortaria.isNotEmpty(),
                    lastAnswerTimestamp = respostasPortaria.lastOrNull()?.timestamp ?: 0L,
                    answeredMessageCount = respostasPortaria.size
                )
            }
            ?.sortedByDescending { it.lastTimestamp }
            ?: emptyList()
    }

    private suspend inline fun <reified T : Any> salvar(path: String, id: String, item: T) {
        val token = AuthSessionManager.requireIdToken()
        if (id.isBlank()) {
            client.post("$baseUrl/$path.json") {
                parameter("auth", token)
                contentType(ContentType.Application.Json)
                setBody(item)
            }
        } else {
            client.put("$baseUrl/$path/$id.json") {
                parameter("auth", token)
                contentType(ContentType.Application.Json)
                setBody(item)
            }
        }
    }

    private suspend inline fun <reified T : Any> listar(path: String, crossinline withId: (T, String) -> T): List<T> {
        val token = AuthSessionManager.requireIdToken()
        val resposta: Map<String, T>? = client.get("$baseUrl/$path.json") {
            parameter("auth", token)
        }.body()

        return resposta
            ?.map { (key, value) -> withId(value, key) }
            ?: emptyList()
    }

    private suspend fun contarMoradores(): Int {
        val token = AuthSessionManager.requireIdToken()
        val resposta: Map<String, UsuarioPerfil>? = client.get("$baseUrl/usuarios.json") {
            parameter("auth", token)
        }.body()

        return resposta
            ?.values
            ?.count { it.role == UserRole.MORADOR.value }
            ?: 0
    }

    private suspend fun excluir(path: String, id: String) {
        val token = AuthSessionManager.requireIdToken()
        client.delete("$baseUrl/$path/$id.json") {
            parameter("auth", token)
        }
    }

    private fun sanitizeFileName(fileName: String): String {
        val baseName = fileName
            .substringAfterLast('/')
            .substringAfterLast('\\')
            .ifBlank { "arquivo" }

        return baseName
            .replace(Regex("[^A-Za-z0-9._-]"), "_")
            .take(120)
            .ifBlank { "arquivo" }
    }

    private fun randomStorageId(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return buildString {
            repeat(18) {
                append(alphabet[kotlin.random.Random.nextInt(alphabet.length)])
            }
        }
    }

    private fun randomDownloadToken(): String {
        val hex = "0123456789abcdef"
        fun segment(size: Int): String = buildString {
            repeat(size) {
                append(hex[kotlin.random.Random.nextInt(hex.length)])
            }
        }

        return "${segment(8)}-${segment(4)}-${segment(4)}-${segment(4)}-${segment(12)}"
    }

    private fun canUseDatabaseFallback(status: HttpStatusCode): Boolean {
        return status == HttpStatusCode.NotFound ||
            status == HttpStatusCode.Forbidden ||
            status == HttpStatusCode.Unauthorized
    }

    private fun storageBucketCandidates(): List<String> {
        val configured = FirebaseConfig.storageBucket.trim()
        val projectIdFromConfigured = configured
            .removeSuffix(".appspot.com")
            .removeSuffix(".firebasestorage.app")
            .takeIf { it.isNotBlank() && it != configured }
        val projectIdFromDatabase = FirebaseConfig.databaseUrl
            .substringAfter("https://", "")
            .substringBefore(".")
            .removeSuffix("-default-rtdb")
            .takeIf { it.isNotBlank() }

        return listOfNotNull(
            configured,
            projectIdFromConfigured?.let { "$it.appspot.com" },
            projectIdFromConfigured?.let { "$it.firebasestorage.app" },
            projectIdFromDatabase?.let { "$it.appspot.com" },
            projectIdFromDatabase?.let { "$it.firebasestorage.app" }
        )
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
    }

    private fun parseContentType(value: String): ContentType {
        val safeValue = value.ifBlank { "application/octet-stream" }
        return runCatching { ContentType.parse(safeValue) }
            .getOrDefault(ContentType.Application.OctetStream)
    }

    private fun currentTimestampMillis(): Long {
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun encodeBase64(bytes: ByteArray): String {
        return Base64.encode(bytes)
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeBase64(value: String): ByteArray {
        return Base64.decode(value)
    }

    private fun encodeStoragePath(value: String): String {
        val hex = "0123456789ABCDEF"
        return buildString {
            value.encodeToByteArray().forEach { byte ->
                val code = byte.toInt() and 0xFF
                val safe = code in 'A'.code..'Z'.code ||
                    code in 'a'.code..'z'.code ||
                    code in '0'.code..'9'.code ||
                    code == '-'.code ||
                    code == '_'.code ||
                    code == '.'.code ||
                    code == '~'.code

                if (safe) {
                    append(code.toChar())
                } else {
                    append('%')
                    append(hex[code shr 4])
                    append(hex[code and 0x0F])
                }
            }
        }
    }
}

data class DocumentoArquivoUpload(
    val nome: String,
    val url: String,
    val storagePath: String
)

data class DocumentoArquivoDownload(
    val nome: String,
    val contentType: String,
    val bytes: ByteArray
)

@Serializable
private data class DocumentoArquivoDatabase(
    val nome: String = "",
    val contentType: String = "",
    val dataBase64: String = "",
    val tamanhoBytes: Int = 0,
    val criadoEm: Long = 0L,
    val criadoPorUid: String = "",
    val origem: String = ""
)

@Serializable
private data class FirebaseRealtimePostResponse(
    val name: String = ""
)

@Serializable
private data class FirebaseStorageUploadResponse(
    val name: String = "",
    val bucket: String = "",
    val downloadTokens: String? = null
)

@kotlinx.serialization.Serializable
private data class ChatConversationBucket(
    val mensagens: Map<String, ChatMessage> = emptyMap()
)

private fun ChatMessage.isAdminMessage(): Boolean {
    return authorRole == UserRole.ADMIN.value || author.equals("Portaria", ignoreCase = true)
}
