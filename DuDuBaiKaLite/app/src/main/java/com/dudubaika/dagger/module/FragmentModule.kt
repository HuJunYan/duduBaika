package com.dudubaika.dagger.module

import android.app.Activity
import android.support.v4.app.Fragment
import com.dudubaika.dagger.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(val fragment: Fragment) {

    @Provides
    @FragmentScope
    fun provideActivity(): Activity = fragment.activity!!

}
