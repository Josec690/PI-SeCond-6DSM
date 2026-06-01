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
import second.project.validation.Validators

class AvisoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var titulo by mutableStateOf("")
    var mensagem by mutableStateOf("")
    var categoria by mutableStateOf("")
    var autor by mutableStateOf("")
    var dataPublicacao by mutableStateOf("")
    var prioridade by mutableStateOf(false)
    var mensagemErro by mutableStateOf("")

    val listaAvisos = mutableStateListOf<Aviso>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            runCatching { repo.listarAvisos() }
                .onSuccess { dados ->
                    listaAvisos.clear()
                    listaAvisos.addAll(dados)
                    mensagemErro = ""
                }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel carregar avisos." }
        }
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Titulo", titulo),
            Validators.required("Mensagem", mensagem),
            Validators.required("Categoria", categoria),
            Validators.required("Autor", autor),
            Validators.required("Data de publicacao", dataPublicacao),
            Validators.optionalDate("Data de publicacao", dataPublicacao)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarAviso(
                    Aviso(
                        id = id,
                        titulo = titulo.trim(),
                        mensagem = mensagem.trim(),
                        categoria = categoria.trim(),
                        autor = autor.trim(),
                        dataPublicacao = dataPublicacao.trim(),
                        prioridade = prioridade
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel gravar aviso."
            }
        }
    }

    fun apagar(aid: String) {
        scope.launch {
            runCatching { repo.excluirAviso(aid) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar aviso." }
        }
    }

    fun editar(a: Aviso) {
        mensagemErro = ""
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
        mensagemErro = ""
    }
}
