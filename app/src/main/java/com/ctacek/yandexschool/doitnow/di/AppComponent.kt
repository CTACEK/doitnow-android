package com.ctacek.yandexschool.doitnow.di

import android.content.Context
import com.ctacek.yandexschool.doitnow.App
import com.ctacek.yandexschool.doitnow.ViewModelFactory
import com.ctacek.yandexschool.doitnow.di.module.ApplicationModule
import com.ctacek.yandexschool.doitnow.di.module.DataBaseModule
import com.ctacek.yandexschool.doitnow.di.module.DataSourceModule
import com.ctacek.yandexschool.doitnow.di.module.NetworkModule
import com.ctacek.yandexschool.doitnow.di.module.NetworkObserver
import com.ctacek.yandexschool.doitnow.di.module.RepositoryModule
import com.ctacek.yandexschool.doitnow.di.module.WorkManagerModule
import com.ctacek.yandexschool.doitnow.di.subcomp.LoginComponent
import com.ctacek.yandexschool.doitnow.ui.activity.MainActivity
import com.ctacek.yandexschool.doitnow.ui.fragment.managetask.ManageTaskFragment
import com.ctacek.yandexschool.doitnow.utils.PeriodWorkManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(
    modules = [NetworkObserver::class,
        ApplicationModule::class,
        NetworkModule::class,
        DataBaseModule::class,
        DataSourceModule::class,
        RepositoryModule::class,
        WorkManagerModule::class],
)
interface AppComponent {

    fun loginComponent(): LoginComponent.Factory
    fun injectApplication(app: App)

    fun injectWorkManager(workManager: PeriodWorkManager)
    fun findViewModelFactory(): ViewModelFactory
    fun injectManageTaskFragment(fragment: ManageTaskFragment)
    fun injectMainActivity(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}

@Scope
annotation class AppScope
