package com.ctacek.yandexschool.doitnow.utils

import kotlin.reflect.KClass

object ServiceLocator {
    private val instances = mutableMapOf<KClass<*>, Any>()
    inline fun <reified T:Any> register(instance:T) = register(T::class, instance)
    fun <T:Any> register(xClass: KClass<T>, instance:T){
        instances[xClass] = instance
    }
    @Suppress("UNCHECKED_CAST")
    fun <T:Any> get(kClass: KClass<T>):T = instances[kClass] as T

}

inline fun <reified T:Any> locale() = ServiceLocator.get(T::class)

inline fun <reified T:Any> localeLazy():Lazy<T> = lazy { ServiceLocator.get(T::class) }