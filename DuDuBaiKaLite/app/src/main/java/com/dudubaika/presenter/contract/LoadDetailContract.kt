package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.LoanDetailBean

/**
 * 我的账单详情
 */
interface LoadDetailContract {

    interface View: BaseContract.BaseView{
        fun showData(data: LoanDetailBean)
        fun addLoanComplete()
        fun finishActivity()

    }

    interface Presenter:BaseContract.BasePresenter<View>{
        fun getLoanDetailData(product_id:String)
        fun addLoanInfo(notes_id:String?,product_id:String?,
                        product_name:String,loan_money:String,
                        loan_date:String,loan_term:String,repay_date:String)

    }
}