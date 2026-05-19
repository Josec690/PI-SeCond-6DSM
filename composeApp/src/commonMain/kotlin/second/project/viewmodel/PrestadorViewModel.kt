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

class PrestadorViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var nome by mutableStateOf("")
    var empresa by mutableStateOf("")
    var telefone by mutableStateOf("")
    var servico by mutableStateOf("")
    var dataVisita by mutableStateOf("")
    var autorizado by mutableStateOf(false)

    var listaPrestadores = mutableStateListOf<Prestador>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarPrestadores()
            listaPrestadores.clear()
            listaPrestadores.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarPrestador(
                Prestador(
                    id = id,
                    nome = nome,
                    empresa = empresa,
                    telefone = telefone,
                    servico = servico,
                    dataVisita = dataVisita,
                    autorizado = autorizado
                )
            )
            limparCampos()
            carregar()
        }
    }

    fun apagar(pid: String) {
        scope.launch {
            repo.excluirPrestador(pid)
            carregar()
        }
    }

    fun editar(p: Prestador) {
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
            repo.salvarPrestador(p.copy(autorizado = !p.autorizado))
            carregar()
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
    }
}

