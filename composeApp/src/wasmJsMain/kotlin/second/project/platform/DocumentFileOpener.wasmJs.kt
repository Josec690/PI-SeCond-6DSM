package second.project.platform

suspend actual fun openDocumentFile(fileName: String, contentType: String, bytes: ByteArray) {
    throw UnsupportedOperationException("Download de arquivo interno ainda nao esta disponivel no alvo WebAssembly.")
}
