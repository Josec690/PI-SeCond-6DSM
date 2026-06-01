package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Documento
import second.project.platform.SelectedDocumentFile
import second.project.platform.openDocumentFile
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class DocumentoViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var titulo by mutableStateOf("")
    var tipo by mutableStateOf("")
    var descricao by mutableStateOf("")
    var dataCadastro by mutableStateOf("")
    var arquivoNome by mutableStateOf("")
    var arquivoUrl by mutableStateOf("")
    var mensagemErro by mutableStateOf("")
    var mensagemUpload by mutableStateOf("")
    var mensagemDownload by mutableStateOf("")
    var enviandoArquivo by mutableStateOf(false)
    var baixandoArquivo by mutableStateOf(false)

    val listaDocumentos = mutableStateListOf<Documento>()
    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val MAX_UPLOAD_BYTES = 5 * 1024 * 1024
    }

    fun carregar() {
        scope.launch {
            runCatching { repo.listarDocumentos() }
                .onSuccess { dados ->
                    listaDocumentos.clear()
                    listaDocumentos.addAll(dados)
                    mensagemErro = ""
                }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel carregar documentos." }
        }
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Titulo", titulo),
            Validators.required("Tipo", tipo),
            Validators.required("Descricao", descricao),
            Validators.required("Data de cadastro", dataCadastro),
            Validators.optionalDate("Data de cadastro", dataCadastro),
            Validators.required("Arquivo", arquivoUrl),
            Validators.optionalUrl(arquivoUrl)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarDocumento(
                    Documento(
                        id = id,
                        titulo = titulo.trim(),
                        tipo = tipo.trim(),
                        descricao = descricao.trim(),
                        dataCadastro = dataCadastro.trim(),
                        arquivoNome = arquivoNome.trim(),
                        arquivoUrl = arquivoUrl.trim()
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel gravar documento."
            }
        }
    }

    fun enviarArquivo(file: SelectedDocumentFile) {
        mensagemErro = ""
        mensagemUpload = ""

        if (file.bytes.isEmpty()) {
            mensagemErro = "O arquivo selecionado esta vazio."
            return
        }
        if (file.bytes.size > MAX_UPLOAD_BYTES) {
            mensagemErro = "Selecione um arquivo de ate 5 MB. Para arquivos maiores, ative o Firebase Storage."
            return
        }

        scope.launch {
            enviandoArquivo = true
            mensagemUpload = "Enviando arquivo..."

            runCatching { repo.enviarArquivoDocumento(file) }
                .onSuccess { upload ->
                    arquivoNome = upload.nome
                    arquivoUrl = upload.url
                    mensagemUpload = if (repo.isArquivoDocumentoInterno(upload.url)) {
                        "Arquivo salvo no banco: ${upload.nome}"
                    } else {
                        "Arquivo enviado: ${upload.nome}"
                    }
                    mensagemErro = ""
                }
                .onFailure {
                    mensagemUpload = ""
                    mensagemErro = it.message
                        ?: "Nao foi possivel enviar o arquivo. Verifique o Firebase Storage."
                }

            enviandoArquivo = false
        }
    }

    fun isArquivoInterno(documento: Documento): Boolean {
        return repo.isArquivoDocumentoInterno(documento.arquivoUrl)
    }

    fun baixarArquivoInterno(documento: Documento) {
        mensagemErro = ""
        mensagemDownload = ""

        scope.launch {
            baixandoArquivo = true
            mensagemDownload = "Abrindo arquivo..."

            runCatching {
                val arquivo = repo.buscarArquivoDocumentoInterno(documento.arquivoUrl)
                openDocumentFile(arquivo.nome, arquivo.contentType, arquivo.bytes)
            }.onSuccess {
                mensagemDownload = ""
            }.onFailure {
                mensagemDownload = ""
                mensagemErro = it.message ?: "Nao foi possivel abrir o arquivo."
            }

            baixandoArquivo = false
        }
    }

    fun apagar(did: String) {
        scope.launch {
            runCatching { repo.excluirDocumento(did) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar documento." }
        }
    }

    fun editar(d: Documento) {
        mensagemErro = ""
        id = d.id
        titulo = d.titulo
        tipo = d.tipo
        descricao = d.descricao
        dataCadastro = d.dataCadastro
        arquivoNome = d.arquivoNome
        arquivoUrl = d.arquivoUrl
        mensagemUpload = if (d.arquivoNome.isNotBlank()) "Arquivo atual: ${d.arquivoNome}" else ""
        mensagemDownload = ""
    }

    fun limparCampos() {
        id = ""
        titulo = ""
        tipo = ""
        descricao = ""
        dataCadastro = ""
        arquivoNome = ""
        arquivoUrl = ""
        mensagemErro = ""
        mensagemUpload = ""
        mensagemDownload = ""
        enviandoArquivo = false
        baixandoArquivo = false
    }
}
