package com.dudubaika.ui.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.FinishSetingActivityEvent
import com.dudubaika.event.LogOutEvent
import com.dudubaika.presenter.contract.LoginOutContract
import com.dudubaika.presenter.impl.LoginOutPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.dialog_islogin_out.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity

/**
 * 设置界面
 */
class SettingActivity : BaseActivity<LoginOutPresenter>(), LoginOutContract.View {

    private var pwd:String?=null
    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    private var mDialog:Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
    override fun getLayout(): Int =  R.layout.activity_setting

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_setting)
        defaultTitle = "设置"
        tv_title.text = defaultTitle
        about_we.setOnClickListener({startActivity<AboutWeActivity>()})
        if (UserUtil.getIsNew(mActivity)){
            pwd= "修改登录密码"
        }else{
            pwd= "设置登录密码"
        }
        change_pwd.text = pwd
        rl_change_pwd.setOnClickListener({startActivity<ForgetPwdActivity>(ForgetPwdActivity.TITLE to pwd!!)})
        setting_return.setOnClickListener({backActivity()})
        rl_login_out.setOnClickListener({
            showIsloginOutDialog()

        })

        notice_layout.setOnClickListener {
            //通知
            if (UserUtil.isLogin(mActivity)) {
                startActivity<NoticeActivity>()
            }else{
                startActivity<LoginActivity>()
            }
        }

    }

    /**
     * 弹窗
     */
    private fun showIsloginOutDialog() {
        mDialog = Dialog(this,R.style.MyDialog)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_islogin_out, null)
        view.dialog_confirm.setOnClickListener({
            //退出登录
           mPresenter.loginOut()

        })

        view.dialog_cancle.setOnClickListener({
            //取消
            mDialog?.dismiss()
        })
        mDialog?.setContentView(view)
        mDialog?.setCancelable(true)
        mDialog?.show()

    }

    override fun initData() {
    }

    override fun logingOutComplete() {
        TCAgent.onEvent(mActivity, TalkingDataParams.LOGIN_OUT)
        ToastUtil.showToast(mActivity,"您已退出登录")
        mDialog?.dismiss()
        startActivity<MainActivity>()
        UserUtil.clearUser(mActivity)
        EventBus.getDefault().post(LogOutEvent())
        finish()
    }

    override fun finishActivity() {

    }


    @Subscribe
    fun finishSetiing(event: FinishSetingActivityEvent){
        finish()
    }
}
