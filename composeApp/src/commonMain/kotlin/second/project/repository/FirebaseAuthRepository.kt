package second.project.repository

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import second.project.model.AuthSession
import second.project.model.ResumoCondominio
import second.project.model.UserRole
import second.project.model.UsuarioPerfil
import second.project.preferences.PreferencesManager
import second.project.validation.Validators

class FirebaseAuthException(message: String) : Exception(message)

class FirebaseAuthRepository {
    private val databaseUrl = FirebaseConfig.databaseUrl.trimEnd('/')
    private val authBaseUrl = "https://identitytoolkit.googleapis.com/v1/accounts"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(email: String, senha: String, expectedRole: UserRole): AuthSession {
        validateAuthConfig()
        validateCredentials(email, senha)

        val auth = try {
            client.post("$authBaseUrl:signInWithPassword") {
                parameter("key", FirebaseConfig.webApiKey)
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(email.trim(), senha, returnSecureToken = true))
            }.body<AuthResponse>()
        } catch (e: ResponseException) {
            throw FirebaseAuthException(firebaseMessage(e))
        }

        val profile = buscarPerfil(auth.localId, auth.idToken)
            ?: UsuarioPerfil(
                id = auth.localId,
                uid = auth.localId,
                email = auth.email,
                role = UserRole.MORADOR.value
            )

        val realRole = UserRole.from(profile.role)
        if (realRole != expectedRole) {
            throw FirebaseAuthException("Esta conta esta cadastrada como ${realRole.value}. Selecione o tipo correto.")
        }

