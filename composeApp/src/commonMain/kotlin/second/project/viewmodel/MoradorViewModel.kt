package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.UsuarioPerfil
import second.project.model.Veiculo
import second.project.repository.FirebaseAuthRepository
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class MoradorViewModel(
    private val authRepository: FirebaseAuthRepository,
    private val repo: RepositorioRemoto
) {
    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")
    var bloco by mutableStateOf("")
    var apartamento by mutableStateOf("")
    var telefone by mutableStateOf("")
    var consulta by mutableStateOf("")
    var mensagemErro by mutableStateOf("")
    var mensagemSucesso by mutableStateOf("")
    var carregando by mutableStateOf(false)
        private set

    val listaMoradores = mutableStateListOf<UsuarioPerfil>()
    val listaVeiculos = mutableStateListOf<Veiculo>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            carregando = true
            mensagemErro = ""
            try {
                listaMoradores.clear()
                listaMoradores.addAll(authRepository.listarMoradores())
                listaVeiculos.clear()
                listaVeiculos.addAll(repo.listarVeiculos())
            } catch (e: Exception) {
                mensagemErro = e.message ?: "Nao foi possivel carregar moradores."
            } finally {
                carregando = false
            }
        }
    }

    fun gravar() {
        val validation = Validators.firstError(
            Validators.required("Nome", nome),
            Validators.email(email),
            Validators.password(senha),
            Validators.passwordConfirmation(senha, confirmarSenha),
            Validators.required("Bloco", bloco),
            Validators.required("Apartamento", apartamento),
            Validators.required("Telefone", telefone)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            mensagemSucesso = ""
            return
        }

        scope.launch {
            carregando = true
            mensagemErro = ""
            mensagemSucesso = ""
            try {
                authRepository.cadastrarMorador(nome, email, senha, bloco, apartamento, telefone)
                limparCampos()
                mensagemSucesso = "Morador cadastrado no Firebase para primeiro acesso."
                listaMoradores.clear()
                listaMoradores.addAll(authRepository.listarMoradores())
                listaVeiculos.clear()
                listaVeiculos.addAll(repo.listarVeiculos())
            } catch (e: Exception) {
                mensagemErro = e.message ?: "Nao foi possivel cadastrar morador."
            } finally {
                carregando = false
            }
        }
    }

    fun limparCampos() {
        nome = ""
        email = ""
        senha = ""
        confirmarSenha = ""
        bloco = ""
        apartamento = ""
        telefone = ""
        mensagemErro = ""
    }

    fun veiculosDoMorador(morador: UsuarioPerfil): List<Veiculo> {
        return listaVeiculos.filter { veiculo ->
            val nome = morador.nome.trim()
            val email = morador.email.trim()
            val mesmoNomeOuEmail = listOf(veiculo.moradorVinculado, veiculo.proprietario).any { valor ->
                valor.equals(nome, ignoreCase = true) || valor.equals(email, ignoreCase = true)
            }
            val mesmaUnidade = veiculo.bloco.equals(morador.bloco, ignoreCase = true) &&
                veiculo.apartamento.equals(morador.apartamento, ignoreCase = true)

            mesmoNomeOuEmail || mesmaUnidade
        }
    }
}
