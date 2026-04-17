package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Aviso(
    val id: String = "",
    val titulo: String = "",
    val mensagem: String = "",
    val categoria: String = "",
    val autor: String = "",
    val dataPublicacao: String = "",
    val prioridade: Boolean = false
)

