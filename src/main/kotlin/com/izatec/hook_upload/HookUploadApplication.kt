package com.izatec.hook_upload

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class HookUploadApplication

fun main(args: Array<String>) {
	runApplication<HookUploadApplication>(*args)
}
