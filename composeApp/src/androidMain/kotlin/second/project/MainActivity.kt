package second.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import second.project.platform.AndroidDocumentFileContext
import second.project.preferences.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize PreferencesManager for Android
        PreferencesManager.initialize(this)
        AndroidDocumentFileContext.context = applicationContext
        setContent {
            // Chama a função principal do commonMain
            App()
        }
    }
}
