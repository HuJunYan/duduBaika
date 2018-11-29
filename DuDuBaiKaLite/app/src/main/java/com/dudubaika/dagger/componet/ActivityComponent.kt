package com.dudubaika.dagger.componet

import android.app.Activity
import com.dudubaika.dagger.module.ActivityModule
import com.dudubaika.dagger.scope.ActivityScope
import com.dudubaika.ui.activity.*
import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun getActivity(): Activity

    fun inject(activity: NavigationActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: SetPwdActivity)
    fun inject(activity: ForgetPwdActivity)
    fun inject(activity: RegestActivity)
    fun inject(activity: SettingActivity)
    fun inject(activity: VerifyWebActivity)
    fun inject(activity: ReviewLoginActivity)

}