        return AuthSession(
            uid = auth.localId,
            email = auth.email,
            idToken = auth.idToken,
            refreshToken = auth.refreshToken,
            role = realRole,
            nome = profile.nome,
            perfil = profile
        ).also(AuthSessionManager::setSession)
    }

    suspend fun cadastrarAdministrador(nome: String, email: String, senha: String): AuthSession {
        validateAuthConfig()
        val validation = Validators.firstError(
            Validators.required("Nome", nome),
            Validators.email(email),
            Validators.password(senha)
        )
        if (validation.isNotBlank()) throw FirebaseAuthException(validation)

        val auth = criarUsuario(email, senha)
        val profile = UsuarioPerfil(
            id = auth.localId,
            uid = auth.localId,
            nome = nome.trim(),
            email = auth.email,
            role = UserRole.ADMIN.value
        )
        salvarPerfil(profile, auth.idToken)
        runCatching { sincronizarTotalMoradoresPublico(auth.idToken) }

        return AuthSession(
            uid = auth.localId,
            email = auth.email,
            idToken = auth.idToken,
            refreshToken = auth.refreshToken,
            role = UserRole.ADMIN,
            nome = nome.trim(),
            perfil = profile
        ).also(AuthSessionManager::setSession)
    }

    suspend fun cadastrarMorador(
        nome: String,
        email: String,
        senha: String,
        bloco: String,
        apartamento: String,
        telefone: String
    ): UsuarioPerfil {
        validateAuthConfig()
        val validation = Validators.firstError(
            Validators.required("Nome", nome),
            Validators.email(email),
            Validators.password(senha),
            Validators.required("Bloco", bloco),
            Validators.required("Apartamento", apartamento),
            Validators.required("Telefone", telefone)
        )
        if (validation.isNotBlank()) throw FirebaseAuthException(validation)

        val auth = criarUsuario(email, senha)
        val profile = UsuarioPerfil(
            id = auth.localId,
            uid = auth.localId,
            nome = nome.trim(),
            email = auth.email,
            role = UserRole.MORADOR.value,
            bloco = bloco.trim(),
            apartamento = apartamento.trim(),
            telefone = telefone.trim()
        )
        salvarPerfil(profile, auth.idToken)
        AuthSessionManager.currentSession?.idToken?.let { adminToken ->
            runCatching { sincronizarTotalMoradoresPublico(adminToken) }
        }
        return profile
    }

    suspend fun buscarResumoPublico(): ResumoCondominio {
        return client.get("$databaseUrl/resumoPublico/condominio.json").body<ResumoCondominio?>()
            ?: ResumoCondominio()
    }

    suspend fun listarMoradores(): List<UsuarioPerfil> {
        val token = AuthSessionManager.requireIdToken()
        val resposta: Map<String, UsuarioPerfil>? = client.get("$databaseUrl/usuarios.json") {
            parameter("auth", token)
        }.body()

        return resposta
            ?.map { (key, value) -> value.copy(id = key, uid = value.uid.ifBlank { key }) }
            ?.filter { it.role == UserRole.MORADOR.value }
            ?.sortedBy { it.nome }
            ?: emptyList()
    }

    private suspend fun criarUsuario(email: String, senha: String): AuthResponse {
        return try {
            client.post("$authBaseUrl:signUp") {
                parameter("key", FirebaseConfig.webApiKey)
                contentType(ContentType.Application.Json)
                setBody(AuthRequest(email.trim(), senha, returnSecureToken = true))
            }.body()
        } catch (e: ResponseException) {
            throw FirebaseAuthException(firebaseMessage(e))
        }
    }

    private suspend fun buscarPerfil(uid: String, idToken: String): UsuarioPerfil? {
        return try {
            client.get("$databaseUrl/usuarios/$uid.json") {
                parameter("auth", idToken)
            }.body<UsuarioPerfil?>()?.copy(id = uid, uid = uid)
        } catch (e: ResponseException) {
            throw FirebaseAuthException(firebaseMessage(e))
        }
    }

    private suspend fun salvarPerfil(profile: UsuarioPerfil, idToken: String) {
        try {
            client.put("$databaseUrl/usuarios/${profile.uid}.json") {
                parameter("auth", idToken)
                contentType(ContentType.Application.Json)
                setBody(profile)
            }
        } catch (e: ResponseException) {
            throw FirebaseAuthException(firebaseMessage(e))
        }
    }

    private suspend fun sincronizarTotalMoradoresPublico(idToken: String) {
        val resposta: Map<String, UsuarioPerfil>? = client.get("$databaseUrl/usuarios.json") {
            parameter("auth", idToken)
        }.body()
        val totalMoradores = resposta
            ?.values
            ?.count { it.role == UserRole.MORADOR.value }
            ?: 0
        PreferencesManager.setResumoTotalMoradores(totalMoradores)

        client.put("$databaseUrl/resumoPublico/condominio/totalMoradores.json") {
            parameter("auth", idToken)
            contentType(ContentType.Application.Json)
            setBody(totalMoradores)
        }
    }

    private fun validateAuthConfig() {
        if (!FirebaseConfig.isAuthConfigured()) {
            throw FirebaseAuthException("Configure a Web API Key em FirebaseConfig.kt para usar Firebase Auth.")
        }
    }

    private fun validateCredentials(email: String, senha: String) {
        val validation = Validators.firstError(
            Validators.email(email),
            Validators.password(senha)
        )
        if (validation.isNotBlank()) throw FirebaseAuthException(validation)
    }

    private suspend fun firebaseMessage(exception: ResponseException): String {
        val body = exception.response.bodyAsText()
        val code = runCatching {
            json.decodeFromString(FirebaseErrorResponse.serializer(), body).error.message
        }.getOrNull()

        return when {
            code == null -> "Firebase retornou HTTP ${exception.response.status.value}: ${body.ifBlank { exception.response.status.description }}"
            code.contains("EMAIL_EXISTS") -> "Este e-mail ja esta cadastrado."
            code.contains("EMAIL_NOT_FOUND") -> "E-mail nao encontrado."
            code.contains("INVALID_LOGIN_CREDENTIALS") -> "E-mail ou senha invalidos."
            code.contains("INVALID_PASSWORD") -> "Senha invalida."
            code.contains("WEAK_PASSWORD") -> "A senha deve ter pelo menos 6 caracteres."
            code.contains("OPERATION_NOT_ALLOWED") -> "Ative Email/Password em Authentication > Sign-in method no Firebase."
            code.contains("PASSWORD_LOGIN_DISABLED") -> "Login por e-mail/senha esta desativado no Firebase."
            code.contains("API_KEY_INVALID") -> "A Web API Key configurada no FirebaseConfig.kt e invalida."
            code.contains("PERMISSION_DENIED") -> "Permissao negada no Realtime Database. Publique as regras com auth != null."
            code.contains("TOO_MANY_ATTEMPTS_TRY_LATER") -> "Muitas tentativas. Tente novamente mais tarde."
            else -> "Erro do Firebase: $code"
        }
    }
}

@Serializable
private data class AuthRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean
)

@Serializable
private data class AuthResponse(
    @SerialName("localId") val localId: String,
    val email: String,
    @SerialName("idToken") val idToken: String,
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
private data class FirebaseErrorResponse(val error: FirebaseError)

@Serializable
private data class FirebaseError(val message: String)
