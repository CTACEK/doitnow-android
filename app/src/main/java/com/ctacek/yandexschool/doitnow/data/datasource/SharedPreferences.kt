package com.ctacek.yandexschool.doitnow.data.datasource

import android.content.Context
import java.util.UUID

class SharedPreferencesAppSettings(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setCurrentToken(token: String?) {
        if (token == null)
            editor.remove(PREF_CURRENT_ACCOUNT_TOKEN)
        else
            editor.putString(PREF_CURRENT_ACCOUNT_TOKEN, token)
        editor.apply()
    }

    fun getCurrentToken(): String? = sharedPreferences.getString(PREF_CURRENT_ACCOUNT_TOKEN, null)

    fun createDeviceId() {
        editor.putString(DEVICE_TAG, UUID.randomUUID().toString())
        editor.apply()
    }

    fun getDeviceId() {
        sharedPreferences.getString(DEVICE_TAG, null)
    }

    fun putRevisionId(revision: Int) {
        editor.putInt(REVISION_TAG, revision)
        editor.apply()
    }

    fun getRevisionId() {
        sharedPreferences.getString(REVISION_TAG, null)
    }

    companion object {
        private const val PREF_CURRENT_ACCOUNT_TOKEN = "currentToken"
        private const val REVISION_TAG = "currentRevision"
        private const val DEVICE_TAG = "currentDevice"
    }

}