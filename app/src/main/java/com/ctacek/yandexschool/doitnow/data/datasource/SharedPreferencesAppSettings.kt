package com.ctacek.yandexschool.doitnow.data.datasource

import android.content.Context
import com.ctacek.yandexschool.doitnow.utils.Constants.TOKEN_API
import com.ctacek.yandexschool.doitnow.utils.Constants.NO_TOKEN

import java.util.UUID

class SharedPreferencesAppSettings(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        if (!sharedPreferences.contains(DEVICE_TAG)) {
            editor.putString(DEVICE_TAG, UUID.randomUUID().toString())
            editor.apply()
        }

        if (!sharedPreferences.contains(PREF_CURRENT_ACCOUNT_TOKEN)) {
            setCurrentToken(NO_TOKEN)
        }
    }

    fun setCurrentToken(token: String) {
        editor.putString(PREF_CURRENT_ACCOUNT_TOKEN, token)
        editor.apply()
    }

    fun getCurrentToken(): String = sharedPreferences.getString(PREF_CURRENT_ACCOUNT_TOKEN, null) ?: "Bearer $TOKEN_API"

    fun getDeviceId() : String {
        return sharedPreferences.getString(DEVICE_TAG, null)?: "0d"
    }

    fun putRevisionId(revision: Int) {
        editor.putInt(REVISION_TAG, revision)
        editor.apply()
    }

    fun getRevisionId() : Int {
        return sharedPreferences.getInt(REVISION_TAG, 1)
    }

    companion object {
        private const val PREF_CURRENT_ACCOUNT_TOKEN = "currentToken"
        private const val REVISION_TAG = "currentRevision"
        private const val DEVICE_TAG = "currentDevice"
    }

}