package second.project.viewmodel

import androidx.compose.runtime.*
import second.project.model.Veiculo
import second.project.repository.RepositorioRemoto
import kotlinx.coroutines.*

class VeiculoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var placa by mutableStateOf("")
    var modelo by mutableStateOf("")
    var cor by mutableStateOf("")
    var proprietario by mutableStateOf("")
    var ativo by mutableStateOf(true)

    var listaVeiculos = mutableStateListOf<Veiculo>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarVeiculos()
            listaVeiculos.clear()
            listaVeiculos.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarVeiculo(Veiculo(id, placa, modelo, cor, proprietario, ativo))
            limparCampos()
            carregar()
        }
    }

    fun apagar(vid: String) {
        scope.launch {
            repo.excluirVeiculo(vid)
            carregar()
        }
    }

    fun alternarStatus(v: Veiculo) {
        scope.launch {
            repo.salvarVeiculo(v.copy(ativo = !v.ativo))
            carregar()
        }
    }

    fun editar(v: Veiculo) {
        id = v.id; placa = v.placa; modelo = v.modelo; cor = v.cor; proprietario = v.proprietario; ativo = v.ativo
    }

    fun limparCampos() {
        id = ""; placa = ""; modelo = ""; cor = ""; proprietario = ""; ativo = true
    }
}