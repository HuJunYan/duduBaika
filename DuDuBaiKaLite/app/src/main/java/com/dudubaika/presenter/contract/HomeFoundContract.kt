package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.HomeFoundBean


//发现首页
interface HomeFoundContract{

    interface View: BaseContract.BaseView{

        fun showData(bean: HomeFoundBean)


    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun loadData()
    }



}