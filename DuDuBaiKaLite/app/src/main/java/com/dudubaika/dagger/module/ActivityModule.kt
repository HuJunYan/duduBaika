package com.dudubaika.dagger.module

import android.app.Activity
import com.dudubaika.dagger.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: Activity) {

    @Provides
    @ActivityScope
    fun provideActivity(): Activity = activity
}
