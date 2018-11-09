package com.dudubaika.dagger.componet

import android.content.Context
import com.dudubaika.dagger.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun getContext(): Context
}
