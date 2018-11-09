package com.dudubaika.presenter.impl

import cn.finalteam.galleryfinal.model.PhotoInfo
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CardMoneyListBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.TianShenApiManager
import com.dudubaika.presenter.contract.SearchTalkContract
import com.dudubaika.presenter.contract.WriteMyTalkContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

/**
 * Created by 胡俊焰 on 2018/8/4.
 *
 * 搜索帖子
 */
class SearchTalkPresenter @Inject constructor():RxPresenter<SearchTalkContract.View>(),SearchTalkContract.Presenter{
    override fun search(discuss_search: String, type: String) {
        val jsonObject = JSONObject()
        jsonObject.put("discuss_search", discuss_search)
        jsonObject.put("type", type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.searchTalk(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CardMoneyListBean>>())
                .compose(RxUtil.handleResult<CardMoneyListBean>())
                .subscribeWith(object : CommonSubscriber<CardMoneyListBean>(mView, true, ApiSettings.DISCUSS_SEARCH) {
                    override fun onNext(data: CardMoneyListBean) {
                        mView?.showSearchListData(data)
                    }
                }))

    }

}