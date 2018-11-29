package com.dudubaika.ui.fragment

import android.text.TextUtils
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.ui.activity.*
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.UserUtil
import com.dudubaika.util.Utils
import kotlinx.android.synthetic.main.fragment_me.*
import org.jetbrains.anko.support.v4.startActivity
import com.dudubaika.base.BaseFragment
import com.dudubaika.event.IsHideMyious
import com.dudubaika.event.RecordStopAppEvent
import com.dudubaika.event.SetMsgIsRead
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.IshaveNoReadMsgBean
import com.dudubaika.presenter.contract.MeContract
import com.dudubaika.presenter.impl.MePresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat


/**
 * 我的页面
 */
class MeFragment : BaseFragment<MePresenter>(), MeContract.View {
    override fun setReadSuccess() {
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() = Unit

    override fun hideProgress() =Unit

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int = R.layout.fragment_me

    override fun initView() {
        defaultTitle="我的"
        refreshUI()
    }

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.setPaddingSmart(activity, tb_me)
    }

    override fun initData() {

        iv_center_msg.setOnClickListener {
            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            } else {
            }
        }

        iv_me_avatar.setOnClickListener({

            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            } else {
            }
        })

        rl_avatar_root.setOnClickListener({

            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            }
        })

        rl_setting.setOnClickListener({
            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            } else {
                startActivity<SettingActivity>()
            }

        })

        rl_my_info.setOnClickListener({

            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            } else {
                //发送event 查询用户的认证状态
                EventBus.getDefault().post(getUserAuthStatus())
            }

        })
        rl_opinion_up.setOnClickListener({

            if (!UserUtil.isLogin(activity!!)) {
                startActivity<LoginActivity>()
            } else {
            }
        })
        rl_service_online.setOnClickListener {

            if (!UserUtil.isLogin(activity!!)){
                startActivity<LoginActivity>()
            }else{
            }
        }
        //帮助中心
        rl_help_center.setOnClickListener {

            if (!UserUtil.isLogin(activity!!)){
                startActivity<LoginActivity>()
            }else{
            }
        }
        rl_weixin.setOnClickListener({
        })
    }

    //未登录UI
    fun refreshUI() {
        if (UserUtil.isLogin(App.instance)) {
            //设置手机号
            val PhoneNum = UserUtil.getMobile(activity!!)
            val encryptPhoneNum = Utils.encryptPhoneNum(PhoneNum)
            phone_num.text = encryptPhoneNum
        } else {
            //未登录
            phone_num.text = "登录/注册"
        }
    }

    override fun isHaveNoreadMsg(data: IshaveNoReadMsgBean) {

        if (null==data){
            return
        }
        if ("1"==data.is_new_msg){
            iv_center_msg.setImageResource(R.drawable.jpush_have_msg)
        }else{
            iv_center_msg.setImageResource(R.drawable.jpush_no_msg)
        }
    }



    override fun onResume() {
        super.onResume()
        if (UserUtil.isLogin(mContext!!)) {
            mPresenter.getMsgStatus()
        }
    }

    @Subscribe
    public fun OnIsHideMyious(event: IsHideMyious){
        if ("1"==event.tag){
            rl_my_info.visibility = View.VISIBLE
        }else{
            rl_my_info.visibility = View.GONE
        }

    }

    @Subscribe
    public fun OnSetMsgIsRead(event: SetMsgIsRead){

        if (!TextUtils.isEmpty(event.msgId)){
            //单个设为已读
            mPresenter.setRead(event.msgId,"2","")
        }else{
            //全部设为已读
            mPresenter.setRead("","1","")

        }
    }
    @Subscribe
    public fun getRecordStopAppEvent(event: RecordStopAppEvent) {

        Thread({
            mPresenter.stopAppTime(UserUtil.getDeviceId(mContext!!))
        }).start()
    }

}