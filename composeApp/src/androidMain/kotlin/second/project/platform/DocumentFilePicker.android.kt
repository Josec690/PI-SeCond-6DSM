package second.project.platform

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberDocumentFilePicker(
    onFileSelected: (SelectedDocumentFile) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val currentOnFileSelected by rememberUpdatedState(onFileSelected)
    val currentOnError by rememberUpdatedState(onError)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        runCatching {
            val resolver = context.contentResolver
            val name = resolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
            }.orEmpty().ifBlank { "documento" }
            val contentType = resolver.getType(uri).orEmpty().ifBlank { "application/octet-stream" }
            val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("Nao foi possivel ler o arquivo selecionado.")

            SelectedDocumentFile(
                name = name,
                contentType = contentType,
                bytes = bytes
            )
        }.onSuccess(currentOnFileSelected)
            .onFailure { currentOnError(it.message ?: "Nao foi possivel selecionar o arquivo.") }
    }

    return { launcher.launch("*/*") }
}
