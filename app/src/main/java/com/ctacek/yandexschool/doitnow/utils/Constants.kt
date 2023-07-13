package com.ctacek.yandexschool.doitnow.utils

object Constants {
    const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    const val TOKEN_API = "wiseacreish"

    const val WORK_MANAGER_TAG = "update_data"

    const val NOTIFICATION_CHANNEL_ID = "deadlines"
    const val NOTIFICATION_CHANNEL_NAME = "Task's deadlines"


    const val TAG_NOTIFICATION_TASK = "task"

    const val DATABASE_NAME = "main_database"

    const val SHARED_PREFERENCES_NAME = "settings"
    const val SHARED_PREFERENCES_TOKEN = "current_token"
    const val SHARED_PREFERENCES_REVISION_TAG = "current_revision"
    const val SHARED_PREFERENCES_DEVICE_TAG = "current_device_id"
    const val SHARED_PREFERENCES_NO_TOKEN = "no_token"
    const val SHARED_PREFERENCES_NOTIFICATION_STATUS = "notification_status"
    const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001


    const val SHARED_PREFERENCES_THEME_OPTION = "theme_option"


    const val REQUEST_LOGIN_SDK_CODE = -1

    const val REPEAT_INTERVAL: Long = 8
    const val RETROFIT_TIMEOUT : Long = 10

    const val ONE_HOUR_IN_MILLIS = 3600000
    const val ONE_DAY_IN_MILLIS = 86400000
}
