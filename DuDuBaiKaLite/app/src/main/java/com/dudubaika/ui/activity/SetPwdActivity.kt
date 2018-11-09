package com.dudubaika.ui.activity

import android.os.Bundle
import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.LoginSuccesfulEvent
import com.dudubaika.event.SetPwdEvent
import com.dudubaika.model.bean.SetPwdBean
import com.dudubaika.presenter.contract.SetPwdContract
import com.dudubaika.presenter.impl.SetPwdPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import kotlinx.android.synthetic.main.activity_set_pwd.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * 设置密码界面
 */
class SetPwdActivity : BaseActivity<SetPwdPresenter>(),SetPwdContract.View {

    private var mMobile:String?=null
    companion object {
        public var PHONENUM="PHONENUM"
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayout(): Int = R.layout.activity_set_pwd

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_set_pwd)

    }

    override fun initData() {
        mMobile = intent.getStringExtra(PHONENUM)
        iv_set_pwd_return.setOnClickListener({
            backActivity()
        })
        set_pwd.setOnClickListener({
            setPwd()
        })
    }

    private fun setPwd() {
        if (TextUtils.isEmpty(pwd.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "密码不能为空")
            return
        }
        if (TextUtils.isEmpty(confirm_pwd.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "确认密码不能为空")
            return
        }

        if (pwd.text.toString().trim() != confirm_pwd.text.toString().trim()) {
            ToastUtil.showToast(mActivity, "两次输入的密码不相同请重新输入")
            return
        }
       /* if (RegexUtil.IsPassword(pwd.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "密码不能含特殊字符，必须为数字和字母")
            return
        }*/
        if (pwd.text.toString().trim().length<6 || pwd.text.toString().trim().length>18){
            ToastUtil.showToast(mActivity,"密码长度必须为6-18位")
            return
        }
        mPresenter.setPwd(mMobile, confirm_pwd.text.toString().trim())
    }


    override fun setPwdComplete(data: SetPwdBean?) {


        EventBus.getDefault().post(SetPwdEvent())
        EventBus.getDefault().post(LoginSuccesfulEvent())
        ToastUtil.showToast(mActivity,"密码设置成功")

        //保存最新的用户值
        UserUtil.saveToken(mActivity,data!!.token)
        UserUtil.saveUserId(mActivity,data!!.customer_id)
        startActivity<MainActivity>()
        finish()
    }

    override fun finshActivity() {

    }
}
