package second.project.preferences

import kotlinx.browser.localStorage

actual object PreferencesManager {
    private const val KEY_DARK_THEME = "dark_theme"

    actual fun isDarkThemeEnabled(): Boolean {
        val stored = localStorage.getItem(KEY_DARK_THEME)
        return stored?.toBoolean() ?: true // Default to dark theme
    }

    actual fun setDarkThemeEnabled(enabled: Boolean) {
        localStorage.setItem(KEY_DARK_THEME, enabled.toString())
    }
}

