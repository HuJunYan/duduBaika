package com.dudubaika.model.bean

data class VerifyHomeDataBean(var banner_list: ArrayList<VerifyBannerBean>, var hot_list: ArrayList<ProductBean>) {
    data class VerifyBannerBean(var img_url: String, var article_title: String, var jump_url: String)

    data class ProductBean(var product_id: String, var logo_url: String,
                           var product_name: String, var product_des: String,
                           var quota_limit: String, var apply_count: String, var local_item_type: Int){
    }

}