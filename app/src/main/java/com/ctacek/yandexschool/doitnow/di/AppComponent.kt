package com.ctacek.yandexschool.doitnow.di

import android.content.Context
import com.ctacek.yandexschool.doitnow.App
import com.ctacek.yandexschool.doitnow.ViewModelFactory
import com.ctacek.yandexschool.doitnow.di.module.ApiModule
import com.ctacek.yandexschool.doitnow.di.module.DataBaseModule
import com.ctacek.yandexschool.doitnow.di.module.DataSourceModule
import com.ctacek.yandexschool.doitnow.di.module.LoggingInterceptorModule
import com.ctacek.yandexschool.doitnow.di.module.NetworkObserver
import com.ctacek.yandexschool.doitnow.di.module.OkHttpClientModule
import com.ctacek.yandexschool.doitnow.di.module.RepositoryModule
import com.ctacek.yandexschool.doitnow.ui.activity.MainActivity
import com.ctacek.yandexschool.doitnow.ui.fragment.LoginFragment
import com.ctacek.yandexschool.doitnow.ui.fragment.MainFragment
import com.ctacek.yandexschool.doitnow.ui.fragment.NewEditTaskFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class,
    DataBaseModule::class,
    ApiModule::class,
    OkHttpClientModule::class,
    LoggingInterceptorModule::class,
    NetworkObserver::class,
    DataSourceModule::class])
interface AppComponent {

    fun injectLoginFragment(fragment: LoginFragment)
    fun findViewModelFactory() : ViewModelFactory

    fun injectMainActivity(activity: MainActivity)
    fun injectMainFragment(fragment: MainFragment)
    fun injectAddEditFragment(fragment: NewEditTaskFragment)



    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}