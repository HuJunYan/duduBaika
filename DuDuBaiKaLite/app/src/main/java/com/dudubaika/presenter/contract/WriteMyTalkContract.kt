package com.dudubaika.presenter.contract

import cn.finalteam.galleryfinal.model.PhotoInfo
import com.dudubaika.base.BaseContract

/**
 * Created by 胡俊焰 on 2018/8/4.
 */
interface WriteMyTalkContract{

    interface View:BaseContract.BaseView{

        fun writeComplete()

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun writeArtice(type:String,discuss_title:String,discuss_content:String,resultList: MutableList<PhotoInfo>?)
    }
}