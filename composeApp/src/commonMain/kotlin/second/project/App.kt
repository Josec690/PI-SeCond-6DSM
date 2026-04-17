package second.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import second.project.navigation.AppNavigation
import second.project.repository.RepositorioRemoto
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

    val customDarkColors = darkColorScheme(
        primary = Color(0xFF4A3B8B),
        background = Color.Black,
        surface = Color(0xFF121212),
        onPrimary = Color(0xFFF5F2FF),
        onBackground = Color(0xFFF5F2FF),
        onSurface = Color(0xFFF5F2FF)
    )

    MaterialTheme(colorScheme = customDarkColors) {
        AppNavigation(vViewModel, cViewModel, eViewModel, aViewModel)
    }
}