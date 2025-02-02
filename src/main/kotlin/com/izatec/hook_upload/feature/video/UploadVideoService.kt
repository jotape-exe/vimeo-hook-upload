package com.izatec.hook_upload.feature.video

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import java.io.File

@Service
class UploadVideoService(
    val client: OkHttpClient
) {

    fun uploadVideoInChunks(videoUrl: String, file: File, chunkSize: Long = 1024 * 1024): Boolean {

        val mediaType = "application/offset+octet-stream".toMediaType()
        val totalSize = file.length()
        var offset: Long = 0

        while (offset < totalSize) {
            val bytesToRead = minOf(chunkSize, totalSize - offset)
            val chunk = ByteArray(bytesToRead.toInt())

            file.inputStream().use { inputStream ->
                inputStream.skip(offset)
                inputStream.read(chunk)
            }

            val requestBody = chunk.toRequestBody(mediaType)
            val request = Request.Builder()
                .url(videoUrl)
                .patch(requestBody)
                .addHeader("Tus-Resumable", "1.0.0")
                .addHeader("Upload-Offset", offset.toString())
                .addHeader("Content-Type", "application/offset+octet-stream")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        offset += bytesToRead
                    } else {
                        return@use
                    }
                }
            } catch (err: Exception) {
                err.printStackTrace()
                break
            }
        }

        return offset == totalSize
    }
}