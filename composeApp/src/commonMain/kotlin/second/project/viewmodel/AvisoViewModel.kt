package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Aviso
import second.project.repository.RepositorioRemoto

class AvisoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var titulo by mutableStateOf("")
    var mensagem by mutableStateOf("")
    var categoria by mutableStateOf("")
    var autor by mutableStateOf("")
    var dataPublicacao by mutableStateOf("")
    var prioridade by mutableStateOf(false)

    var listaAvisos = mutableStateListOf<Aviso>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarAvisos()
            listaAvisos.clear()
            listaAvisos.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarAviso(
                Aviso(
                    id = id,
                    titulo = titulo,
                    mensagem = mensagem,
                    categoria = categoria,
                    autor = autor,
                    dataPublicacao = dataPublicacao,
                    prioridade = prioridade
                )
            )
            limparCampos()
            carregar()
        }
    }

    fun apagar(aid: String) {
        scope.launch {
            repo.excluirAviso(aid)
            carregar()
        }
    }

    fun editar(a: Aviso) {
        id = a.id
        titulo = a.titulo
        mensagem = a.mensagem
        categoria = a.categoria
        autor = a.autor
        dataPublicacao = a.dataPublicacao
        prioridade = a.prioridade
    }

    fun limparCampos() {
        id = ""
        titulo = ""
        mensagem = ""
        categoria = ""
        autor = ""
        dataPublicacao = ""
        prioridade = false
    }
}

