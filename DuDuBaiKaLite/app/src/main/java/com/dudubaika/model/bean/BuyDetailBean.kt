package com.dudubaika.model.bean

/**
 * Created by wang on 2018/1/2.
 */
data class BuyDetailBean(var product_id: String, var product_name: String, var product_logo_url: String, var product_apply_count: String,
                         var product_loan_limit: String, var product_loan_term: String, var product_day_rate: String, var product_day_rate_desc: String,
                         var product_apply_limit: String, var product_credit_info: ArrayList<ProductCreditInfo>) {
    data class ProductCreditInfo(var logo_url: String, var credit_name: String)
}
