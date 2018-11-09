package com.dudubaika.model.bean

data class ProvinceBean(var provinceList: ArrayList<ProvinceItemBean>) {
    data class ProvinceItemBean(var province_id: String, var province_name: String)
}