package com.dudubaika.model.bean

/**
 * 首页推荐位bean
 */
data class HomeTopInfo(var quota_desc:String,var quota_value:String,var quota_click:String,var msg_list: ArrayList<String>,var title_list: ArrayList<TitleListBean>,var recommend_list:List<RecommendList>) {

    data class RecommendList(var product_id:String,var recommend_title:String,var recommend_name:String,var recommend_logo:String,var quota_name:String
                             ,var quota_start_value:String ,var quota_start_unit:String,var quota_end_value:String
                             ,var quota_end_unit:String   ,var rate_name:String,var rate_value:String,var rate_unit:String
                             ,var term_name:String,var term_start_value:String,var term_start_unit:String,var term_end_value:String
                             ,var term_end_unit:String,var recommend_des:String,var recommend_button_name:String)



data class TitleListBean(var title_id:String,var title_name: String)

}