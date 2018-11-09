package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.MineLoanBean


interface MineLoanContract {

    interface View: BaseContract.BaseView {
        fun showData(data:MineLoanBean)
        fun changeComplete()
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getData(date_flag:String)
        fun changeNoteStatus(product_id:String)

    }
}