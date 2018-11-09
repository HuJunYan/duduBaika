package com.dudubaika.model.bean

data class BannerListBean(var banner_list: ArrayList<BannerBean>) {
    data class BannerBean(var banner_url: String, var jump_type: String,
                          var jump_url: String, var product_id: String)
}