package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import androidx.room.Room
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDao
import com.ctacek.yandexschool.doitnow.data.datasource.local.ToDoItemDatabase
import com.ctacek.yandexschool.doitnow.di.AppScope
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
interface DataBaseModule {
    companion object {
        @Reusable
        @Provides
        fun provideToDoDao(database: ToDoItemDatabase): ToDoItemDao {
            return database.provideToDoDao()
        }

        @AppScope
        @Provides
        fun provideDataBase(context: Context): ToDoItemDatabase {
            return Room.databaseBuilder(context, ToDoItemDatabase::class.java, "main_database")
                .build()
        }
    }
}
