package com.izatec.hook_upload.feature.video

import com.google.gson.annotations.SerializedName

data class CreateVideoPayload(
    val upload: Upload = Upload(),
    val name: String,
    val description: String,
    val privacy: Privacy = Privacy(),
    @SerializedName("folder_uri") val folderUri: String
)

data class Upload(
    val approach: String = "tus",
    var size: Long? = null
)

data class Privacy(
    val view: String = View.NOBODY.lowercase(),
    val embed: String = Embed.PRIVATE.lowercase(),
    // val download: Boolean = false
)

@OptIn(kotlin.ExperimentalStdlibApi::class)
enum class View {
    ANYBODY,
    CONTACTS,
    DISABLE,
    NOBODY,
    PASSWORD,
    UNLISTED;

    fun lowercase(): String = name.lowercase()
}

@OptIn(kotlin.ExperimentalStdlibApi::class)
enum class Embed {
    PRIVATE,
    PUBLIC;

    fun lowercase(): String = name.lowercase()
}
