package com.dudubaika.presenter.impl

import cn.finalteam.galleryfinal.model.PhotoInfo
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.TianShenApiManager
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
 * 发布文章
 */
class WriteMyTalkPresenter @Inject constructor():RxPresenter<WriteMyTalkContract.View>(),WriteMyTalkContract.Presenter{

    //发布文章
    override fun writeArtice(type:String,discuss_title: String, discuss_content: String,resultList: MutableList<PhotoInfo>?) {
        //"type":'1/2'  // 1=贷款论坛；2=信用卡论坛
        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("discuss_title", discuss_title)
        jsonObject.put("discuss_content", discuss_content)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        //用户选择的照片
        val files = arrayListOf<MultipartBody.Part>()
        if (resultList!!.size>0) {
            for (item in resultList!!) {
                val file = File(item.photoPath)
                val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)
                val fileBody = MultipartBody.Part.createFormData(file.name, file.name, requestFile)
                files.add(fileBody)
            }
        }else{
            val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"),"")
            val fileBody = MultipartBody.Part.createFormData("file", "无图.png", requestFile)
            files.add(fileBody)
        }

        addSubscribe(ApiManager.sendMyTalk(body, files)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.SAVE_DISCUSS) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.writeComplete()
                    }
                }))
    }


}