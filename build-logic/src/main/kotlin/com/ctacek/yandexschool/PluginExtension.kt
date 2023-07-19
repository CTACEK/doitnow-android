package com.ctacek.yandexschool

import org.gradle.api.provider.Property

interface PluginExtension {

    val fileSizeLimitInMb: Property<Int>

    val enableSizeCheck: Property<Boolean>


}
