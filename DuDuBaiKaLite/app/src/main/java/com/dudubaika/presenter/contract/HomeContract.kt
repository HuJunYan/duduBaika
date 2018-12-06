package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.*

interface HomeContract {

    interface View : BaseContract.BaseView {

        fun showBannerInfo(data: HomeTopInfo)

        fun  showHomeBottomData(data: HomeButtomDialogBean)

        fun showDialogForUser(data: HomeDialogForUser)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {

        //首页推荐位信息获取
        fun getBannerInfo(isShowLoading: Boolean)

        //获取首页底部信息
        fun getBottomInfo(isShowLoading: Boolean)

        //获取弹窗
        fun getDialogForUser()

        //测试
        fun test()

    }

}