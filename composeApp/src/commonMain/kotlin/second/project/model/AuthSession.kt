package second.project.model

data class AuthSession(
    val uid: String,
    val email: String,
    val idToken: String,
    val refreshToken: String,
    val role: UserRole,
    val nome: String = "",
    val perfil: UsuarioPerfil = UsuarioPerfil(
        id = uid,
        uid = uid,
        email = email,
        role = role.value,
        nome = nome
    )
)
