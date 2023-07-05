package com.ctacek.yandexschool.doitnow.data.model

sealed class LoadingState<out T> {
    data class Success<out T>(val data: T): LoadingState<T>()
    data class Loading<out T>(val isLoading: Boolean): LoadingState<T>()
    data class Error<T>(val nameError: String): LoadingState<T>()
}