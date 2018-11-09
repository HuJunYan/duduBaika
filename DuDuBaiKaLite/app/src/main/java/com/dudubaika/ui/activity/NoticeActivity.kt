package com.dudubaika.ui.activity

import android.widget.CompoundButton
import cn.jpush.android.api.JPushInterface
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.SimpleActivity
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.UserUtil
import kotlinx.android.synthetic.main.activity_notice.*
import java.util.*

//通知开启 关闭界面
class NoticeActivity : SimpleActivity() {
    override fun getLayout(): Int = R.layout.activity_notice

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb)
        defaultTitle="推送设置"

        iv_return.setOnClickListener {
            backActivity()
        }

        jpush_msg.setOnCheckedChangeListener { buttonView, isChecked ->

            if(jpush_msg.isChecked){
                //打开推送
                JPushInterface.resumePush(App.instance)
            }else{
                //关闭推送
                JPushInterface.stopPush(App.instance)
                UserUtil.savePushNoticeTime(mActivity,Date().toString().trim())
//                UserUtil.savePushNoticeTime(mActivity,"Thu Sep 13 17:49:23 GMT+08:00 2017")
            }


        }

    }

    override fun initData() {
//        isPushStopped  是否已经停止
        jpush_msg.isChecked = !JPushInterface.isPushStopped(App.instance)

    }


}
