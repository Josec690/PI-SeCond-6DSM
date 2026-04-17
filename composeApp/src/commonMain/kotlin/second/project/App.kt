package second.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import second.project.navigation.AppNavigation
import second.project.preferences.PreferencesManager
import second.project.repository.RepositorioRemoto
import second.project.ui.components.CrudDesign
import second.project.viewmodel.VeiculoViewModel
import second.project.viewmodel.ConvidadoViewModel
import second.project.viewmodel.EncomendaViewModel
import second.project.viewmodel.AvisoViewModel

@Composable
fun App() {
    val repo = RepositorioRemoto()
    val vViewModel = VeiculoViewModel(repo)
    val cViewModel = ConvidadoViewModel(repo)
    val eViewModel = EncomendaViewModel(repo)
    val aViewModel = AvisoViewModel(repo)
    var isDarkTheme by rememberSaveable { mutableStateOf(PreferencesManager.isDarkThemeEnabled()) }

    val customDarkColors = darkColorScheme(
        primary = Color(0xFF4A3B8B),
        background = Color.Black,
        surface = Color(0xFF121212),
        onPrimary = Color(0xFFF5F2FF),
        onBackground = Color(0xFFF5F2FF),
        onSurface = Color(0xFFF5F2FF)
    )

    val customLightColors = lightColorScheme(
        primary = Color(0xFF6A56B8),
        background = Color(0xFFECE8F8),
        surface = Color(0xFFF6F2FF),
        onPrimary = Color.White,
        onBackground = Color(0xFF1A1630),
        onSurface = Color(0xFF1A1630)
    )

    SideEffect {
        CrudDesign.applyTheme(isDarkTheme)
    }

    MaterialTheme(colorScheme = if (isDarkTheme) customDarkColors else customLightColors) {
        AppNavigation(
            vViewModel = vViewModel,
            cViewModel = cViewModel,
            eViewModel = eViewModel,
            aViewModel = aViewModel,
            isDarkTheme = isDarkTheme,
            onToggleTheme = {
                isDarkTheme = !isDarkTheme
                PreferencesManager.setDarkThemeEnabled(isDarkTheme)
            }
        )
    }
}