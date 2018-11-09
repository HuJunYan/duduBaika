package com.dudubaika.model.bean

/**
 * Created by wang on 2018/1/30.
 */
data class UserInfoBean(var user_name: String, var user_mobile: String,
                        var user_address_provice: String, var user_address_city: String, var user_address_county: String,
                        var user_address_detail: String, var company_name: String, var company_phone: String,
                        var company_address_provice: String, var company_address_city: String, var company_address_county: String,
                        var company_address_detail: String, var qq_num: String, var selected_occupation_name: String,
                        var occupation: ArrayList<OccupationBean>) {
    data class OccupationBean(var occupation_id: String, var occupation_name: String, var sub_occupation: ArrayList<SubOccupation>)
    data class SubOccupation(var sub_id: String, var sub_name: String)
}
