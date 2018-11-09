package com.dudubaika.model.bean

data class CreditAssessBean(var required_list: ArrayList<CreditAssessItemBean>,
                            var not_required_list: ArrayList<CreditAssessItemBean>) {
    data class CreditAssessItemBean(var item_num: String, var item_icon: String,
                                    var item_name: String, var item_status: String,
                                    var item_is_click: String, var jump_url: String,
                                    var local_item_type: Int, var local_item_title: String,
                                    var local_item_des: String, var local_item_is_required: Boolean) {
        constructor(local_item_type: Int, local_item_title: String, local_item_des: String, local_item_is_required: Boolean)
                : this("", "", "", "", "", "", local_item_type, local_item_title, local_item_des, local_item_is_required)

        constructor(item_num: String, item_icon: String,
                    item_name: String, item_status: String,
                    item_is_click: String, jump_url: String,
                    local_item_type: Int) : this(item_num, item_icon, item_name, item_status, item_is_click, jump_url, local_item_type, "", "", false)
    }

}