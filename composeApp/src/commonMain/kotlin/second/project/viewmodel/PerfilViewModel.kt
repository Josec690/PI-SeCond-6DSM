package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import second.project.model.UsuarioPerfil
import second.project.model.Veiculo
import second.project.preferences.PreferencesManager
import second.project.repository.AuthSessionManager
import second.project.repository.RepositorioRemoto

class PerfilViewModel(private val repo: RepositorioRemoto) {
    var perfil by mutableStateOf<UsuarioPerfil?>(null)
        private set
    var veiculo by mutableStateOf<Veiculo?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set
    var mensagemErro by mutableStateOf("")
        private set
    var totalApartamentos by mutableStateOf(PreferencesManager.getResumoTotalApartamentos().takeIf { it > 0 }?.toString().orEmpty())
    var totalMoradores by mutableStateOf(PreferencesManager.getResumoTotalMoradores())
        private set
    var mensagemConfiguracao by mutableStateOf("")
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun carregarResumoCondominio() {
        scope.launch {
            carregando = true
            mensagemErro = ""
            mensagemConfiguracao = ""
            try {
                val resumo = repo.buscarResumoCondominio()
                totalApartamentos = resumo.totalApartamentos.takeIf { it > 0 }?.toString().orEmpty()
                totalMoradores = repo.sincronizarTotalMoradoresPublico()
                salvarResumoEmCache(totalApartamentos.toIntOrNull() ?: 0, totalMoradores)
            } catch (e: Exception) {
                mensagemErro = e.message ?: "Nao foi possivel carregar os dados do condominio."
            } finally {
                carregando = false
            }
        }
    }

    fun salvarTotalApartamentos() {
        val total = totalApartamentos.toIntOrNull()
        if (total == null || total < 0) {
            mensagemConfiguracao = "Informe uma quantidade valida de apartamentos."
            return
        }

        scope.launch {
            carregando = true
            mensagemErro = ""
            mensagemConfiguracao = ""
            try {
                val resumo = repo.salvarTotalApartamentos(total)
                totalApartamentos = resumo.totalApartamentos.toString()
                totalMoradores = resumo.totalMoradores
                salvarResumoEmCache(resumo.totalApartamentos, resumo.totalMoradores)
                mensagemConfiguracao = "Quantidade de apartamentos atualizada."
            } catch (e: Exception) {
                mensagemConfiguracao = e.message ?: "Nao foi possivel salvar os dados do condominio."
            } finally {
                carregando = false
            }
        }
    }

    private fun salvarResumoEmCache(totalApartamentos: Int, totalMoradores: Int) {
        PreferencesManager.setResumoTotalApartamentos(totalApartamentos)
        PreferencesManager.setResumoTotalMoradores(totalMoradores)
    }

    fun carregar() {
        scope.launch {
            carregando = true
            mensagemErro = ""
            try {
                val session = AuthSessionManager.currentSession
                    ?: throw IllegalStateException("Sessao expirada. Faca login novamente.")

                perfil = session.perfil
                veiculo = repo.listarVeiculos().firstOrNull { item ->
                    item.pertenceAoMorador(session.perfil)
                }
            } catch (e: Exception) {
                mensagemErro = e.message ?: "Nao foi possivel carregar o perfil."
            } finally {
                carregando = false
            }
        }
    }

    private fun Veiculo.pertenceAoMorador(perfil: UsuarioPerfil): Boolean {
        val nome = perfil.nome.trim()
        val email = perfil.email.trim()
        val mesmoNomeOuEmail = listOf(moradorVinculado, proprietario).any { valor ->
            valor.equals(nome, ignoreCase = true) || valor.equals(email, ignoreCase = true)
        }
        val mesmaUnidade = bloco.equals(perfil.bloco, ignoreCase = true) &&
            apartamento.equals(perfil.apartamento, ignoreCase = true)

        return mesmoNomeOuEmail || mesmaUnidade
    }
}
