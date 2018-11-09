package com.dudubaika.model.bean

/**
 * Created by wang on 2018/1/30.
 */
data class CountyBean(var countyList: ArrayList<CountyItemBean>) {
    data class CountyItemBean(var county_id: String, var county_name: String)
}
