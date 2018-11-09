package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CreditAssessBean
import com.dudubaika.model.bean.CreidtApplyBean
import com.dudubaika.model.bean.LastSmsTimeBean
import org.json.JSONObject

/**
 * Created by admin on 2018/1/16.
 */
object CreditAssessmentContract {

    interface View : BaseContract.BaseView {
        fun getCreditAssessResult(data: CreditAssessBean?, isNeedJump: Boolean)
        fun processLastSmsTime(t: LastSmsTimeBean?, isFirst: Boolean)
        fun processSubmitPhoneInfoResult()
        fun processCreditApplyData(t: CreidtApplyBean?)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun getCreditAssessData(isNeedJump: Boolean)
        fun submitCredit()
        fun getLastSmsTime(isFirst: Boolean)
        fun submitPhoneInfo(data: JSONObject, deviceId: String)
    }
}