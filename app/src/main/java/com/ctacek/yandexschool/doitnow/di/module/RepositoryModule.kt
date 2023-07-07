package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.data.datasource.SharedPreferencesAppSettings
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDao
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.data.datasource.remote.RemoteDataSourceImpl
import com.ctacek.yandexschool.doitnow.data.datasource.remote.ToDoItemService
import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindTodoRepository(todoRepository: ToDoItemsRepositoryImpl): Repository
}