package com.ctacek.yandexschool.doitnow.di.module

import android.content.Context
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
interface AuthModule {
    companion object {
        @Reusable
        @Provides
        fun provideYandexAuthSdk(context: Context): YandexAuthSdk {
            return YandexAuthSdk(context, YandexAuthOptions(context, true))
        }
    }
}
