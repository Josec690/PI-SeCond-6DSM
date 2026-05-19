package second.project.model

enum class UserRole(val value: String) {
    MORADOR("morador"),
    ADMIN("admin");

    companion object {
        fun from(value: String?): UserRole = when (value) {
            ADMIN.value -> ADMIN
            else -> MORADOR
        }
    }
}

