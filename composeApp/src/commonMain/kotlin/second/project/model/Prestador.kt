package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Prestador(
    val id: String = "",
    val nome: String = "",
    val empresa: String = "",
    val telefone: String = "",
    val servico: String = "",
    val dataVisita: String = "",
    val autorizado: Boolean = false
)

