package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract

/**
 * Created by admin on 2018/2/27.
 */
class OpionContract {

    interface View:BaseContract.BaseView{

        fun upLoadComplete()
    }

    interface Presenter:BaseContract.BasePresenter<View>{
        fun upLoadOpion(feed_content:String?,mobile:String?)

    }
}