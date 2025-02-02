package com.izatec.hook_upload.feature.video

import com.google.gson.Gson
import com.izatec.hook_upload.infra.config.VimeoProperties
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class CreateVideoService(
    private val gson: Gson,
    private val client: OkHttpClient,
    private val properties: VimeoProperties
) {

    fun create(payload: CreateVideoPayload): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = gson.toJson(payload).toRequestBody(mediaType)

        val request = Request.Builder()
            .url(properties.url)
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/vnd.vimeo.*+json;version=3.4")
            .addHeader("Authorization", "bearer ${properties.token}")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (response.code > 299) throw IOException("Erro na requisição: ${response.code}")
                response.body?.string()?.let { responseBody ->
                    gson.fromJson(responseBody, UploadVideoResponse::class.java)?.upload?.uploadLink
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
