package com.ctacek.yandexschool.doitnow.data.datasource

import android.content.Context
import com.ctacek.yandexschool.doitnow.utils.Constants.TOKEN_API
import com.ctacek.yandexschool.doitnow.utils.Constants.SHARED_PREFERENCES_NO_TOKEN
import com.ctacek.yandexschool.doitnow.utils.Constants.SHARED_PREFERENCES_NAME
import com.ctacek.yandexschool.doitnow.utils.Constants.SHARED_PREFERENCES_DEVICE_TAG
import com.ctacek.yandexschool.doitnow.utils.Constants.SHARED_PREFERENCES_TOKEN
import com.ctacek.yandexschool.doitnow.utils.Constants.SHARED_PREFERENCES_REVISION_TAG


import java.util.UUID
import javax.inject.Inject

class SharedPreferencesAppSettings @Inject constructor(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        if (!sharedPreferences.contains(SHARED_PREFERENCES_DEVICE_TAG)) {
            editor.putString(SHARED_PREFERENCES_DEVICE_TAG, UUID.randomUUID().toString())
            editor.apply()
        }

        if (!sharedPreferences.contains(SHARED_PREFERENCES_TOKEN)) {
            setCurrentToken(SHARED_PREFERENCES_NO_TOKEN)
        }
    }

    fun setCurrentToken(token: String) {
        editor.putString(SHARED_PREFERENCES_TOKEN, token)
        editor.apply()
    }

    fun getCurrentToken(): String =
        sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: "Bearer $TOKEN_API"

    fun getDeviceId(): String {
        return sharedPreferences.getString(SHARED_PREFERENCES_DEVICE_TAG, null) ?: "0d"
    }

    fun putRevisionId(revision: Int) {
        editor.putInt(SHARED_PREFERENCES_REVISION_TAG, revision)
        editor.apply()
    }

    fun getRevisionId(): Int {
        return sharedPreferences.getInt(SHARED_PREFERENCES_REVISION_TAG, 1)
    }
}
