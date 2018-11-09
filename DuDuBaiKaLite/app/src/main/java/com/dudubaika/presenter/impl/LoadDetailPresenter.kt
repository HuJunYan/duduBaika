package com.dudubaika.presenter.impl

import android.text.TextUtils
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.LoanDetailBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.LoadDetailContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 获取用户账单详情
 */
class LoadDetailPresenter @Inject constructor():RxPresenter<LoadDetailContract.View>(),LoadDetailContract.Presenter{

    //添加账单信息
    override fun addLoanInfo(notes_id: String?, product_id: String?, product_name: String, loan_money: String, loan_date: String, loan_term: String, repay_date: String) {


        val jsonObject = JSONObject()
        jsonObject.put("notes_id",notes_id)
        jsonObject.put("product_id",product_id)
        jsonObject.put("product_name",product_name)
        jsonObject.put("loan_money",loan_money)
        jsonObject.put("loan_date",loan_date)
        jsonObject.put("loan_term",loan_term)
        jsonObject.put("repay_date",repay_date)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.addOrChangeLoan(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: Any) {
                        mView?.addLoanComplete()
                    }

                    override fun onComplete() {
                        super.onComplete()
                        mView?.finishActivity()
                    }
                }))

    }

    override fun getLoanDetailData(product_id: String) {

        val jsonObject = JSONObject()
        jsonObject.put("product_id",product_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getLoanDetail(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LoanDetailBean>>())
                .compose(RxUtil.handleResult<LoanDetailBean>())
                .subscribeWith(object : CommonSubscriber<LoanDetailBean>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: LoanDetailBean) {
                        mView?.showData(data)
                    }
                }))
    }

}