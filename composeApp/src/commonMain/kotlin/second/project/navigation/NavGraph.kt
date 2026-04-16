package second.project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import second.project.ui.auth.*
import second.project.ui.dashboard.DashboardScreen
import second.project.ui.veiculos.*
import second.project.ui.convidados.*
import second.project.viewmodel.*

@Composable
fun AppNavigation(vViewModel: VeiculoViewModel, cViewModel: ConvidadoViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                onNavigateToCadastro = { navController.navigate(Screen.Cadastro.route) }
            )
        }
        composable(Screen.Cadastro.route) {
            CadastroScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Cadastro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavVeiculos = { navController.navigate(Screen.VeiculoList.route) },
                onNavConvidados = { navController.navigate(Screen.ConvidadoList.route) },
                onNavLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onNavCadastro = {
                    navController.navigate(Screen.Cadastro.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        // Veículos
        composable(Screen.VeiculoList.route) {
            vViewModel.carregar()
            ListaVeiculosScreen(vViewModel) { navController.navigate(Screen.VeiculoForm.route) }
        }
        composable(Screen.VeiculoForm.route) {
            FormularioVeiculoScreen(vViewModel) { navController.popBackStack() }
        }
        // Convidados
        composable(Screen.ConvidadoList.route) {
            cViewModel.carregar()
            ListaConvidadosScreen(cViewModel) { navController.navigate(Screen.ConvidadoForm.route) }
        }
        composable(Screen.ConvidadoForm.route) {
            FormularioConvidadoScreen(cViewModel) { navController.popBackStack() }
        }
    }
}