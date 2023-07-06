package com.ctacek.yandexschool.doitnow.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {
    abstract fun provideToDoDao(): ToDoItemDao
//    companion object {
//        @Volatile
//        private var INSTANCE: ToDoItemDatabase? = null
//
//        fun getDatabase(context: Context): ToDoItemDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context,
//                    ToDoItemDatabase::class.java,
//                    "main_database"
//                )
//                    .build()
//                INSTANCE = instance
//
//                instance
//            }
//        }
//
//        fun getDatabaseInstance(): ToDoItemDatabase {
//            return INSTANCE ?: throw IllegalStateException("Database not initialized")
//        }
//    }
}