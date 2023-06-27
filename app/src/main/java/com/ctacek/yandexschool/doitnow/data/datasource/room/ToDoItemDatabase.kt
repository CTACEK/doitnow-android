package com.ctacek.yandexschool.doitnow.data.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemEntity::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao

    private lateinit var applicationContext: Context

    companion object {
        @Volatile
        private var INSTANCE: ToDoItemDatabase? = null

        fun getDatabase(context: Context): ToDoItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ToDoItemDatabase::class.java,
                    "main_database"
                )
//                    .addCallback(ToDoRandomItemCallback())
                    .build()
                INSTANCE = instance

                instance
            }
        }

        fun getDatabaseInstance(): ToDoItemDatabase {
            return INSTANCE ?: throw IllegalStateException("Database not initialized")
        }
    }

    fun init(context: Context) {
        applicationContext = context
    }


}