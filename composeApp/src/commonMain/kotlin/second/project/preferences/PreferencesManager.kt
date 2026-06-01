package second.project.preferences

expect object PreferencesManager {
    fun isDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
    fun getUserRole(): String?
    fun setUserRole(role: String)
    fun getResumoTotalApartamentos(): Int
    fun setResumoTotalApartamentos(total: Int)
    fun getResumoTotalMoradores(): Int
    fun setResumoTotalMoradores(total: Int)
}

