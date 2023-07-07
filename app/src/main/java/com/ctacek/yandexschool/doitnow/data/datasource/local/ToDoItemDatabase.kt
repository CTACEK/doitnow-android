package com.ctacek.yandexschool.doitnow.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {
    abstract fun provideToDoDao(): ToDoItemDao
}
