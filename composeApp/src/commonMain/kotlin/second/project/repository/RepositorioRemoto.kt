package second.project.repository

import second.project.model.Veiculo
import second.project.model.Convidado
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class RepositorioRemoto {
    private val baseUrl = "https://projeto-second-default-rtdb.firebaseio.com/\n" // TROQUE PELA SUA URL

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
}