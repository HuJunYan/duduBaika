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
    fun inject(activity: RecommendActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: SetPwdActivity)
    fun inject(activity: ForgetPwdActivity)
    fun inject(activity: RegestActivity)
    fun inject(activity: AuthInfoActivity)
    fun inject(activity: AuthExtroContactsActivity)
    fun inject(activity: CreditAssessmentActivity)
    fun inject(activity: IdentityActivity)
    fun inject(activity: SettingActivity)
    fun inject(activity: OpinionUpActivity)
    fun inject(activity: RecommendResultActivity)
    fun inject(activity: DetailInfoActivity)
    fun inject(activity: VerifyWebActivity)
    fun inject(activity: MyCollectionActivity)
    fun inject(activity: ReviewLoginActivity)
    fun inject(activity: ProductDetailActivity)
    fun inject(activity: SuggestionResultActivity)
    fun inject(activity: ProductInfoActivity)
    fun inject(activity: LoanBooksActivity)
    fun inject(activity: DayWeekMonthLoanActivity)
    fun inject(activity: LoanDetailActivity)
    fun inject(activity: AddLoanInfoActivity)
    fun inject(activity: PrductListSimpleActivity)
    fun inject(activity: SearchActivity)
    fun inject(activity: ProductFormeActivity)
    fun inject(activity: JpushDetailActivity)
    fun inject(activity: MsgCenterListActivity)
    fun inject(activity: HelpCenterActivity)
    fun inject(activity: BankListActivity)
    fun inject(activity: CreditCardDetailActivity)
    fun inject(activity: FindActivity)
    fun inject(activity: FindDetailActivity)
    fun inject(activity: TalkHomeActivity)
    fun inject(activity: MineTalkActivity)
    fun inject(activity: WriteMyTalkActivity)
    fun inject(activity: SearchTalkActivity)
    fun inject(activity: TalkDetailActivity)
    fun inject(activity: ReporTalkActivity)
    fun inject(activity: TagProductActivity)

}
