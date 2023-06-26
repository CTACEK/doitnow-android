//package com.ctacek.yandexschool.doitnow
//
//import com.ctacek.yandexschool.doitnow.data.datasource.retrofit.RetrofitFactory
//import com.ctacek.yandexschool.doitnow.data.model.Importance
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
//fun main() {
//    val service = RetrofitFactory.makeRetrofitService()
//    runBlocking {
//        val response = service.getList()
//        try {
//            if (response.isSuccessful) {
//                println(response.body()?.list?.get(0)?.toToDoItem())
//            } else {
//                println("Error network operation failed with ${response.code()}")
//            }
//
//        } catch (e: HttpException) {
//            println("Exception ${e.message}")
//        } catch (e: Throwable) {
//            println("Ooops: Something else went wrong")
//        }
//    }
//}