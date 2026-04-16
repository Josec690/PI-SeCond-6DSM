package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Veiculo(
    val id: String = "",
    val placa: String = "",
    val modelo: String = "",
    val cor: String = "",
    val proprietario: String = "",
    val ativo: Boolean = true
)