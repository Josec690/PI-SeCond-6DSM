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

class ReservaViewModel(private val repo: RepositorioRemoto) {
    var id by mutableStateOf("")
    var area by mutableStateOf("")
    var morador by mutableStateOf("")
    var dataReserva by mutableStateOf("")
    var horario by mutableStateOf("")
    var observacoes by mutableStateOf("")
    var confirmada by mutableStateOf(false)

    var listaReservas = mutableStateListOf<Reserva>()
    private val scope = CoroutineScope(Dispatchers.Main)

    fun carregar() {
        scope.launch {
            val dados = repo.listarReservas()
            listaReservas.clear()
            listaReservas.addAll(dados)
        }
    }

    fun gravar() {
        scope.launch {
            repo.salvarReserva(
                Reserva(
                    id = id,
                    area = area,
                    morador = morador,
                    dataReserva = dataReserva,
                    horario = horario,
                    observacoes = observacoes,
                    confirmada = confirmada
                )
            )
            limparCampos()
            carregar()
        }
    }

    fun apagar(rid: String) {
        scope.launch {
            repo.excluirReserva(rid)
            carregar()
        }
    }

    fun editar(r: Reserva) {
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
            repo.salvarReserva(r.copy(confirmada = !r.confirmada))
            carregar()
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
    }
}

