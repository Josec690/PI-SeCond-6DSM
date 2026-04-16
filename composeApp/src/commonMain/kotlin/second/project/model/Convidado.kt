package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Convidado(
    val id: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val localAlugado: String = "",
    val ativo: Boolean = true
)