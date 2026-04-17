package second.project.repository

import second.project.model.Veiculo
import second.project.model.Convidado
import second.project.model.Encomenda
import second.project.model.Aviso
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RepositorioRemoto {
    private val baseUrl = "https://projeto-second-default-rtdb.firebaseio.com" // TROQUE PELA SUA URL

    // --- MÉTODOS DE VEÍCULOS ---
    suspend fun salvarVeiculo(veiculo: Veiculo) {
        if (veiculo.id.isEmpty()) {
            client.post("$baseUrl/veiculos.json") {
                setBody(veiculo)
                contentType(ContentType.Application.Json)
            }
        } else {
            client.put("$baseUrl/veiculos/${veiculo.id}.json") {
                setBody(veiculo)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun listarVeiculos(): List<Veiculo> {
        return try {
            val resposta: Map<String, Veiculo>? = client.get("$baseUrl/veiculos.json").body()
            resposta?.map { it.value.copy(id = it.key) } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun excluirVeiculo(id: String) {
        client.delete("$baseUrl/veiculos/$id.json")
    }

    // --- MÉTODOS DE CONVIDADOS ---
    suspend fun salvarConvidado(convidado: Convidado) {
        if (convidado.id.isEmpty()) {
            client.post("$baseUrl/convidados.json") {
                setBody(convidado)
                contentType(ContentType.Application.Json)
            }
        } else {
            client.put("$baseUrl/convidados/${convidado.id}.json") {
                setBody(convidado)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun listarConvidados(): List<Convidado> {
        return try {
            val resposta: Map<String, Convidado>? = client.get("$baseUrl/convidados.json").body()
            resposta?.map { it.value.copy(id = it.key) } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    suspend fun excluirConvidado(id: String) {
        client.delete("$baseUrl/convidados/$id.json")
    }

    // --- METODOS DE ENCOMENDAS ---
    suspend fun salvarEncomenda(encomenda: Encomenda) {
        if (encomenda.id.isEmpty()) {
            client.post("$baseUrl/encomendas.json") {
                setBody(encomenda)
                contentType(ContentType.Application.Json)
            }
        } else {
            client.put("$baseUrl/encomendas/${encomenda.id}.json") {
                setBody(encomenda)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun listarEncomendas(): List<Encomenda> {
        return try {
            val resposta: Map<String, Encomenda>? = client.get("$baseUrl/encomendas.json").body()
            resposta?.map { it.value.copy(id = it.key) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun excluirEncomenda(id: String) {
        client.delete("$baseUrl/encomendas/$id.json")
    }

    // --- METODOS DE AVISOS ---
    suspend fun salvarAviso(aviso: Aviso) {
        if (aviso.id.isEmpty()) {
            client.post("$baseUrl/avisos.json") {
                setBody(aviso)
                contentType(ContentType.Application.Json)
            }
        } else {
            client.put("$baseUrl/avisos/${aviso.id}.json") {
                setBody(aviso)
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun listarAvisos(): List<Aviso> {
        return try {
            val resposta: Map<String, Aviso>? = client.get("$baseUrl/avisos.json").body()
            resposta?.map { it.value.copy(id = it.key) } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun excluirAviso(id: String) {
        client.delete("$baseUrl/avisos/$id.json")
    }
}