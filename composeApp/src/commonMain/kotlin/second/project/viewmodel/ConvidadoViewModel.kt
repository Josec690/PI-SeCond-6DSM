package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Convidado
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class ConvidadoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var nome by mutableStateOf("")
    var telefone by mutableStateOf("")
    var email by mutableStateOf("")
    var localAlugado by mutableStateOf("")
    var ativo by mutableStateOf(true)
    var mensagemErro by mutableStateOf("")

    val listaConvidados = mutableStateListOf<Convidado>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            runCatching { repo.listarConvidados() }
                .onSuccess { dados ->
                    listaConvidados.clear()
                    listaConvidados.addAll(dados)
                    mensagemErro = ""
                }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel carregar convidados." }
        }
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Nome", nome),
            Validators.required("Telefone", telefone),
            Validators.email(email),
            Validators.required("Local alugado", localAlugado)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarConvidado(
                    Convidado(
                        id = id,
                        nome = nome.trim(),
                        telefone = telefone.trim(),
                        email = email.trim(),
                        localAlugado = localAlugado.trim(),
                        ativo = ativo
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel gravar convidado."
            }
        }
    }

    fun apagar(cid: String) {
        scope.launch {
            runCatching { repo.excluirConvidado(cid) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar convidado." }
        }
    }

    fun alternarStatus(c: Convidado) {
        scope.launch {
            runCatching { repo.salvarConvidado(c.copy(ativo = !c.ativo)) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel alterar status." }
        }
    }

    fun editar(c: Convidado) {
        mensagemErro = ""
        id = c.id
        nome = c.nome
        telefone = c.telefone
        email = c.email
        localAlugado = c.localAlugado
        ativo = c.ativo
    }

    fun limparCampos() {
        id = ""
        nome = ""
        telefone = ""
        email = ""
        localAlugado = ""
        ativo = true
        mensagemErro = ""
    }
}
