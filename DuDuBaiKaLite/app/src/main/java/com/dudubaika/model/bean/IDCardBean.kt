package com.dudubaika.model.bean

import com.google.gson.annotations.SerializedName

data class IDCardBean(var name: String, var time_used: String, var gender: String,
                      var id_card_number: String, var request_id: String, var side: String,
                      var race: String, var address: String, var issued_by: String,
                      var valid_date: String, var birthday: Birthday, var legality: Legality) {
    data class Birthday(var month: String, var day: String, var year: String)
    data class Legality(
            @SerializedName("ID Photo")
            var ID_Photo: Double,//身份证
            var Edited: Double,//编辑过的
            var Photocopy: Double,//复印件
            var Screen: Double,//屏幕
            @SerializedName("Temporary ID Photo")
            var Temporary_ID_Photo: Double //临时的
    )
}