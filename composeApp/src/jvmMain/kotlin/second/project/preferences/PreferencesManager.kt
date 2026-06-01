package second.project.preferences

import java.util.prefs.Preferences

actual object PreferencesManager {
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_RESUMO_APARTAMENTOS = "resumo_total_apartamentos"
    private const val KEY_RESUMO_MORADORES = "resumo_total_moradores"
    private val prefs: Preferences = Preferences.userRoot().node("second_project")

    actual fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_THEME, true) // Default to dark theme
    }

    actual fun setDarkThemeEnabled(enabled: Boolean) {
        prefs.putBoolean(KEY_DARK_THEME, enabled)
    }

    actual fun getUserRole(): String? {
        return prefs.get(KEY_USER_ROLE, null)
    }

    actual fun setUserRole(role: String) {
        prefs.put(KEY_USER_ROLE, role)
    }

    actual fun getResumoTotalApartamentos(): Int {
        return prefs.getInt(KEY_RESUMO_APARTAMENTOS, 0)
    }

    actual fun setResumoTotalApartamentos(total: Int) {
        prefs.putInt(KEY_RESUMO_APARTAMENTOS, total)
    }

    actual fun getResumoTotalMoradores(): Int {
        return prefs.getInt(KEY_RESUMO_MORADORES, 0)
    }

    actual fun setResumoTotalMoradores(total: Int) {
        prefs.putInt(KEY_RESUMO_MORADORES, total)
    }
}

