package second.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import second.project.model.UserRole
import second.project.ui.auth.*
import second.project.ui.avisos.*
import second.project.ui.chat.ChatPortariaScreen
import second.project.ui.configuracao.*
import second.project.ui.documentos.*
import second.project.ui.convidados.*
import second.project.ui.dashboard.DashboardScreen
import second.project.ui.encomendas.*
import second.project.ui.moradores.CadastroMoradorScreen
import second.project.ui.prestadores.*
import second.project.ui.reservas.*
import second.project.ui.veiculos.*
import second.project.viewmodel.*

private fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) { launchSingleTop = true }
}

@Composable
fun AppNavigation(
    vViewModel: VeiculoViewModel,
    cViewModel: ConvidadoViewModel,
    eViewModel: EncomendaViewModel,
    aViewModel: AvisoViewModel,
    rViewModel: ReservaViewModel,
    pViewModel: PrestadorViewModel,
    dViewModel: DocumentoViewModel,
    mViewModel: MoradorViewModel,
    perfilViewModel: PerfilViewModel,
    chatViewModel: ChatPortariaViewModel,
    authViewModel: AuthViewModel,
    userRole: UserRole,
    onUserRoleChange: (UserRole) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Graph.AUTH) {
            navigation(startDestination = Screen.Login.route, route = Graph.AUTH) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onLogin = { role ->
                            onUserRoleChange(role)
                            navController.navigate(Graph.APP) {
                                popUpTo(Graph.AUTH) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToCadastro = {
                            authViewModel.clearError()
                            navController.navigateSingleTopTo(Screen.Cadastro.route)
                        }
                    )
                }

                composable(Screen.Cadastro.route) {
                    CadastroScreen(
                        authViewModel = authViewModel,
                        onRegisterSuccess = {
                            onUserRoleChange(UserRole.ADMIN)
                            navController.navigate(Graph.APP) {
                                popUpTo(Graph.AUTH) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onBackToLogin = {
                            authViewModel.clearError()
                            navController.navigateSingleTopTo(Screen.Login.route)
                        }
                    )
                }

            }

            navigation(startDestination = Screen.Dashboard.route, route = Graph.APP) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        onNavVeiculos = { navController.navigateSingleTopTo(Graph.VEICULOS) },
                        onNavConvidados = { navController.navigateSingleTopTo(Graph.CONVIDADOS) },
                        onNavEncomendas = { navController.navigateSingleTopTo(Graph.ENCOMENDAS) },
                        onNavAvisos = { navController.navigateSingleTopTo(Graph.AVISOS) },
                        onNavReservas = { navController.navigateSingleTopTo(Graph.RESERVAS) },
                        onNavPrestadores = { navController.navigateSingleTopTo(Graph.PRESTADORES) },
                        onNavDocumentos = { navController.navigateSingleTopTo(Graph.DOCUMENTOS) },
                        onNavChat = { navController.navigateSingleTopTo(Graph.CHAT) },
                        onNavMoradores = { navController.navigateSingleTopTo(Graph.MORADORES) },
                        onNavConfiguracao = { navController.navigateSingleTopTo(Screen.ConfiguracaoPerfil.route) },
                        userRole = userRole,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme,
                        onLogout = {
                            authViewModel.logout()
                            onUserRoleChange(UserRole.MORADOR)
                            navController.navigate(Graph.AUTH) {
                                popUpTo(Graph.APP) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                navigation(startDestination = Screen.VeiculoList.route, route = Graph.VEICULOS) {
                    composable(Screen.VeiculoList.route) {
                        LaunchedEffect(Unit) { vViewModel.carregar() }
                        ListaVeiculosScreen(
                            viewModel = vViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.VeiculoForm.route) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.VeiculoForm.route) {
                        FormularioVeiculoScreen(
                            viewModel = vViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.ConvidadoList.route, route = Graph.CONVIDADOS) {
                    composable(Screen.ConvidadoList.route) {
                        LaunchedEffect(Unit) { cViewModel.carregar() }
                        ListaConvidadosScreen(
                            viewModel = cViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.ConvidadoForm.route) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.ConvidadoForm.route) {
                        FormularioConvidadoScreen(
                            viewModel = cViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.EncomendaList.route, route = Graph.ENCOMENDAS) {
                    composable(Screen.EncomendaList.route) {
                        LaunchedEffect(Unit) { eViewModel.carregar() }
                        ListaEncomendasScreen(
                            viewModel = eViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.EncomendaForm.route) },
                            onBack = { navController.popBackStack() },
                            userRole = userRole
                        )
                    }

                    composable(Screen.EncomendaForm.route) {
                        FormularioEncomendaScreen(
                            viewModel = eViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() },
                            userRole = userRole
                        )
                    }
                }

                navigation(startDestination = Screen.AvisoList.route, route = Graph.AVISOS) {
                    composable(Screen.AvisoList.route) {
                        LaunchedEffect(Unit) { aViewModel.carregar() }
                        ListaAvisosScreen(
                            viewModel = aViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.AvisoForm.route) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.AvisoForm.route) {
                        FormularioAvisoScreen(
                            viewModel = aViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.ReservaList.route, route = Graph.RESERVAS) {
                    composable(Screen.ReservaList.route) {
                        ListaReservasScreen(
                            viewModel = rViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.ReservaForm.route) },
                            onBack = { navController.popBackStack() },
                            userRole = userRole
                        )
                    }

                    composable(Screen.ReservaForm.route) {
                        FormularioReservaScreen(
                            viewModel = rViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() },
                            userRole = userRole
                        )
                    }
                }

                navigation(startDestination = Screen.PrestadorList.route, route = Graph.PRESTADORES) {
                    composable(Screen.PrestadorList.route) {
                        ListaPrestadoresScreen(
                            viewModel = pViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.PrestadorForm.route) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.PrestadorForm.route) {
                        FormularioPrestadorScreen(
                            viewModel = pViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.DocumentoList.route, route = Graph.DOCUMENTOS) {
                    composable(Screen.DocumentoList.route) {
                        ListaDocumentosScreen(
                            viewModel = dViewModel,
                            onAddClick = { navController.navigateSingleTopTo(Screen.DocumentoForm.route) },
                            onBack = { navController.popBackStack() },
                            userRole = userRole
                        )
                    }

                    composable(Screen.DocumentoForm.route) {
                        FormularioDocumentoScreen(
                            viewModel = dViewModel,
                            onSaved = { navController.popBackStack() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.ChatPortaria.route, route = Graph.CHAT) {
                    composable(Screen.ChatPortaria.route) {
                        ChatPortariaScreen(
                            viewModel = chatViewModel,
                            userRole = userRole,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                navigation(startDestination = Screen.CadastroMorador.route, route = Graph.MORADORES) {
                    composable(Screen.CadastroMorador.route) {
                        CadastroMoradorScreen(
                            viewModel = mViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                composable(Screen.ConfiguracaoPerfil.route) {
                    ConfiguracaoPerfilScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme,
                        userRole = userRole,
                        perfilViewModel = perfilViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
}
