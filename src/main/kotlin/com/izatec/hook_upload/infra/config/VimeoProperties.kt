package com.izatec.hook_upload.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vimeo")
data class VimeoProperties(
    val token: String,
    val url: String
)