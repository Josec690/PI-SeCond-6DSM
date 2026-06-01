package second.project.platform

import java.awt.Desktop
import java.nio.file.Files
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend actual fun openDocumentFile(fileName: String, contentType: String, bytes: ByteArray) {
    val file = withContext(Dispatchers.IO) {
        val safeName = sanitizeFileName(fileName)
        val suffix = safeName.substringAfterLast('.', "").let { extension ->
            if (extension.isBlank()) ".tmp" else ".$extension"
        }
        Files.createTempFile("second-documento-", suffix).also { path ->
            Files.write(path, bytes)
        }.toFile()
    }

    Desktop.getDesktop().open(file)
}

private fun sanitizeFileName(fileName: String): String {
    return fileName
        .substringAfterLast('/')
        .substringAfterLast('\\')
        .replace(Regex("[^A-Za-z0-9._-]"), "_")
        .ifBlank { "documento" }
}
