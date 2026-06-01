package second.project.platform

import kotlinx.browser.document
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

suspend actual fun openDocumentFile(fileName: String, contentType: String, bytes: ByteArray) {
    val typedArray = Int8Array(bytes.size)
    bytes.forEachIndexed { index, byte ->
        typedArray.asDynamic()[index] = byte
    }

    val blob = Blob(arrayOf(typedArray), BlobPropertyBag(type = contentType.ifBlank { "application/octet-stream" }))
    val url = URL.createObjectURL(blob)
    val anchor = document.createElement("a") as HTMLAnchorElement
    anchor.href = url
    anchor.download = fileName.ifBlank { "documento" }
    anchor.click()
    URL.revokeObjectURL(url)
}
