package second.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import second.project.ui.auth.*
import second.project.ui.dashboard.DashboardScreen
import second.project.ui.veiculos.*
import second.project.ui.convidados.*
import second.project.viewmodel.*

private fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) { launchSingleTop = true }
}

@Composable
fun AppNavigation(vViewModel: VeiculoViewModel, cViewModel: ConvidadoViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Graph.AUTH) {
        navigation(startDestination = Screen.Login.route, route = Graph.AUTH) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = {
                        navController.navigate(Graph.APP) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateToCadastro = { navController.navigateSingleTopTo(Screen.Cadastro.route) }
                )
            }

            composable(Screen.Cadastro.route) {
                CadastroScreen(
                    onRegisterSuccess = {
                        navController.navigate(Graph.APP) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBackToLogin = { navController.navigateSingleTopTo(Screen.Login.route) }
                )
            }
        }

        navigation(startDestination = Screen.Dashboard.route, route = Graph.APP) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavVeiculos = { navController.navigateSingleTopTo(Graph.VEICULOS) },
                    onNavConvidados = { navController.navigateSingleTopTo(Graph.CONVIDADOS) },
                    onNavLogin = {
                        navController.navigate(Graph.AUTH) {
                            popUpTo(Graph.APP) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavCadastro = {
                        navController.navigate(Graph.AUTH) {
                            popUpTo(Graph.APP) { inclusive = true }
                            launchSingleTop = true
                        }
                        navController.navigateSingleTopTo(Screen.Cadastro.route)
                    }
                )
            }

            navigation(startDestination = Screen.VeiculoList.route, route = Graph.VEICULOS) {
                composable(Screen.VeiculoList.route) {
                    LaunchedEffect(Unit) { vViewModel.carregar() }
                    ListaVeiculosScreen(vViewModel) { navController.navigateSingleTopTo(Screen.VeiculoForm.route) }
                }

                composable(Screen.VeiculoForm.route) {
                    FormularioVeiculoScreen(vViewModel) { navController.popBackStack() }
                }
            }

            navigation(startDestination = Screen.ConvidadoList.route, route = Graph.CONVIDADOS) {
                composable(Screen.ConvidadoList.route) {
                    LaunchedEffect(Unit) { cViewModel.carregar() }
                    ListaConvidadosScreen(cViewModel) { navController.navigateSingleTopTo(Screen.ConvidadoForm.route) }
                }

                composable(Screen.ConvidadoForm.route) {
                    FormularioConvidadoScreen(cViewModel) { navController.popBackStack() }
                }
            }
        }
    }
}