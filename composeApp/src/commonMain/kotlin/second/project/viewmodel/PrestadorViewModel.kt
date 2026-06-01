package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Prestador
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class PrestadorViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var nome by mutableStateOf("")
    var empresa by mutableStateOf("")
    var telefone by mutableStateOf("")
    var servico by mutableStateOf("")
    var dataVisita by mutableStateOf("")
    var autorizado by mutableStateOf(false)
    var mensagemErro by mutableStateOf("")

    val listaPrestadores = mutableStateListOf<Prestador>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            runCatching { repo.listarPrestadores() }
                .onSuccess { dados ->
                    listaPrestadores.clear()
                    listaPrestadores.addAll(dados)
                    mensagemErro = ""
                }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel carregar prestadores." }
        }
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Nome", nome),
            Validators.required("Empresa", empresa),
            Validators.required("Telefone", telefone),
            Validators.required("Servico", servico),
            Validators.optionalDate("Data da visita", dataVisita)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarPrestador(
                    Prestador(
                        id = id,
                        nome = nome.trim(),
                        empresa = empresa.trim(),
                        telefone = telefone.trim(),
                        servico = servico.trim(),
                        dataVisita = dataVisita.trim(),
                        autorizado = autorizado
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel gravar prestador."
            }
        }
    }

    fun apagar(pid: String) {
        scope.launch {
            runCatching { repo.excluirPrestador(pid) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar prestador." }
        }
    }

    fun editar(p: Prestador) {
        mensagemErro = ""
        id = p.id
        nome = p.nome
        empresa = p.empresa
        telefone = p.telefone
        servico = p.servico
        dataVisita = p.dataVisita
        autorizado = p.autorizado
    }

    fun alternarStatus(p: Prestador) {
        scope.launch {
            runCatching { repo.salvarPrestador(p.copy(autorizado = !p.autorizado)) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel alterar autorizacao." }
        }
    }

    fun limparCampos() {
        id = ""
        nome = ""
        empresa = ""
        telefone = ""
        servico = ""
        dataVisita = ""
        autorizado = false
        mensagemErro = ""
    }
}
