package second.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader

@Composable
actual fun rememberDocumentFilePicker(
    onFileSelected: (SelectedDocumentFile) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return remember(onFileSelected, onError) {
        {
            runCatching {
                val input = document.createElement("input") as HTMLInputElement
                input.type = "file"
                input.accept = "*/*"
                input.onchange = {
                    val file = input.files?.item(0)
                    if (file == null) {
                        onError("Nenhum arquivo foi selecionado.")
                    } else {
                        val reader = FileReader()
                        reader.onload = {
                            runCatching {
                                val buffer = reader.result as ArrayBuffer
                                val typedArray = Int8Array(buffer)
                                val bytes = ByteArray(typedArray.length) { index ->
                                    typedArray.asDynamic()[index].unsafeCast<Int>().toByte()
                                }

                                SelectedDocumentFile(
                                    name = file.name,
                                    contentType = file.type.ifBlank { "application/octet-stream" },
                                    bytes = bytes
                                )
                            }.onSuccess(onFileSelected)
                                .onFailure { onError(it.message ?: "Nao foi possivel ler o arquivo.") }
                            null
                        }
                        reader.onerror = {
                            onError("Nao foi possivel ler o arquivo selecionado.")
                            null
                        }
                        reader.readAsArrayBuffer(file)
                    }
                    null
                }
                input.click()
            }.onFailure {
                onError(it.message ?: "Nao foi possivel abrir o seletor de arquivos.")
            }
        }
    }
}
