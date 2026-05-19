package second.project.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private var mPrefs: SharedPreferences? = null

actual object PreferencesManager {
    private const val PREFS_NAME = "SeCond_Preferences"
    private const val KEY_DARK_THEME = "dark_theme"
    private const val KEY_USER_ROLE = "user_role"

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
}



