package second.project.model

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPerfil(
    val id: String = "",
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val role: String = UserRole.MORADOR.value,
    val bloco: String = "",
    val apartamento: String = "",
    val telefone: String = ""
)
