package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CardMoneyListBean

/**
 * Created by 胡俊焰 on 2018/8/4.
 * 搜索
 */
interface SearchTalkContract{

    interface View:BaseContract.BaseView{

        fun showSearchListData(data:CardMoneyListBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun search( discuss_search:String,type:String)
    }
}