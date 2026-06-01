package second.project.preferences

import kotlinx.browser.localStorage

actual object PreferencesManager {
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_RESUMO_APARTAMENTOS = "resumo_total_apartamentos"
    private const val KEY_RESUMO_MORADORES = "resumo_total_moradores"

    actual fun isDarkThemeEnabled(): Boolean {
        val stored = localStorage.getItem(KEY_DARK_THEME)
        return stored?.toBoolean() ?: true // Default to dark theme
    }

    actual fun setDarkThemeEnabled(enabled: Boolean) {
        localStorage.setItem(KEY_DARK_THEME, enabled.toString())
    }

    actual fun getUserRole(): String? {
        return localStorage.getItem(KEY_USER_ROLE)
    }

    actual fun setUserRole(role: String) {
        localStorage.setItem(KEY_USER_ROLE, role)
    }

    actual fun getResumoTotalApartamentos(): Int {
        return localStorage.getItem(KEY_RESUMO_APARTAMENTOS)?.toIntOrNull() ?: 0
    }

    actual fun setResumoTotalApartamentos(total: Int) {
        localStorage.setItem(KEY_RESUMO_APARTAMENTOS, total.toString())
    }

    actual fun getResumoTotalMoradores(): Int {
        return localStorage.getItem(KEY_RESUMO_MORADORES)?.toIntOrNull() ?: 0
    }

    actual fun setResumoTotalMoradores(total: Int) {
        localStorage.setItem(KEY_RESUMO_MORADORES, total.toString())
    }
}

