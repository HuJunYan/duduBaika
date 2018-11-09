package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.ReportBean

//举报
interface ReportContract {

    interface View:BaseContract.BaseView{

        fun getReportInfoComplete(data:ReportBean)
        fun reportComplete()
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getReportInfo(discuss_id:String)
        fun reportTalk(discuss_id:String,report_id:String)
    }
}