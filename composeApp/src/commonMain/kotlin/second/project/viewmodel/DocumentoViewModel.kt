package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Documento
import second.project.repository.RepositorioRemoto

class DocumentoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var titulo by mutableStateOf("")
    var tipo by mutableStateOf("")
    var descricao by mutableStateOf("")
    var dataCadastro by mutableStateOf("")
    var arquivoUrl by mutableStateOf("")

    var listaDocumentos = mutableStateListOf<Documento>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarDocumentos()
            listaDocumentos.clear()
            listaDocumentos.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarDocumento(
                Documento(
                    id = id,
                    titulo = titulo,
                    tipo = tipo,
                    descricao = descricao,
                    dataCadastro = dataCadastro,
                    arquivoUrl = arquivoUrl
                )
            )
            limparCampos()
            carregar()
        }
    }

    fun apagar(did: String) {
        scope.launch {
            repo.excluirDocumento(did)
            carregar()
        }
    }

    fun editar(d: Documento) {
        id = d.id
        titulo = d.titulo
        tipo = d.tipo
        descricao = d.descricao
        dataCadastro = d.dataCadastro
        arquivoUrl = d.arquivoUrl
    }

    fun limparCampos() {
        id = ""
        titulo = ""
        tipo = ""
        descricao = ""
        dataCadastro = ""
        arquivoUrl = ""
    }
}

