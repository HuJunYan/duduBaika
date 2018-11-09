package com.dudubaika.util

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import okhttp3.RequestBody
import org.json.JSONObject

object RequsetUtil {

    fun getRequestBody(jsonObject: JSONObject): RequestBody {

        val rootJsonObject = JSONObject()

        val timestamp = System.currentTimeMillis().toString()
        val version = Utils.getVersion(App.instance)
        val token = UserUtil.getToken(App.instance)
        val customer_id = UserUtil.getUserId(App.instance)

        rootJsonObject.put("type", "1") //客户端类型0h5,1android,2ios,3winphone
        rootJsonObject.put("platform", GlobalParams.PLATFORM_FLAG)
        rootJsonObject.put("token", token) //token
        rootJsonObject.put("version", version)
        rootJsonObject.put("customer_id", customer_id)
        rootJsonObject.put("timestamp", timestamp)
        rootJsonObject.put("mobile", UserUtil.getMobile(App.instance))
        rootJsonObject.put("data", jsonObject)
        rootJsonObject.put("channel_id", Utils.getChannelId())
        val strEntity = rootJsonObject.toString()
        return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
    }

    fun getUcenterRequestBody(jsonObject: JSONObject): RequestBody {

        val rootJsonObject = JSONObject()

        val timestamp = System.currentTimeMillis().toString()
        val version = Utils.getVersion(App.instance)
        val token = UserUtil.getToken(App.instance)
        val user_id = UserUtil.getUserId(App.instance)

        rootJsonObject.put("type", "1") //客户端类型0h5,1android,2ios,3winphone
        rootJsonObject.put("platform", GlobalParams.PLATFORM_FLAG)
        rootJsonObject.put("token", token) //token
        rootJsonObject.put("version", version)
        rootJsonObject.put("timestamp", timestamp)
        rootJsonObject.put(GlobalParams.USER_ID, user_id)
        rootJsonObject.put("mobile", UserUtil.getMobile(App.instance))
        rootJsonObject.put("data", jsonObject)
        val strEntity = rootJsonObject.toString()
        return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity)
    }
}