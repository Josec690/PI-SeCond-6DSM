package second.project.preferences

import java.util.prefs.Preferences

actual object PreferencesManager {
    private const val KEY_DARK_THEME = "dark_theme"
    private val prefs: Preferences = Preferences.userRoot().node("second_project")

    actual fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_THEME, true) // Default to dark theme
    }

    actual fun setDarkThemeEnabled(enabled: Boolean) {
        prefs.putBoolean(KEY_DARK_THEME, enabled)
    }
}

