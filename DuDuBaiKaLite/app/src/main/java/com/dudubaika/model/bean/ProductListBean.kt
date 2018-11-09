package com.dudubaika.model.bean

data class ProductListBean(var top_list: ArrayList<ProductBean>,
                           var top_title: String = "",
                           var hot_title: String = "",
                           var hot_list: ArrayList<ProductBean>) {
    data class ProductBean(var product_id: String, var logo_url: String,
                           var product_name: String, var product_des: String,
                           var product_type: String, var quota_limit: String,
                           var loan_time: String, var apply_count: String)
}