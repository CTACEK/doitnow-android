package com.ctacek.yandexschool.doitnow.data.datasource.retrofit

import com.ctacek.yandexschool.doitnow.data.model.Importance
import com.google.gson.*
import java.lang.reflect.Type
import java.util.Locale

class ImportanceDeserializer : JsonDeserializer<Importance> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Importance {
        val importanceValue = json?.asString?.uppercase(Locale.ROOT)
        return importanceValue?.let { Importance.valueOf(it) } ?: Importance.BASIC
    }
}