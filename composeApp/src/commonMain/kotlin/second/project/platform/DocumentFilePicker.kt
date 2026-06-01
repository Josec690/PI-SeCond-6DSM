package second.project.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberDocumentFilePicker(
    onFileSelected: (SelectedDocumentFile) -> Unit,
    onError: (String) -> Unit
): () -> Unit
