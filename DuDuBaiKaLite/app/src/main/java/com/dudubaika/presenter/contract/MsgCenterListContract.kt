package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.MsgCenterListBean

/**
 * 消息中心
 */
interface MsgCenterListContract {

    interface View:BaseContract.BaseView{
        fun showDtaa(data: MsgCenterListBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{
        fun getListData(now_page:String,page_size:String,table_identifier:String,table_page_offset:String)

    }
}