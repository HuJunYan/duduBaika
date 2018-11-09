package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.AdvertisingBean
import com.dudubaika.model.bean.UpgradeBean

interface NavigationContract {
    interface View : BaseContract.BaseView {
        fun checkUpdateResult(data: UpgradeBean)
        fun  getAdverstData(data: AdvertisingBean)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun checkUpdate()

        fun getAdverst()
        fun startAppTime(deviceId :String)
    }

}