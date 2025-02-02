package com.izatec.hook_upload.presentation

import com.izatec.hook_upload.feature.video.CreateVideoPayload
import com.izatec.hook_upload.feature.video.CreateVideoService
import com.izatec.hook_upload.feature.video.UploadVideoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File

@RestController
@RequestMapping("iza-webhook")
class VimeoController(
    private val createVideoService: CreateVideoService,
    private val uploadVideoService: UploadVideoService
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createAndUploadVideo(
        @RequestPart("payload") createVideo: CreateVideoPayload,
        @RequestPart("video") video: MultipartFile
    ): ResponseEntity<String> {

        createVideo.upload.size = video.size

        val uploadLink = createVideoService.create(createVideo)
            ?: return ResponseEntity(
                "Falha ao obter o link de upload.",
                HttpStatus.INTERNAL_SERVER_ERROR
            )

        val videoFile = File(System.getProperty("java.io.tmpdir"), video.originalFilename ?: "video.mp4")
        video.inputStream.use { inputStream ->
            videoFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return if (uploadVideoService.uploadVideoInChunks(uploadLink, videoFile)) {
            ResponseEntity("Upload completo!", HttpStatus.OK)
        } else {
            ResponseEntity("Erro durante o upload do vídeo.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
