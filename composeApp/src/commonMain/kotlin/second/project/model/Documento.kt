package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Documento(
    val id: String = "",
    val titulo: String = "",
    val tipo: String = "",
    val descricao: String = "",
    val dataCadastro: String = "",
    val arquivoNome: String = "",
    val arquivoUrl: String = ""
)

