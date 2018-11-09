package com.dudubaika.presenter.impl

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.AuthInfoContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.UserUtil
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/1/16.
 */
class AuthInfoPresenter @Inject constructor() : RxPresenter<AuthInfoContract.View>(), AuthInfoContract.Presenter<AuthInfoContract.View> {
    override fun saveUserInfo(hashMap: HashMap<Int, AddressBean>, user_address_detail: String, company_name: String,
                              company_phone: String, company_address_detail: String, qq_num: String, selected_occupation_name: String) {
        val jsonObject = JSONObject()
        val userAdd = hashMap.get(1)
        val companyAdd = hashMap.get(2)
        var user_address_provice = ""
        var user_address_city = ""
        var user_address_county = ""
        var company_address_provice = ""
        var company_address_city = ""
        var company_address_county = ""
        if (userAdd != null) {
            user_address_provice = userAdd.province
            user_address_city = userAdd.city
            user_address_county = userAdd.county
        }
        if (companyAdd != null) {
            company_address_provice = companyAdd.province
            company_address_city = companyAdd.city
            company_address_county = companyAdd.county
        }
        jsonObject.put("user_address_provice", user_address_provice)
        jsonObject.put("user_address_city", user_address_city)
        jsonObject.put("user_address_county", user_address_county)
        jsonObject.put("user_address_detail", user_address_detail)
        jsonObject.put("company_name", company_name)
        jsonObject.put("company_phone", company_phone)
        jsonObject.put("company_address_provice", company_address_provice)
        jsonObject.put("company_address_city", company_address_city)
        jsonObject.put("company_address_county", company_address_county)
        jsonObject.put("company_address_detail", company_address_detail)
        jsonObject.put("qq_num", qq_num)
        jsonObject.put("selected_occupation_name", selected_occupation_name)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.saveUserInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(data: Any) {
                    }

                    override fun onComplete() {
                        super.onComplete()
                        mView?.saveUserInfoComplete()
                    }
                }))
    }

    override fun getUserInfo() {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getUserInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UserInfoBean>>())
                .compose(RxUtil.handleResult<UserInfoBean>())
                .subscribeWith(object : CommonSubscriber<UserInfoBean>(mView!!, false, true, url = ApiSettings.GET_USER_INFO) {
                    override fun onNext(t: UserInfoBean) {
                        super.onNext(t)
                        mView?.getUserInfoResult(t)
                    }
                }))
    }


    override fun getCountyData(cityId: String?) {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        jsonObject.put("cityId", cityId)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getCounty(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CountyBean>>())
                .compose(RxUtil.handleResult<CountyBean>())
                .subscribeWith(object : CommonSubscriber<CountyBean>(mView!!, true) {
                    override fun onNext(data: CountyBean) {
                        mView?.getCountyResult(data)
                    }
                }))
    }

    override fun getProvinceData() {

        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getProvince(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProvinceBean>>())
                .compose(RxUtil.handleResult<ProvinceBean>())
                .subscribeWith(object : CommonSubscriber<ProvinceBean>(mView!!, true) {
                    override fun onNext(data: ProvinceBean) {
                        mView?.getProviceResult(data)
                    }
                }))
    }

    override fun getCityData(provinceId: String?) {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        jsonObject.put("provinceId", provinceId)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getCity(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CityBean>>())
                .compose(RxUtil.handleResult<CityBean>())
                .subscribeWith(object : CommonSubscriber<CityBean>(mView!!, true) {
                    override fun onNext(data: CityBean) {
                        mView?.getCityResult(data)
                    }
                }))
    }


}