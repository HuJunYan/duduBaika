package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.UsersAuthLimitBean

/**
 * 获取用户额度
 */
interface GetUserLoanContract{


    interface View:BaseContract.BaseView{

        fun showData(data: UsersAuthLimitBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getData()
    }

}