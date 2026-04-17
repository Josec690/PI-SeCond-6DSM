package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Encomenda
import second.project.repository.RepositorioRemoto

class EncomendaViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var destinatarioNome by mutableStateOf("")
    var apartamento by mutableStateOf("")
    var blocoTorre by mutableStateOf("")
    var codigoRastreio by mutableStateOf("")
    var transportadora by mutableStateOf("")
    var dataRecebimento by mutableStateOf("")
    var statusRetirada by mutableStateOf(false)

    var listaEncomendas = mutableStateListOf<Encomenda>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarEncomendas()
            listaEncomendas.clear()
            listaEncomendas.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarEncomenda(
                Encomenda(
                    id = id,
                    destinatarioNome = destinatarioNome,
                    apartamento = apartamento,
                    blocoTorre = blocoTorre,
                    codigoRastreio = codigoRastreio,
                    transportadora = transportadora,
                    dataRecebimento = dataRecebimento,
                    statusRetirada = statusRetirada
                )
            )
            limparCampos()
            carregar()
        }
    }

    fun apagar(eid: String) {
        scope.launch {
            repo.excluirEncomenda(eid)
            carregar()
        }
    }

    fun editar(e: Encomenda) {
        id = e.id
        destinatarioNome = e.destinatarioNome
        apartamento = e.apartamento
        blocoTorre = e.blocoTorre
        codigoRastreio = e.codigoRastreio
        transportadora = e.transportadora
        dataRecebimento = e.dataRecebimento
        statusRetirada = e.statusRetirada
    }

    fun limparCampos() {
        id = ""
        destinatarioNome = ""
        apartamento = ""
        blocoTorre = ""
        codigoRastreio = ""
        transportadora = ""
        dataRecebimento = ""
        statusRetirada = false
    }
}

