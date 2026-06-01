package second.project.validation

object Validators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val dateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")

    fun firstError(vararg validations: String?): String {
        return validations.firstOrNull { !it.isNullOrBlank() }.orEmpty()
    }

    fun required(label: String, value: String): String? {
        return if (value.trim().isBlank()) "$label e obrigatorio." else null
    }

    fun email(value: String, required: Boolean = true): String? {
        val trimmed = value.trim()
        if (!required && trimmed.isBlank()) return null
        if (trimmed.isBlank()) return "E-mail e obrigatorio."
        return if (emailRegex.matches(trimmed)) null else "Informe um e-mail valido."
    }

    fun password(value: String): String? {
        return when {
            value.isBlank() -> "Senha e obrigatoria."
            value.length < 6 -> "A senha deve ter pelo menos 6 caracteres."
            else -> null
        }
    }

    fun passwordConfirmation(password: String, confirmation: String): String? {
        return if (password == confirmation) null else "As senhas nao conferem."
    }

    fun optionalDate(label: String, value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isBlank()) return null
        return if (dateRegex.matches(trimmed)) null else "$label deve estar no formato dd/mm/aaaa."
    }

    fun optionalUrl(value: String): String? {
        val trimmed = value.trim()
        if (trimmed.isBlank()) return null
        return if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            null
        } else if (trimmed.startsWith("second-db-document://")) {
            null
        } else {
            "A URL deve comecar com http:// ou https://."
        }
    }
}
