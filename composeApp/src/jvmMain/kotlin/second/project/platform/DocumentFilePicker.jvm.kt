package second.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Files

@Composable
actual fun rememberDocumentFilePicker(
    onFileSelected: (SelectedDocumentFile) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return remember(onFileSelected, onError) {
        {
            runCatching {
                val dialog = FileDialog(null as Frame?, "Selecionar arquivo", FileDialog.LOAD)
                dialog.isVisible = true

                val selectedFileName = dialog.file ?: return@runCatching null
                val selectedDirectory = dialog.directory ?: return@runCatching null
                val file = java.io.File(selectedDirectory, selectedFileName)
                val contentType = Files.probeContentType(file.toPath()) ?: "application/octet-stream"

                SelectedDocumentFile(
                    name = file.name,
                    contentType = contentType,
                    bytes = file.readBytes()
                )
            }.onSuccess { selectedFile ->
                if (selectedFile != null) onFileSelected(selectedFile)
            }.onFailure {
                onError(it.message ?: "Nao foi possivel selecionar o arquivo.")
            }
        }
    }
}
