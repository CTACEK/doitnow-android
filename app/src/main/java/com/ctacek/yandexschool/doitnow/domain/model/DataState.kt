package com.ctacek.yandexschool.doitnow.domain.model

sealed class DataState<out T> {
    object Initial : DataState<Nothing>()
    data class Result<T>(val data: T) : DataState<T>()
    data class Exception(val cause: Throwable): DataState<Nothing>()
}
