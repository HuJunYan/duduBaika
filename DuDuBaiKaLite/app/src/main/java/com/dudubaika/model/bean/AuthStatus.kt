package com.dudubaika.model.bean

/**
 * Created by wang on 2018/1/30.
 *  "is_auth"：1,"是否开启认证，1:开启，2:不开启",
 *"auth_status": “1:身份认证  2:个人信息  3:紧急联系人  4:运营商  5:淘宝  6.风控中（不可点击）7.风控结果（跳转到我的额度页面）”
 *"mobile_url":运营商url
 */
data class AuthStatus(var is_auth: String, var auth_status: String, var mobile_url: String)