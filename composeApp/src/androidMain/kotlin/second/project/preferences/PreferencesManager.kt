package second.project.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private var mPrefs: SharedPreferences? = null

actual object PreferencesManager {
    private const val PREFS_NAME = "SeCond_Preferences"
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_RESUMO_APARTAMENTOS = "resumo_total_apartamentos"
    private const val KEY_RESUMO_MORADORES = "resumo_total_moradores"

    fun initialize(context: Context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    actual fun isDarkThemeEnabled(): Boolean {
        return mPrefs?.getBoolean(KEY_DARK_THEME, true) ?: true // Default to dark theme
    }

    actual fun setDarkThemeEnabled(enabled: Boolean) {
        mPrefs?.edit {
            putBoolean(KEY_DARK_THEME, enabled)
        }
    }

    actual fun getUserRole(): String? {
        return mPrefs?.getString(KEY_USER_ROLE, null)
    }

    actual fun setUserRole(role: String) {
        mPrefs?.edit { putString(KEY_USER_ROLE, role) }
    }

    actual fun getResumoTotalApartamentos(): Int {
        return mPrefs?.getInt(KEY_RESUMO_APARTAMENTOS, 0) ?: 0
    }

    actual fun setResumoTotalApartamentos(total: Int) {
        mPrefs?.edit { putInt(KEY_RESUMO_APARTAMENTOS, total) }
    }

    actual fun getResumoTotalMoradores(): Int {
        return mPrefs?.getInt(KEY_RESUMO_MORADORES, 0) ?: 0
    }

    actual fun setResumoTotalMoradores(total: Int) {
        mPrefs?.edit { putInt(KEY_RESUMO_MORADORES, total) }
    }
}



