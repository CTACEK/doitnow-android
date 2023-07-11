package com.ctacek.yandexschool.doitnow.di.module

import com.ctacek.yandexschool.doitnow.data.repository.ToDoItemsRepositoryImpl
import com.ctacek.yandexschool.doitnow.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface RepositoryModule {
    @Reusable
    @Binds
    fun bindTodoRepository(todoRepository: ToDoItemsRepositoryImpl): Repository
}
