plugins {
  `kotlin-dsl`
}

gradlePlugin {
  plugins.register("check-size-plugin") {
    id = "check-size-plugin"
    implementationClass = "com.ctacek.yandexschool.checksize.CheckSizePlugin"
  }
  plugins.register("upload-tg-plugin") {
    id = "upload-tg-plugin"
    implementationClass = "com.ctacek.yandexschool.uploadtg.UploadPlugin"
  }
}

dependencies {
  implementation(libs.agp)
  implementation(libs.kotlin.gradle.plugin)
  implementation(libs.bundles.ktor)
}
