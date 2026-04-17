package second.project.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private var mPrefs: SharedPreferences? = null

actual object PreferencesManager {
    private const val PREFS_NAME = "SeCond_Preferences"
    private const val KEY_DARK_THEME = "dark_theme"

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
}



