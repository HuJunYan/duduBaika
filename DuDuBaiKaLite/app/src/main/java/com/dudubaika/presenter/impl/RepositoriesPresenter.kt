package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.Repository
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.RepositoriesContract
import com.dudubaika.util.RxUtil
import javax.inject.Inject


class RepositoriesPresenter @Inject constructor()
    : RxPresenter<RepositoriesContract.View>(), RepositoriesContract.Presenter<RepositoriesContract.View> {

    private val NUM_OF_PAGE = "10"
    private var mCurrentPage = 1


    companion object {
        private const val ORGANIZATION_NAME = "kotlin"
        private const val REPOS_TYPE = "public"
    }

    override fun loadRepositories() {
        mView?.showProgress()
        mCurrentPage = 1
        addSubscribe(ApiManager.loadOrganizationRepos(ORGANIZATION_NAME, REPOS_TYPE, mCurrentPage.toString(), NUM_OF_PAGE)
                .compose(RxUtil.rxSchedulerHelper<MutableList<Repository>>())
                .subscribeWith(object : CommonSubscriber<MutableList<Repository>>(mView!!) {
                    override fun onNext(t: MutableList<Repository>) {
                        mView?.hideProgress()
                        mView?.showRepositories(t!!)
                    }
                }))
    }

    override fun loadRefreshRepositories() {
        mCurrentPage = 1
        addSubscribe(ApiManager.loadOrganizationRepos(ORGANIZATION_NAME, REPOS_TYPE, mCurrentPage.toString(), NUM_OF_PAGE)
                .compose(RxUtil.rxSchedulerHelper<MutableList<Repository>>())
                .subscribeWith(object : CommonSubscriber<MutableList<Repository>>(mView!!) {
                    override fun onNext(t: MutableList<Repository>) {
                        mView?.showRefreshRepositories(t!!)
                    }
                }))
    }

    override fun loadMoreRepositories() {
        mCurrentPage++
        addSubscribe(ApiManager.loadOrganizationRepos(ORGANIZATION_NAME, REPOS_TYPE, mCurrentPage.toString(), NUM_OF_PAGE)
                .compose(RxUtil.rxSchedulerHelper<MutableList<Repository>>())
                .subscribeWith(object : CommonSubscriber<MutableList<Repository>>(mView!!) {
                    override fun onNext(t: MutableList<Repository>) {
                        mView?.showMoreRepositories(t!!)
                    }
                }))
    }

}

