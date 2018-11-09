package com.dudubaika.model.bean

/**
 * Created by lenovo on 2018/3/28.
 */
class CardDetailBean(var product_detail_list: ArrayList<cardItemDetailBean>) {
    data class cardItemDetailBean( var product_title: String, var product_des: String,
                                  var product_url: String)
}