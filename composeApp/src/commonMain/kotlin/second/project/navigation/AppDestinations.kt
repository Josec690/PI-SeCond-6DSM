package second.project.navigation

object Graph {
    const val AUTH = "auth_graph"
    const val APP = "app_graph"
    const val VEICULOS = "veiculos_graph"
    const val CONVIDADOS = "convidados_graph"
    const val ENCOMENDAS = "encomendas_graph"
    const val AVISOS = "avisos_graph"
    const val RESERVAS = "reservas_graph"
    const val PRESTADORES = "prestadores_graph"
    const val DOCUMENTOS = "documentos_graph"
    const val CONFIGURACAO = "configuracao_graph"
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
    object ReservaList : Screen("r_list")
    object ReservaForm : Screen("r_form")
    object PrestadorList : Screen("p_list")
    object PrestadorForm : Screen("p_form")
    object DocumentoList : Screen("d_list")
    object DocumentoForm : Screen("d_form")
    object ConfiguracaoPerfil : Screen("config_perfil")
}