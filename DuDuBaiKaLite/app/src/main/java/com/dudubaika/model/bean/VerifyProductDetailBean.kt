package com.dudubaika.model.bean

data class VerifyProductDetailBean(var product_detail_list: ArrayList<ProductDetailBean>) {
    data class ProductDetailBean(var product_url: String, var product_title: String, var product_des: String)
}