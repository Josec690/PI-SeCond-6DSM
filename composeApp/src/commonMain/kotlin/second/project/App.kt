package second.project

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import second.project.navigation.AppNavigation
import second.project.repository.RepositorioRemoto
import second.project.viewmodel.VeiculoViewModel
import second.project.viewmodel.ConvidadoViewModel

@Composable
fun App() {
    val repo = RepositorioRemoto()
    val vViewModel = VeiculoViewModel(repo)
    val cViewModel = ConvidadoViewModel(repo)

    val customDarkColors = darkColors(
        primary = Color(0xFF4A3B8B),
        background = Color.Black,
        surface = Color(0xFF121212)
    )

    MaterialTheme(colors = customDarkColors) {
        AppNavigation(vViewModel, cViewModel)
    }
}