package com.ctacek.yandexschool.doitnow.utils

object Constants {
    const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    const val TOKEN_API = "wiseacreish"

    const val WORK_MANAGER_TAG = "update_data"

    const val TAG_NOTIFICATION_CHANNEL_ID = "Tasks"
    const val TAG_NOTIFICATION_TASK = "task_id"
    const val TAG_NOTIFICATION_DEADLINE = "task_deadline"
    const val TAG_NOTIFICATION_IMPORTANCE = "task_importance"
    const val TAG_NOTIFICATION_DESCRIPTION = "task_description"



    const val DATABASE_NAME = "main_database"

    const val SHARED_PREFERENCES_NAME = "settings"
    const val SHARED_PREFERENCES_TOKEN = "current_token"
    const val SHARED_PREFERENCES_REVISION_TAG = "current_revision"
    const val SHARED_PREFERENCES_DEVICE_TAG = "current_device_id"
    const val SHARED_PREFERENCES_NO_TOKEN = "no_token"

    const val SHARED_PREFERENCES_THEME_OPTION = "theme_option"


    const val REQUEST_LOGIN_SDK_CODE = -1

    const val REPEAT_INTERVAL: Long = 8
    const val RETROFIT_TIMEOUT : Long = 10
}
