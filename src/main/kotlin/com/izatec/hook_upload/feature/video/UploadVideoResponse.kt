package com.izatec.hook_upload.feature.video

import com.google.gson.annotations.SerializedName

data class UploadVideoResponse(
    @SerializedName("upload") val upload: UploadLink
)

data class UploadLink(
    @SerializedName("upload_link") val uploadLink: String
)
