package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import androidx.room.Room
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDao
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataBaseModule {

    @Singleton
    @Provides
    fun provideToDoDao(database: ToDoItemDatabase): ToDoItemDao {
        return database.provideToDoDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): ToDoItemDatabase {
        return Room.databaseBuilder(
            context, ToDoItemDatabase::class.java, "main_database"
        ).build()
    }
}