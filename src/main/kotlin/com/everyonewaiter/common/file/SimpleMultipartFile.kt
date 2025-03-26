package com.everyonewaiter.common.file

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream

class SimpleMultipartFile(
    private val name: String,
    private val content: ByteArray,
    private val originalFilename: String? = null,
    private val contentType: String? = null,
) : MultipartFile {
    override fun getName(): String = name

    override fun getOriginalFilename(): String? = originalFilename

    override fun getContentType(): String? = contentType

    override fun isEmpty(): Boolean = content.isEmpty()

    override fun getSize(): Long = content.size.toLong()

    override fun getBytes(): ByteArray = content

    override fun getInputStream(): InputStream = content.inputStream()

    override fun transferTo(dest: File) {
        dest.outputStream().use { it.write(content) }
    }
}
