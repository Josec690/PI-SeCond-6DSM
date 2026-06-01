package second.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberDocumentFilePicker(
    onFileSelected: (SelectedDocumentFile) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return remember(onError) {
        {
            onError("Upload de arquivo ainda nao esta disponivel no alvo WebAssembly. Use Android, desktop ou JS.")
        }
    }
}
