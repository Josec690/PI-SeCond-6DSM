
package second.project.viewmodel

import androidx.compose.runtime.*
import second.project.model.Convidado
import second.project.repository.RepositorioRemoto
import kotlinx.coroutines.*

class ConvidadoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var nome by mutableStateOf("")
    var telefone by mutableStateOf("")
    var email by mutableStateOf("")
    var localAlugado by mutableStateOf("")
    var ativo by mutableStateOf(true)

    var listaConvidados = mutableStateListOf<Convidado>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarConvidados()
            listaConvidados.clear()
            listaConvidados.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarConvidado(Convidado(id, nome, telefone, email, localAlugado, ativo))
            limparCampos()
            carregar()
        }
    }

    fun apagar(cid: String) {
        scope.launch {
            repo.excluirConvidado(cid)
            carregar()
        }
    }

    fun editar(c: Convidado) {
        id = c.id; nome = c.nome; telefone = c.telefone; email = c.email; localAlugado = c.localAlugado; ativo = c.ativo
    }

    fun limparCampos() {
        id = ""; nome = ""; telefone = ""; email = ""; localAlugado = ""; ativo = true
    }
}