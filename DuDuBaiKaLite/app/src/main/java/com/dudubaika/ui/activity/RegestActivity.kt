package com.dudubaika.ui.activity

import android.app.Activity
import android.text.InputType
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.LoginSuccesfulEvent
import com.dudubaika.model.bean.RegistBean
import com.dudubaika.presenter.contract.RegistContract
import com.dudubaika.presenter.impl.RegisterPresenter
import com.dudubaika.util.*
import com.tendcloud.tenddata.TCAgent
import com.tendcloud.tenddata.TDAccount
import kotlinx.android.synthetic.main.activity_regest.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * 注册界面
 */
class RegestActivity : BaseActivity<RegisterPresenter>(), RegistContract.View {

    private var mUrl: String? = null
    var webTitle: String? = null
    var mTimeCount: TimeCount? = null
    var ssList: MutableList<CharacterStyle>? = null

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        if (mTimeCount != null) {
            mTimeCount!!.finish()
        }
    }

    override fun getLayout(): Int = R.layout.activity_regest

    override fun initView() {
        StatusBarUtil.setPaddingSmart(this, tb_regist)
        defaultTitle="注册"
        initGotoWebData()
        iv_regist_return.setOnClickListener({
            backActivity()
        })
        get_code.setOnClickListener { getVeryCode() }
        regist.setOnClickListener({ regist() })
        tv_login.setOnClickListener { backActivity() }

        pwd_islook.setOnClickListener {
            if (pwd_islook.isChecked){
                pwd.inputType  =InputType.TYPE_CLASS_TEXT
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
    }

    fun regist() {

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
            ToastUtil.showToast(mActivity, "两次输入的密码不相同请重新输入")
            return
        }
        /* if (RegexUtil.IsPassword(pwd.text.toString().trim())){
             ToastUtil.showToast(mActivity,"密码不能含特殊字符，必须为数字和字母")
             return
         }*/
        if (!RegexUtil.IsPasswLength(pwd.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "请使用非汉字组合,且密码长度必须为6-18位")
            return
        }
        mPresenter.regist(phone_num.text.toString().trim(), very_code.text.toString().trim(), pwd.text.toString().trim())


    }

    private fun getVeryCode(): Boolean {
        val userName = phone_num.text.toString().trim()


        if (TextUtils.isEmpty(userName) || !RegexUtil.IsTelephone(userName)) {
            ToastUtil.showToast(mActivity, "该手机号码不合规，请重新输入")
            return false
        }
        //获取验证码
        mTimeCount = TimeCount(get_code, 60000, 1000, "重新获取")
        mTimeCount!!.start()
        mPresenter.getVeryCode(userName, "1")

        return true
    }

    /**
     * 设置spannable点击规则
     */
    private fun initGotoWebData() {

        if (ssList == null) {
            ssList = ArrayList<CharacterStyle>()
        }
        ssList!!.clear()
        ssList!!.add(webSpan)
        val text = "注册即表示您同意《用户注册协议》"
        SpannableUtils.setWebSpannableString(info, text, "《", "》", ssList, resources.getColor(R.color.very_code_bg_line))
    }


    override fun getVeryCodeComplete() {
        TCAgent.onEvent(mActivity, TalkingDataParams.GET_VERIFY_CODE, "1")
        ToastUtil.showToast(mActivity, "验证码发送成功")
        showSoftInputFromWindow(mActivity,very_code)

    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }


    private val webSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            webTitle = "用户注册协议"
            mUrl = UserUtil.getUserServiceUrl(mActivity)
            if (TextUtils.isEmpty(mUrl)) {
                ToastUtil.showToast(mActivity, "协议错误")
                return
            }
            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to mUrl!!, WebActivity.WEB_URL_TITLE to webTitle!!)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    override fun registComplete(data: RegistBean) {


        if (null==data){
            ToastUtil.showToast(mActivity,"服务器繁忙，请稍后再试")
            return
        }
        TCAgent. onRegister(data.customer_id, TDAccount.AccountType.REGISTERED, phone_num.text.toString().trim())
        ToastUtil.showToast(mActivity, "注册成功")

        //保存登录后的用户值
        UserUtil.saveToken(mActivity, data.token)
        UserUtil.saveUserId(mActivity, data.customer_id)
        UserUtil.saveIsNew(mActivity, "1")
        UserUtil.saveMobile(mActivity, phone_num.text.toString().trim())
        EventBus.getDefault().post(LoginSuccesfulEvent())
        startActivity<MainActivity>()
        finish()

    }

    override fun finishActivity() {

    }


    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(activity: Activity, editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

}
