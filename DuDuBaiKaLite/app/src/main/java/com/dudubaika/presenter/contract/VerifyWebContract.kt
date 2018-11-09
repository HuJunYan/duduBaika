package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.VerifyHomeDataBean

interface VerifyWebContract {

    interface View : BaseContract.BaseView {
        fun collectionComplete()
        fun unCollectionComplete()
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun collectionArt(article_id: String)
        fun unCollection(article_id: String)
    }

}
