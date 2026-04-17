package second.project.navigation

object Graph {
    const val AUTH = "auth_graph"
    const val APP = "app_graph"
    const val VEICULOS = "veiculos_graph"
    const val CONVIDADOS = "convidados_graph"
    const val ENCOMENDAS = "encomendas_graph"
    const val AVISOS = "avisos_graph"
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Cadastro : Screen("cadastro")
    object Dashboard : Screen("dashboard")
    object VeiculoList : Screen("v_list")
    object VeiculoForm : Screen("v_form")
    object ConvidadoList : Screen("c_list")
    object ConvidadoForm : Screen("c_form")
    object EncomendaList : Screen("e_list")
    object EncomendaForm : Screen("e_form")
    object AvisoList : Screen("a_list")
    object AvisoForm : Screen("a_form")
}