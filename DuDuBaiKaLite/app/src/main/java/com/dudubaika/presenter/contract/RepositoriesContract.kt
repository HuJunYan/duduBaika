package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.Repository

interface RepositoriesContract {

    interface View : BaseContract.BaseView {
        fun showRepositories(repositories: MutableList<Repository>)
        fun showRefreshRepositories(repositories: MutableList<Repository>)
        fun showMoreRepositories(repositories: MutableList<Repository>)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun loadRepositories()
        fun loadRefreshRepositories()
        fun loadMoreRepositories()
    }
}