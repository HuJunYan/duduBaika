package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.*


/**
 * Created by admin on 2018/1/16.
 */
interface AuthInfoContract {

    interface View : BaseContract.BaseView {
        fun getUserInfoResult(data: UserInfoBean?)
        fun getProviceResult(data: ProvinceBean?)

        fun getCityResult(data: CityBean?)

        fun getCountyResult(data: CountyBean?)

        fun saveUserInfoComplete()
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun getUserInfo()
        fun getProvinceData()

        fun getCityData(provinceId: String?)

        fun getCountyData(cityId: String?)

        fun saveUserInfo(hashMap: HashMap<Int, AddressBean>, user_address_detail: String, company_name: String,
                         company_phone: String, company_address_detail: String, qq_num: String, selected_occupation_name: String)
    }
}