package com.dudubaika.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.tbruyelle.rxpermissions2.RxPermissions

object UserUtil {
    val DEVICE_ID: String = "device_id"
    val TOKEN: String = "token"
    val CUSTOMER_ID: String = "customer_id"
    val MOBILE: String = "mobile"
    val JPUSH_ID: String = "jpush_id"
    val IS_MEMBER: String = "is_new"
    val IS_WHITE: String = "is_white"
    val IS_FIRST: String = "is_first"
    val USER_SERVICE_URL = "user_service_url"
    val SERVICE_ONLINE_URL = "service_online_url"
    val AUTHOR = "author"
    val ISUPLOAD = "isupload"
    val ISSTARTAPP = "isstartapp"
    //app启动时间
    val APPSTARTTIME = "app_start_time"
    //推送提醒
    val PRIEV = "priev"


    @SuppressLint("MissingPermission")
            /**
     * 得到设备ID
     */
    fun getDeviceId(activity: Activity): String {
        var device_id = SharedPreferencesUtil.getInstance(activity).getString(DEVICE_ID)

        if (!TextUtils.isEmpty(device_id)) {
            return device_id
        }

        val rxPermissions = RxPermissions(activity)

        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe {
            if (it) {
                val telephoneManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                device_id = telephoneManager.deviceId
            }
        }
        if (TextUtils.isEmpty(device_id)) {
            device_id = System.currentTimeMillis().toString()
        }
        UserUtil.saveDeviceId(activity, device_id)
        return device_id
    }

    /**
     * 保存设备ID
     */
    fun saveDeviceId(context: Context, device_id: String) {
        SharedPreferencesUtil.getInstance(context).putString(DEVICE_ID, device_id)
    }

    /**
     * 得到设备ID 给结束app的使用
     */
    fun getDeviceId(context: Context) :String{
       val id = SharedPreferencesUtil.getInstance(context).getString(DEVICE_ID)
        return if (TextUtils.isEmpty(id)){
            ""
        }else{
            id
        }
    }


    /**
     * 保存Token
     */
    fun saveToken(context: Context, token: String) {
        SharedPreferencesUtil.getInstance(context).putString(TOKEN, token)
    }

    /**
     * 得到Token
     */
    fun getToken(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(TOKEN)


    /**
     * 得到用户ID
     */
    fun getUserId(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(CUSTOMER_ID)

    /**
     * 保存customer_id
     */
    fun saveUserId(context: Context, customer_id: String) {
        SharedPreferencesUtil.getInstance(context).putString(CUSTOMER_ID, customer_id)
    }

    /**
     * 得到手机号
     */
    fun getMobile(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(MOBILE)

    /**
     * 保存手机号
     */
    fun saveMobile(context: Context, mobile: String) {
        SharedPreferencesUtil.getInstance(context).putString(MOBILE, mobile)
    }

    /**
     * 获取用户jpush_id
     */
    fun getUserJPushId(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(JPUSH_ID)

    fun saveUserJPushId(context: Context, jpush_id: String) {
        SharedPreferencesUtil.getInstance(context).putString(JPUSH_ID, jpush_id)
    }

    /**
     *得到新手引导
     */
    fun getUserIsMember(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(IS_MEMBER)

    /**
     * 保存是否显示过新手引导
     */
    fun saveUserIsMember(context: Context, isMember: Boolean) {
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_MEMBER, isMember)
    }

    /**
     *得到用户是否设置密码
     */
    fun getIsNew(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(IS_MEMBER)

    /**
     * 保存用户是否设置密码
     * 1已设置密码 2没有设置密码
     */
    fun saveIsNew(context: Context, isNew: String) {
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_MEMBER, false)
        if ("1".equals(isNew)) {
            SharedPreferencesUtil.getInstance(context).putBoolean(IS_MEMBER, true)
        }
    }

    /**
     *得到是否显示过新手引导
     */
    fun getUserIsWhite(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(IS_WHITE)

    /**
     * 保存是否显示过新手引导
     */
    fun saveUserIsWhite(context: Context, isWhite: Boolean) {
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_WHITE, isWhite)
    }

 /**
     *得到贷款记录是否显示过新手引导
     */
    fun getIsfirst(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(IS_FIRST)

    /**
     * 保存贷款记录是否显示过新手引导
     */
    fun saveIsfirst(context: Context, isFirst: Boolean) {
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_FIRST, isFirst)
    }

    /**
     *得到用户协议url
     */
    fun getUserServiceUrl(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(USER_SERVICE_URL)

    /**
     * 保存用户注册协议url
     */
    fun saveUserServiceUrl(context: Context, url: String) {
        SharedPreferencesUtil.getInstance(context).putString(USER_SERVICE_URL, url)
    }

    /**
     *得到客服url
     */
    fun getServiceOnlineUrl(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(SERVICE_ONLINE_URL)

    /**
     * 保存客服url
     */
    fun saveServiceOnlineUrl(context: Context, url: String) {
        SharedPreferencesUtil.getInstance(context).putString(SERVICE_ONLINE_URL, url)
    }

    /**
     *获取认证中心url协议
     */
    fun getAuthorUrl(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(AUTHOR)

    /**
     * 保存认证中心url协议
     */
    fun saveAuthorUrl(context: Context, url: String) {
        SharedPreferencesUtil.getInstance(context).putString(AUTHOR, url)
    }

    /**
     *获取用户是否上传过手机信息的标识
     */
    fun isUpLoadPhoneInfo(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(ISUPLOAD)

    /**
     * 保存用户上传信息的标识
     */
    fun savePhoneInfo(context: Context, flag: Boolean) {
        SharedPreferencesUtil.getInstance(context).putBoolean(ISUPLOAD, flag)
    }

    /**
     * 判断是否登录
     */
    fun isLogin(context: Context): Boolean = !TextUtils.isEmpty(getToken(context))

    /**
     *得到是否启动了app
     */
    fun getIsStartApp(context: Context): Boolean =
            SharedPreferencesUtil.getInstance(context).getBoolean(ISSTARTAPP)

    /**
     * 保存启动过App
     */
    fun saveIsStartApp(context: Context, isStart: Boolean) {
        SharedPreferencesUtil.getInstance(context).putBoolean(ISSTARTAPP, isStart)
    }


    /**
     *用户上次推送 打开app的时间点
     */
    fun getPushNoticeTime(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(PRIEV)

    /**
     * 保存用户上次点击拒绝的时间
     */
    fun savePushNoticeTime(context: Context, isStart: String) {
        SharedPreferencesUtil.getInstance(context).putString(PRIEV, isStart)
    }


    /**
     *用户本次使用app的时时间点
     */
    fun getAppStartTime(context: Context): String =
            SharedPreferencesUtil.getInstance(context).getString(APPSTARTTIME)

    /**
     * 保存用户启动App的时间
     */
    fun saveAppStartTime(context: Context, isStart: String) {
        SharedPreferencesUtil.getInstance(context).putString(APPSTARTTIME, isStart)
    }



    /**
     * 清除所有的用户信息
     */
    fun clearUser(context: Context) {
        /*--------  用户协议 不清除-------------*/
        val userServiceUrl = getUserServiceUrl(context)
        val serviceOnline = getServiceOnlineUrl(context)
        SharedPreferencesUtil.getInstance(context).clearSp()
        saveUserServiceUrl(context, userServiceUrl)
        saveServiceOnlineUrl(context,serviceOnline)
        /*--------  用户协议 不清除-------------*/
    }


}