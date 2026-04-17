package second.project.preferences

expect object PreferencesManager {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
}

