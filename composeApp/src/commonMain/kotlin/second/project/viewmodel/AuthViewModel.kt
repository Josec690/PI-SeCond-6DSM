package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import second.project.model.ResumoCondominio
import second.project.model.UserRole
import second.project.preferences.PreferencesManager
import second.project.repository.AuthSessionManager
import second.project.repository.FirebaseAuthRepository
import second.project.validation.Validators

class AuthViewModel(private val authRepository: FirebaseAuthRepository) {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var statusMessage by mutableStateOf("")
        private set
    var loginEmailError by mutableStateOf("")
        private set
    var loginSenhaError by mutableStateOf("")
        private set
    var cadastroNomeError by mutableStateOf("")
        private set
    var cadastroEmailError by mutableStateOf("")
        private set
    var cadastroSenhaError by mutableStateOf("")
        private set
    var cadastroConfirmarSenhaError by mutableStateOf("")
        private set
    var resumoCondominio by mutableStateOf(resumoEmCache())
        private set

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        isLoading = false
        statusMessage = ""
        errorMessage = throwable.message ?: "Falha inesperada na autenticacao."
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)

    fun carregarResumoPublico() {
        resumoCondominio = resumoEmCache()
        scope.launch {
            runCatching {
                authRepository.buscarResumoPublico()
            }.onSuccess { resumo ->
                resumoCondominio = resumo
                salvarResumoEmCache(resumo)
            }
        }
    }

    fun login(email: String, senha: String, expectedRole: UserRole, onSuccess: (UserRole) -> Unit) {
        validateLoginFields(email, senha)
        if (loginEmailError.isNotBlank() || loginSenhaError.isNotBlank()) {
            errorMessage = ""
            return
        }

        runAuthAction("Autenticando no Firebase...") {
            val session = authRepository.login(email, senha, expectedRole)
            PreferencesManager.setUserRole(session.role.value)
            onSuccess(session.role)
        }
    }

    fun cadastrarAdministrador(nome: String, email: String, senha: String, confirmarSenha: String, onSuccess: () -> Unit) {
        validateCadastroFields(nome, email, senha, confirmarSenha)
        if (
            cadastroNomeError.isNotBlank() ||
            cadastroEmailError.isNotBlank() ||
            cadastroSenhaError.isNotBlank() ||
            cadastroConfirmarSenhaError.isNotBlank()
        ) {
            errorMessage = ""
            return
        }

        runAuthAction("Criando administrador no Firebase...") {
            val session = authRepository.cadastrarAdministrador(nome, email, senha)
            PreferencesManager.setUserRole(session.role.value)
            onSuccess()
        }
    }

    fun logout() {
        AuthSessionManager.clear()
        PreferencesManager.setUserRole(UserRole.MORADOR.value)
        errorMessage = ""
        statusMessage = ""
    }

    fun clearError() {
        errorMessage = ""
        statusMessage = ""
    }

    fun clearLoginFieldErrors() {
        loginEmailError = ""
        loginSenhaError = ""
        errorMessage = ""
        statusMessage = ""
    }

    fun clearCadastroFieldErrors() {
        cadastroNomeError = ""
        cadastroEmailError = ""
        cadastroSenhaError = ""
        cadastroConfirmarSenhaError = ""
        errorMessage = ""
        statusMessage = ""
    }

    private fun validateLoginFields(email: String, senha: String) {
        loginEmailError = Validators.email(email).orEmpty()
        loginSenhaError = Validators.password(senha).orEmpty()
    }

    private fun validateCadastroFields(nome: String, email: String, senha: String, confirmarSenha: String) {
        cadastroNomeError = Validators.required("Nome completo", nome).orEmpty()
        cadastroEmailError = Validators.email(email).orEmpty()
        cadastroSenhaError = Validators.password(senha).orEmpty()
        cadastroConfirmarSenhaError = Validators.firstError(
            Validators.required("Confirmacao de senha", confirmarSenha),
            Validators.passwordConfirmation(senha, confirmarSenha)
        )
    }

    private fun runAuthAction(status: String, block: suspend () -> Unit): Job? {
        if (isLoading) return null
        return runCatching {
            scope.launch {
                isLoading = true
                errorMessage = ""
                clearLoginFieldErrors()
                clearCadastroFieldErrors()
                statusMessage = status
                try {
                    block()
                    statusMessage = ""
                } catch (t: Throwable) {
                    errorMessage = describeThrowable(t)
                    statusMessage = ""
                } finally {
                    isLoading = false
                }
            }
        }.getOrElse { t ->
            isLoading = true
            statusMessage = ""
            errorMessage = describeThrowable(t)
            isLoading = false
            null
        }
    }

    private fun describeThrowable(t: Throwable): String {
        val message = t.message?.takeIf { it.isNotBlank() }
        if (message != null) return message

        return when (t::class.simpleName) {
            "IllegalStateException" -> "Falha na configuracao HTTP do app. Verifique as dependencias Ktor do alvo em execucao."
            "NoClassDefFoundError", "ClassNotFoundException" -> "Falta uma dependencia de runtime para autenticar no Firebase."
            else -> "Nao foi possivel concluir a operacao (${t::class.simpleName ?: "erro desconhecido"})."
        }
    }

    private fun resumoEmCache(): ResumoCondominio {
        return ResumoCondominio(
            totalApartamentos = PreferencesManager.getResumoTotalApartamentos(),
            totalMoradores = PreferencesManager.getResumoTotalMoradores()
        )
    }

    private fun salvarResumoEmCache(resumo: ResumoCondominio) {
        PreferencesManager.setResumoTotalApartamentos(resumo.totalApartamentos)
        PreferencesManager.setResumoTotalMoradores(resumo.totalMoradores)
    }
}
