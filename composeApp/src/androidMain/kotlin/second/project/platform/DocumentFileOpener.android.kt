package second.project.platform

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AndroidDocumentFileContext {
    var context: Context? = null
}

suspend actual fun openDocumentFile(fileName: String, contentType: String, bytes: ByteArray) {
    val context = AndroidDocumentFileContext.context
        ?: throw IllegalStateException("Contexto Android indisponivel para abrir o arquivo.")

    val file = withContext(Dispatchers.IO) {
        val dir = File(context.cacheDir, "documentos")
        dir.mkdirs()
        File(dir, sanitizeFileName(fileName)).also { target ->
            target.writeBytes(bytes)
        }
    }
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, contentType.ifBlank { "application/octet-stream" })
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(Intent.createChooser(intent, "Abrir arquivo").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

private fun sanitizeFileName(fileName: String): String {
    return fileName
        .substringAfterLast('/')
        .substringAfterLast('\\')
        .replace(Regex("[^A-Za-z0-9._-]"), "_")
        .ifBlank { "documento" }
}
