package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.ContactsBean
import com.dudubaika.model.bean.ExtroContactsBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.AuthExtroContactsContract
import com.dudubaika.util.GsonUtil
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

/**
 * Created by admin on 2018/1/16.
 */
class AuthExtroContactsPresenter @Inject constructor() : RxPresenter<AuthExtroContactsContract.View>(), AuthExtroContactsContract.Presenter<AuthExtroContactsContract.View> {
    /**
     * 上传通讯录
     */
    override fun uploadContacts(list: List<ContactsBean>) {
        val jsonObject = JSONObject()
        jsonObject.put("contact_list", JSONArray(GsonUtil.bean2json(list)))
        val jsonObjectSigned = SignUtils.signJsonContainList(jsonObject, "contact_list")
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.saveContacts(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        mView?.processUploadContactsResult()

                    }
                }))
    }

    override fun getExtroContacts() {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getExtroContacts(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ExtroContactsBean>>())
                .compose(RxUtil.handleResult<ExtroContactsBean>())
                .subscribeWith(object : CommonSubscriber<ExtroContactsBean>(mView!!, false, true, ApiSettings.GET_EXTRO_CONTACTS) {
                    override fun onNext(t: ExtroContactsBean) {
                        super.onNext(t)
                        mView?.processExtroData(t)
                    }
                }))
    }

    override fun saveExtroContacts(map: HashMap<String, ExtroContactsBean.EmeContacts>) {
        val jsonObject = JSONObject()
        val jsonArray = JSONArray()
        for (key in map.keys) {
            val emeContacts = map.get(key)
            if (emeContacts != null) {
                var jsonObject = JSONObject()
                jsonObject.put("type", emeContacts.type)
                jsonObject.put("contact_name", emeContacts.contact_name)
                jsonObject.put("contact_phone", emeContacts.contact_phone)
                jsonArray.put(jsonObject)
            }
        }

        jsonObject.put("eme_contacts", jsonArray)
        val jsonObjectSigned = SignUtils.signJsonContainList(jsonObject, "eme_contacts")
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.saveExtroContacts(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        mView?.processUploadResult()
                    }
                }))
    }


}