package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class ResumoCondominio(
    val totalApartamentos: Int = 0,
    val totalMoradores: Int = 0
)
