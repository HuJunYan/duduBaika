package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CardDetailBean
import com.dudubaika.model.bean.VerifyProductDetailBean

/**
 * Created by lenovo on 2018/3/28.
 */
class TransficCardConstract : BaseContract {

    interface View:BaseContract.BaseView{
        fun show_product_detail(cardBean: VerifyProductDetailBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun setCard_id(product_id:String)
    }
}