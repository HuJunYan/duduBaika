package com.dudubaika.ui.activity

import android.text.InputType
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.FinishSetingActivityEvent
import com.dudubaika.event.LogOutEvent
import com.dudubaika.presenter.contract.ForgetPwdContract
import com.dudubaika.presenter.impl.ResetPwdPrsenter
import com.dudubaika.util.*
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * 忘记密码界面
 */
class ForgetPwdActivity : BaseActivity<ResetPwdPrsenter>(), ForgetPwdContract.View {

    private var mTimount: TimeCount? = null
    private var mTitle: String? = null

    companion object {
        val TITLE: String = "title"
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        if (mTimount != null) {
            mTimount?.finish()
        }
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun getLayout(): Int = R.layout.activity_forget_pwd

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_forget_pwd)

        pwd_islook.setOnClickListener {
            if (pwd_islook.isChecked){
                pwd.inputType  = InputType.TYPE_CLASS_TEXT
                pwd.transformationMethod =null
                confirm_pwd.inputType  =InputType.TYPE_CLASS_TEXT
                confirm_pwd.transformationMethod =null
            }else{

                pwd.inputType = InputType.TYPE_CLASS_TEXT
                pwd.transformationMethod = PasswordTransformationMethod.getInstance()

                confirm_pwd.inputType = InputType.TYPE_CLASS_TEXT
                confirm_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun initData() {
        mTitle = intent.getStringExtra(TITLE)
        defaultTitle= "忘记密码"
        if (!TextUtils.isEmpty(mTitle)) {
            tv_home_title.text = mTitle
            defaultTitle = mTitle!!
        }
        when(defaultTitle){
            "忘记密码"-> TCAgent.onEvent(mActivity, TalkingDataParams.FORGET_PWD)
            "修改登录密码"->TCAgent.onEvent(mActivity, TalkingDataParams.CHANGE_PWD)
            else->TCAgent.onEvent(mActivity, TalkingDataParams.SET_PWD)
        }

        phone_num.setText(UserUtil.getMobile(mActivity))
        phone_num.isEnabled = TextUtils.isEmpty(UserUtil.getMobile(mActivity))

        get_code.setOnClickListener({ getVerifyCode() })
        tv_reset_pwd.setOnClickListener({ resetPwd() })
        iv_reset_return.setOnClickListener({ backActivity() })
        tv_login.setOnClickListener { backActivity() }
    }

    private fun resetPwd() {
        if (TextUtils.isEmpty(phone_num.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "手机号不能为空")
            return
        }
        if (!RegexUtil.IsTelephone(phone_num.text.toString())) {
            ToastUtil.showToast(mActivity,"手机号格式不正确")
            return
        }
        if (TextUtils.isEmpty(very_code.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "验证码不能为空")
            return
        }
        if (TextUtils.isEmpty(pwd.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "密码不能为空")
            return
        }

        if (pwd.text.toString().trim() != confirm_pwd.text.toString().trim()) {
            ToastUtil.showToast(mActivity, "您输入的两次密码不同，请重新输入")
            return
        }
        /* if (!RegexUtil.IsPassword(pwd.text.toString().trim())){
             ToastUtil.showToast(mActivity,"密码不能含特殊字符，必须为数字和字母")
             return
         }*/
        if (pwd.text.toString().trim().length < 6 || pwd.text.toString().trim().length > 18) {
            ToastUtil.showToast(mActivity, "密码长度必须为6-18位")
            return
        }
        mPresenter.resetPwd(phone_num.text.toString().trim(), pwd.text.toString().trim(), very_code.text.toString().trim())
    }

    private fun getVerifyCode() {

        if (!RegexUtil.IsTelephone(phone_num.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "该手机号码不合规，请重新输入")
            return
        }

        mTimount = TimeCount(get_code, 60000, 1000, "重新获取")
        mTimount?.start()
        mPresenter.getVerifyCode(phone_num.text.toString().trim(), "3")
    }

    override fun getVCodeComplete() {
        TCAgent.onEvent(mActivity, TalkingDataParams.GET_VERIFY_CODE, "3")
        ToastUtil.showToast(mActivity, "验证码发送成功")


    }

    override fun resetPwdComplete() {
        UserUtil.saveIsNew(mActivity, "1")
        finish()
       /* if (!TextUtils.isEmpty(mTitle)) {
            startActivity<MainActivity>()

//            EventBus.getDefault().post(LogOutEvent())
//            EventBus.getDefault().post(FinishSetingActivityEvent())

        }*/
    }

    override fun finshActivity() {
    }
}
