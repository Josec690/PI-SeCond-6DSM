package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Encomenda(
    val id: String = "",
    val destinatarioNome: String = "",
    val apartamento: String = "",
    val blocoTorre: String = "",
    val codigoRastreio: String = "",
    val transportadora: String = "",
    val dataRecebimento: String = "",
    val statusRetirada: Boolean = false
)

