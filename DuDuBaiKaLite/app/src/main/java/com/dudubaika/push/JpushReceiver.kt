package com.dudubaika.push

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.dudubaika.base.App
import com.dudubaika.event.RefreshHomeUserMoney
import com.dudubaika.event.RefreshMsgCenterListData
import com.dudubaika.event.SetMsgIsRead
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.JpushBaseBean
import com.dudubaika.ui.activity.*
import com.dudubaika.util.GsonUtil
import com.dudubaika.util.UserUtil
import org.greenrobot.eventbus.EventBus


/**
 * Created by wang on 2017/12/21.
 */
class JpushReceiver : BroadcastReceiver() {


    companion object {
        private val TAG = "Jpush"
    }

    override fun onReceive(context: Context, intent: Intent) {
        var nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        LogUtil.d(TAG, "JPushInterface onReceive")
        if (intent == null) {
            return
        }

        val bundle = intent.extras
        var result = bundle.getString(JPushInterface.EXTRA_EXTRA)

        try {
            //解析模板
            val jpushBaseBean = GsonUtil.json2bean(result, JpushBaseBean::class.java)
            val action = intent.action
            LogUtil.d(TAG, "JPushInterface action = $action")
            if (TextUtils.isEmpty(action)) {
                return
            }
            if (action == JPushInterface.ACTION_REGISTRATION_ID) {
                val bundle = intent.extras
                val jpush_id = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                LogUtil.d(TAG, "JPushInterface jpush_id = $jpush_id")
            }
            //收到推送
            if (action == JPushInterface.ACTION_NOTIFICATION_RECEIVED) {
                val notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
                LogUtil.d(TAG, "Jpush Received ACTION_NOTIFICATION_RECEIVED, notificationId = $notificationId")

                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val info = activityManager.getRunningTasks(1)[0]
                val className = info.topActivity.shortClassName
                if (null !=className && App.isOnResume){
                    //用户已登录 且在前台

                    if ("3".equals(jpushBaseBean.msg_type)){
                        //发送event 通知首页刷新额度
                        if (UserUtil.isLogin(context) && className.contains(MainActivity::class.java.simpleName)) {
                            EventBus.getDefault().post(RefreshHomeUserMoney())
                        }
                    }

                    if ("2".equals(jpushBaseBean.msg_type) || "1".equals(jpushBaseBean.msg_type)){


                    }

                }

                //打开推送
            } else if (action == JPushInterface.ACTION_NOTIFICATION_OPENED) {
                //用户打开推送
                val bundle = intent.extras
                val result = bundle.getString(JPushInterface.EXTRA_EXTRA)
                LogUtil.d(TAG, "JPushInterface result = $result")
                val jpushBaseBean = GsonUtil.json2bean(result, JpushBaseBean::class.java)
                var msg_type = jpushBaseBean.msg_type
                when (msg_type) {
                    "1" ,"2"-> {
                        //跳转到H5

                        val oneIntent = Intent(context, WebVerifyActivity::class.java)
                        oneIntent.putExtra(WebVerifyActivity.WEB_URL_TITLE,jpushBaseBean.msg_content.msg_title)
                        oneIntent.putExtra(WebVerifyActivity.WEB_URL_KEY,jpushBaseBean.msg_content.msg_url)
                        oneIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        toIntent(oneIntent, context)

                    }
                    "4" -> {
                        //跳转到详情

                    }
                    "3" -> {
                        //

                    }
                    else -> gotoMain(context)
                }
                //发送消息设置为已读
                EventBus.getDefault().postSticky(SetMsgIsRead(jpushBaseBean.msg_content.msg_id))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun gotoMain(context: Context) {
        if (App.isOnResume) {
            return
        }
        val realIntent = Intent(context, NavigationActivity::class.java)
        realIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        toIntent(realIntent, context)
    }



    fun toIntent(intent: Intent, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_SINGLE_TOP
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        context.startActivity(intent)
    }



}