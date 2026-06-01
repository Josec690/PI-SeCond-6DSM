package second.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import second.project.model.Reserva
import second.project.repository.RepositorioRemoto
import second.project.validation.Validators

class ReservaViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var area by mutableStateOf("")
    var morador by mutableStateOf("")
    var dataReserva by mutableStateOf("")
    var horario by mutableStateOf("")
    var observacoes by mutableStateOf("")
    var confirmada by mutableStateOf(false)
    var mensagemErro by mutableStateOf("")

    val listaReservas = mutableStateListOf<Reserva>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            runCatching { repo.listarReservas() }
                .onSuccess { dados ->
                    listaReservas.clear()
                    listaReservas.addAll(dados)
                    mensagemErro = ""
                }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel carregar reservas." }
        }
    }

    fun gravar(onSuccess: () -> Unit = {}) {
        val validation = Validators.firstError(
            Validators.required("Area", area),
            Validators.required("Morador", morador),
            Validators.required("Data", dataReserva),
            Validators.optionalDate("Data", dataReserva),
            Validators.required("Horario", horario)
        )
        if (validation.isNotBlank()) {
            mensagemErro = validation
            return
        }

        scope.launch {
            runCatching {
                repo.salvarReserva(
                    Reserva(
                        id = id,
                        area = area.trim(),
                        morador = morador.trim(),
                        dataReserva = dataReserva.trim(),
                        horario = horario.trim(),
                        observacoes = observacoes.trim(),
                        confirmada = confirmada
                    )
                )
            }.onSuccess {
                limparCampos()
                carregar()
                onSuccess()
            }.onFailure {
                mensagemErro = it.message ?: "Nao foi possivel gravar reserva."
            }
        }
    }

    fun apagar(rid: String) {
        scope.launch {
            runCatching { repo.excluirReserva(rid) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel apagar reserva." }
        }
    }

    fun editar(r: Reserva) {
        mensagemErro = ""
        id = r.id
        area = r.area
        morador = r.morador
        dataReserva = r.dataReserva
        horario = r.horario
        observacoes = r.observacoes
        confirmada = r.confirmada
    }

    fun alternarStatus(r: Reserva) {
        scope.launch {
            runCatching { repo.salvarReserva(r.copy(confirmada = !r.confirmada)) }
                .onSuccess { carregar() }
                .onFailure { mensagemErro = it.message ?: "Nao foi possivel alterar reserva." }
        }
    }

    fun limparCampos() {
        id = ""
        area = ""
        morador = ""
        dataReserva = ""
        horario = ""
        observacoes = ""
        confirmada = false
        mensagemErro = ""
    }
}
