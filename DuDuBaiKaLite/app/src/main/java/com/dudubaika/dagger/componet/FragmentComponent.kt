package com.dudubaika.dagger.componet

import android.app.Activity
import com.dudubaika.dagger.module.FragmentModule
import com.dudubaika.dagger.scope.FragmentScope
import com.dudubaika.ui.fragment.*
import dagger.Component

@FragmentScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun getActivity(): Activity

    fun inject(fragment: HomeFragment)
    fun inject(fragment2: HomeFragment2)
    fun inject(fragment2: HotProductFragment)
    fun inject(fragment2: RapidFragment)
    fun inject(fragment: DhFragment)
    fun inject(fragment: YhFragment)
    fun inject(fragment: CardFragment)
    fun inject(fragment: CreditCardFragment)

    fun inject(fragment: VerifyHomeFragment)
    fun inject(fragment: VerifyMeFragment)
    fun inject(fragment: ThreeFragment)
    fun inject(fragment: FourFragment)
    fun inject(fragment: FiveFragment)
    fun inject(fragment: SixFragment)
    fun inject(fragment: MeFragment)
    fun inject(fragment: FoundFragment)

}
