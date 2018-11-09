package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.FindInitBean
import com.dudubaika.model.bean.UsersAuthLimitBean

/**
 * 机构搜索
 */
interface FindContract {

    interface View:BaseContract.BaseView{

        fun showSortData(data:UsersAuthLimitBean)
        //显示初始化搜索数据
        fun showInitData(data:FindInitBean)

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getSortData(current_page:Int,page_size:String,quota:String,
                        lony_term :String,cycle:String,rate:String,
                        mech:String,mark :String)

        fun getTopData()
    }
}