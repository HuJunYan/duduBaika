package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.ProductListBean
import com.dudubaika.model.bean.ProductRecommentBean
import com.dudubaika.model.bean.UserAuthResultBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.RepositoriesRusultContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.Tools
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import javax.inject.Inject


class RepositoriesRusultPresenter @Inject constructor()
    : RxPresenter<RepositoriesRusultContract.View>(), RepositoriesRusultContract.Presenter<RepositoriesRusultContract.View> {

    override fun productRecommend() {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.productRecommend(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductRecommentBean>>())
                .compose(RxUtil.handleResult<ProductRecommentBean>())
                .subscribeWith(object : CommonSubscriber<ProductRecommentBean>(mView!!, false, true) {

                    override fun onStart() {
                        super.onStart()
                        mView?.showProgress()
                    }

                    override fun onNext(t: ProductRecommentBean) {
                        mView?.showProductRecommentTop(t)
                    }
                }))
    }

    override fun getProductList(product_type: String) {
        val jsonObject = JSONObject()
        jsonObject.put("use_flag", "2")
        val root = Tools.isRoot()
        jsonObject.put("is_root", if (root) "1" else "2");
        jsonObject.put("product_type", product_type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getProductList(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductListBean>>())
                .compose(RxUtil.handleResult<ProductListBean>())
                .subscribeWith(object : CommonSubscriber<ProductListBean>(mView!!, false, true) {

                    override fun onNext(t: ProductListBean) {

                        doAsync {
                            //模拟加载中
                            Thread.sleep(1500)
                            uiThread {
                                mView?.hideProgress()
                                mView?.showProduct(t)
                            }
                        }
                    }

                }))

    }

    override fun getUserAuthResult() {
        val jsonObject = JSONObject()
        val root = Tools.isRoot()
        jsonObject.put("is_root", if (root) "1" else "2");
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getUserAuthResult(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UserAuthResultBean>>())
                .compose(RxUtil.handleResult<UserAuthResultBean>())
                .subscribeWith(object : CommonSubscriber<UserAuthResultBean>(mView!!, true) {
                    override fun onNext(t: UserAuthResultBean) {
                        mView?.onUserAuthResult(t)
                    }
                }))
    }


}