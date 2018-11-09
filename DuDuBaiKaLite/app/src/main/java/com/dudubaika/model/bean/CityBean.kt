package com.dudubaika.model.bean

data class CityBean(var cityList: ArrayList<CityItemBean>) {
    data class CityItemBean(var city_id: String, var city_name: String)
}