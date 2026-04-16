package second.project.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Dashboard : Screen("dashboard")
    object VeiculoList : Screen("v_list")
    object VeiculoForm : Screen("v_form")
    object ConvidadoList : Screen("c_list")
    object ConvidadoForm : Screen("c_form")
}