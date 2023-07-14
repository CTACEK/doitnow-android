package com.ctacek.yandexschool.doitnow.data.model

enum class Importance {
    LOW, BASIC, IMPORTANT
}

fun Importance.toName() : String = when(this){
    Importance.LOW -> "Low"
    Importance.BASIC -> "Basic"
    Importance.IMPORTANT -> "!! Important"
}
