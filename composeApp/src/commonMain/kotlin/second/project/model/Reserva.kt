package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class Reserva(
    val id: String = "",
    val area: String = "",
    val morador: String = "",
    val dataReserva: String = "",
    val horario: String = "",
    val observacoes: String = "",
    val confirmada: Boolean = false
)

