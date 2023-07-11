package com.ctacek.yandexschool.doitnow.di.subcomp

import com.ctacek.yandexschool.doitnow.di.customscope.FragmentScope
import com.ctacek.yandexschool.doitnow.di.module.AuthModule
import com.ctacek.yandexschool.doitnow.ui.fragment.login.LoginFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AuthModule::class])
interface LoginComponent {

    fun injectYandexAuth(fragment : LoginFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }
}

