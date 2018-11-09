package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.TalkDetailBean

/**
 * Created by 胡俊焰 on 2018/8/4.
 */
interface TalkDetailContact {

    interface View:BaseContract.BaseView{

        fun showDetailData(data: TalkDetailBean)
        fun dissTalkCompete()
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getDeatilData(discuss_id:String)

        fun Talkdiss(discuss_id:String,comment_content:String)

        fun reportTalk(discuss_id:String,report_id:String)
    }

}