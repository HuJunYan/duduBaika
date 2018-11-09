package com.dudubaika.model.bean

data class UpgradeBean(var app_type: String = "1", var download_url: String = "",
                       var introduction: String = "", var force_upgrade: String = "0",
                       var on_verify: String = "0", var android_md5: String = "0", var is_ignore: String = "1",
                       var user_service_url: String = "", var service_online_url: String = "", var register_url: String = "")