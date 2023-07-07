package com.ctacek.yandexschool.doitnow.di

import android.content.Context
import com.ctacek.yandexschool.doitnow.ViewModelFactory
import com.ctacek.yandexschool.doitnow.di.module.ApiModule
import com.ctacek.yandexschool.doitnow.di.module.ApplicationModule
import com.ctacek.yandexschool.doitnow.di.module.AuthModule
import com.ctacek.yandexschool.doitnow.di.module.DataBaseModule
import com.ctacek.yandexschool.doitnow.di.module.DataSourceModule
import com.ctacek.yandexschool.doitnow.di.module.LoggingInterceptorModule
import com.ctacek.yandexschool.doitnow.di.module.NetworkObserver
import com.ctacek.yandexschool.doitnow.di.module.OkHttpClientModule
import com.ctacek.yandexschool.doitnow.di.module.RepositoryModule
import com.ctacek.yandexschool.doitnow.ui.activity.MainActivity
import com.ctacek.yandexschool.doitnow.ui.fragment.login.LoginFragment
import com.ctacek.yandexschool.doitnow.ui.fragment.main.MainFragment
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.ManageTaskFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class,
        AuthModule::class,
        DataBaseModule::class,
        ApiModule::class,
        OkHttpClientModule::class,
        LoggingInterceptorModule::class,
        NetworkObserver::class,
        DataSourceModule::class,
        ApplicationModule::class]
)
interface AppComponent {

    fun findViewModelFactory(): ViewModelFactory

    fun injectMainActivity(activity: MainActivity)
    fun injectMainFragment(fragment: MainFragment)
    fun injectLoginFragment(fragment: LoginFragment)
    fun injectManageTaskFragment(fragment: ManageTaskFragment)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}