//package com.ctacek.yandexschool.doitnow.data.datasource.local
//
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.ctacek.yandexschool.doitnow.data.model.Importance
//import com.github.javafaker.Faker
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.UUID
//import javax.inject.Inject
//import kotlin.random.Random
//
//class ToDoRandomItemCallback @Inject constructor(private val itemDao: ToDoItemDao) : RoomDatabase.Callback() {
//
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        super.onCreate(db)
//        // Вызывается при создании базы данных
//
//        // Запускаем корутину для заполнения базы данных
//        CoroutineScope(Dispatchers.IO)
//            .launch(Dispatchers.IO) {
//
//                val faker = Faker.instance()
//
//                // Генерируем случайные данные и добавляем их в базу данных
//                for (i in 1..30) {
//                    val tempItem = ToDoItemEntity(
//                        UUID.randomUUID().toString(),
//                        if (Random.nextBoolean()) faker.backToTheFuture().character()
//                            .toString() else faker.harryPotter().quote(),
//                        getRandomPriority(),
//                        if (Random.nextBoolean()) faker.date().birthday().time else null,
//                        Random.nextBoolean(),
//                        faker.date().birthday().time,
//                        null
//                    )
//
//                    itemDao.createItem(tempItem)
//                }
//            }
//    }
//
//    private fun getRandomPriority(): Importance {
//        val list = listOf(Importance.LOW, Importance.BASIC, Importance.IMPORTANT)
//        return list[Random.nextInt(0, 3)]
//    }
//}