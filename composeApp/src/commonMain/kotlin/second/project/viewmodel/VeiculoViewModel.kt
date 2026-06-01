package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.UsuarioPerfil
import second.project.model.Veiculo
import second.project.repository.FirebaseAuthRepository
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class VeiculoViewModel(
    private val repo: RepositorioRemoto,
    private val authRepository: FirebaseAuthRepository
) {
    var id by mutableStateOf("")
    var placa by mutableStateOf("")
    var modelo by mutableStateOf("")
    var cor by mutableStateOf("")
    var proprietario by mutableStateOf("")
    var moradorVinculado by mutableStateOf("")
    var bloco by mutableStateOf("")
    var apartamento by mutableStateOf("")
    var ativo by mutableStateOf(true)
    var mensagemErro by mutableStateOf("")
    var carregandoMoradores by mutableStateOf(false)
        private set

    val listaVeiculos = mutableStateListOf<Veiculo>()
    val listaMoradores = mutableStateListOf<UsuarioPerfil>()
    private val scope = CoroutineScope(Dispatchers.Main)

    val moradoresFiltrados: List<UsuarioPerfil>
        get() {
            val termo = proprietario.trim()
            if (moradorVinculado.isNotBlank()) return emptyList()
            if (termo.length < 2) return emptyList()

            return listaMoradores.filter { morador ->
                morador.nome.contains(termo, ignoreCase = true) ||
                    morador.email.contains(termo, ignoreCase = true) ||
                    morador.bloco.contains(termo, ignoreCase = true) ||
                    morador.apartamento.contains(termo, ignoreCase = true) ||
                    morador.telefone.contains(termo, ignoreCase = true)
            }.take(5)
        }

    fun carregar() {
        scope.launch {
            runCatching {
                repo.listarVeiculos()
            }.onSuccess { dados ->
                listaVeiculos.clear()
                listaVeiculos.addAll(dados)
                mensagemErro = ""
            }.onFailure { erro ->
                mensagemErro = erro.message ?: "Nao foi possivel carregar veiculos."
            }
        }
    }

    fun carregarMoradores() {
        scope.launch {
            carregandoMoradores = true
            runCatching {
                authRepository.listarMoradores()
            }.onSuccess { moradores ->
                listaMoradores.clear()
                listaMoradores.addAll(moradores)
                mensagemErro = ""
            }.onFailure { erro ->
                mensagemErro = erro.message ?: "Nao foi possivel carregar moradores."
            }
            carregandoMoradores = false
        }
    }

    fun selecionarMorador(morador: UsuarioPerfil) {
        proprietario = morador.nome
        moradorVinculado = morador.nome
        bloco = morador.bloco
        apartamento = morador.apartamento
        mensagemErro = ""
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Placa", placa),
            Validators.required("Modelo", modelo),
            Validators.required("Cor", cor),
            Validators.required("Proprietario", proprietario),
            if (moradorVinculado.isBlank()) "Selecione um morador cadastrado para vincular ao veiculo." else null,
            Validators.required("Bloco", bloco),
            Validators.required("Apartamento", apartamento)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarVeiculo(
                    Veiculo(
                        id = id,
                        placa = placa.trim().uppercase(),
                        modelo = modelo.trim(),
                        cor = cor.trim(),
                        proprietario = proprietario.trim(),
                        moradorVinculado = moradorVinculado.trim(),
                        bloco = bloco.trim(),
                        apartamento = apartamento.trim(),
                        ativo = ativo
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure { erro ->
                mensagemErro = erro.message ?: "Nao foi possivel gravar veiculo."
            }
        }
    }

    fun apagar(vid: String) {
        scope.launch {
            runCatching { repo.excluirVeiculo(vid) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar veiculo." }
        }
    }

    fun alternarStatus(v: Veiculo) {
        scope.launch {
            runCatching { repo.salvarVeiculo(v.copy(ativo = !v.ativo)) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel alterar status." }
        }
    }

    fun editar(v: Veiculo) {
        mensagemErro = ""
        id = v.id
        placa = v.placa
        modelo = v.modelo
        cor = v.cor
        proprietario = v.proprietario
        moradorVinculado = v.moradorVinculado
        bloco = v.bloco
        apartamento = v.apartamento
        ativo = v.ativo
    }

    fun limparCampos() {
        id = ""
        placa = ""
        modelo = ""
        cor = ""
        proprietario = ""
        moradorVinculado = ""
        bloco = ""
        apartamento = ""
        ativo = true
        mensagemErro = ""
    }
}
