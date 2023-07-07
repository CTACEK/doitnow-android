package com.ctacek.yandexschool.doitnow.di.subcomp

import com.ctacek.yandexschool.doitnow.di.module.AuthModule
import com.ctacek.yandexschool.doitnow.ui.fragment.login.LoginFragment
import dagger.Subcomponent
import javax.inject.Scope

@FragmentScope
@Subcomponent(modules = [AuthModule::class])
interface LoginComponent {

    fun injectYandexAuth(fragment : LoginFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }
}

@Scope
annotation class FragmentScope
