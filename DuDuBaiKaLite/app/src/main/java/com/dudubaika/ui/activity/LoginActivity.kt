package com.dudubaika.ui.activity

import android.content.Intent
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.LoginSuccesfulEvent
import com.dudubaika.event.SetPwdEvent
import com.dudubaika.model.bean.LoginBean
import com.dudubaika.presenter.contract.LoginContract
import com.dudubaika.presenter.impl.LoginPresenter
import com.dudubaika.util.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_mylogin_edit_text.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import java.util.*
import android.view.WindowManager
import android.widget.EditText
import android.app.Activity
import android.renderscript.ScriptGroup
import android.text.*
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.tendcloud.tenddata.TCAgent
import com.tendcloud.tenddata.TDAccount


/**
 * 登录页面
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    private var login_type: String = "2"
    private var mBean: LoginBean? = null
    var ssList: MutableList<CharacterStyle>? = null
    var webTitle: String? = null
    var mLoginBean: LoginBean? = null
    var mTimer:TimeCount?=null
    private var isTogoDetail: Boolean = false

    companion object {
        var ISTOGODETAIL = "isTogoDetail"
        private val INPUT_TEXT = 1
        private val INPUT_PASSWORD = 3
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
        LoadingUtil.hideLoadingDialog(mActivity)
    }

    override fun showError(url: String, msg: String) {
        et_mobile_num.finishTimer()
        LoadingUtil.hideLoadingDialog(mActivity)
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun getLayout(): Int = R.layout.activity_login


    override fun initView() {
        tv_home_title.text = "登录${getString(R.string.app_name)}"
        defaultTitle="登录"
        StatusBarUtil.setPaddingSmart(this, tb_lgoin)
        showCodeLogin()

        et_mobile_num.edit_content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        et_phone_number2.edit_content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(18))
        et_phone_pwd.edit_content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(18))


        et_mobile_num.isShowRightView(false)
        iv_login_return.setOnClickListener({
            backActivity()
        })
        tv_pwd_login.setOnClickListener({
            if (login_type == "1") {
                return@setOnClickListener
            }
            login_type = "1"

            et_mobile_num.isShowRightView(false)

            val str1 = Html.fromHtml("<font color=#ffffff>" + "密码登录" + "</font>")
            tv_pwd_login.text = str1

            val str2 = Html.fromHtml("<font color=#C8C8C8>" + "验证码登录" + "</font>")
            tv_verycode_login.text = str2

            pwd_line.visibility = View.INVISIBLE
            very_code_line.visibility = View.VISIBLE

            et_phone_pwd.visibility = View.VISIBLE
            rl_code_layout.visibility = View.INVISIBLE

            forget_pwd.visibility = View.VISIBLE
            pwd_islook.visibility  =View.VISIBLE
        })
        tv_verycode_login.setOnClickListener({
            if (login_type == "2") {
                return@setOnClickListener
            }
            showCodeLogin()
        })

        pwd_islook.setOnClickListener {

            if (pwd_islook.isChecked){
                //开启密码可见
                et_phone_pwd.viewInputType = INPUT_TEXT
            }else{
                //密码不可见
                et_phone_pwd.viewInputType = INPUT_PASSWORD
            }

        }

    }

    private fun showCodeLogin() {
        login_type = "2"
        pwd_islook.visibility  =View.GONE
        et_mobile_num.isShowRightView(false)


        val str1 = Html.fromHtml("<font color=#C8C8C8>" + "密码登录" + "</font>")
        tv_pwd_login.text = str1

        val str2 = Html.fromHtml("<font color=#FFFFFF>" + "验证码登录" + "</font>")
        tv_verycode_login.text = str2

        pwd_line.visibility = View.VISIBLE
        very_code_line.visibility = View.INVISIBLE

        et_phone_pwd.visibility = View.INVISIBLE
        rl_code_layout.visibility = View.VISIBLE

        forget_pwd.visibility = View.GONE
    }

    override fun initData() {
        initGotoWebData()
        isTogoDetail =intent.getBooleanExtra(ISTOGODETAIL,false)

        forget_pwd.setOnClickListener { startActivity(Intent(mActivity, ForgetPwdActivity::class.java)) }
        tv_regist.setOnClickListener { startActivity(Intent(mActivity, RegestActivity::class.java)) }

        //获取验证码
//        et_phone_number2.setListener {  }

        tv_get_code.setOnClickListener { getVeryCode() }

        tv_login.setOnClickListener {

            if (TextUtils.isEmpty(et_mobile_num.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "手机号不能为空")
                return@setOnClickListener
            }

            if (!RegexUtil.IsTelephone(et_mobile_num.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "该手机号码不合规，请重新输入")
                return@setOnClickListener
            }

            if (login_type == "1" && TextUtils.isEmpty(et_phone_pwd.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "密码不能为空")
                return@setOnClickListener
            }
            if (login_type == "2" && TextUtils.isEmpty(et_phone_number2.text.toString().trim())) {
                ToastUtil.showToast(mActivity, "验证码不能为空")
                return@setOnClickListener
            }
            LoadingUtil.showLoadingDialog(mActivity, false)
            mPresenter.login(et_mobile_num.text.toString().trim(), login_type, et_phone_pwd.text.toString().trim(), et_phone_number2.text.toString().trim())
        }
    }

    override fun getVCodeComplete() {
        mTimer?.start()
        GlobalParams
        TCAgent.onEvent(mActivity, TalkingDataParams.GET_VERIFY_CODE, "2")
        ToastUtil.showToast(mActivity, "验证码发送成功")
        showSoftInputFromWindow(mActivity,et_phone_number2.edit_content)

    }

    override fun loginComplete(data: LoginBean?) {
        LoadingUtil.hideLoadingDialog(mActivity)
        if (data == null) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        mBean = data


        /*if (data?.is_new == "2") {
            //设置密码
            UserUtil.saveMobile(mActivity, et_mobile_num.text.toString().trim())
            startActivity<SetPwdActivity>(SetPwdActivity.PHONENUM to et_mobile_num.text.toString().trim())

        } else {

        }*/

        //保存登录后的用户值
        UserUtil.saveToken(mActivity, data!!.token)
        UserUtil.saveUserId(mActivity, data!!.customer_id)
        UserUtil.saveIsNew(mActivity, data!!.is_new)
        UserUtil.saveMobile(mActivity, et_mobile_num.text.toString().trim())
        if ("1"==login_type){
            TCAgent.onLogin(data!!.customer_id, TDAccount.AccountType.TYPE1,et_mobile_num.text.toString().trim())
        }else{
            TCAgent.onLogin(data!!.customer_id, TDAccount.AccountType.TYPE2,et_mobile_num.text.toString().trim())
        }

        postLoginSuccessful()
        if (!isTogoDetail) {
            //从详情界面点进去不需要进到主界面,暂改为所有都到主界面

        }
        startActivity<MainActivity>()
        finish()


    }

    override fun finishActivity() {

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
        val text = "登录即表示您同意《用户注册协议》"
        SpannableUtils.setWebSpannableString(service, text, "《", "》", ssList, resources.getColor(R.color.red_home))
    }


    private val webSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            webTitle = "用户注册协议"
            val userServiceUrl = UserUtil.getUserServiceUrl(mActivity)
            if (TextUtils.isEmpty(userServiceUrl)) {
                ToastUtil.showToast(mActivity, "协议错误")
                return
            }
            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to userServiceUrl, WebActivity.WEB_URL_TITLE to webTitle!!)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    private fun getVeryCode() {
        val userName = et_mobile_num.text.toString().trim()

        if (TextUtils.isEmpty(userName) || !RegexUtil.IsTelephone(userName)) {
            ToastUtil.showToast(mActivity, "该手机号码不合规，请重新输入")
            return
        }
        //获取验证码
        mPresenter.getVerifyCode(userName, "2")
        mTimer = TimeCount(tv_get_code, 60000, 1000, "重新获取",true)


    }

    @Subscribe
    fun setPwdSuccesEvent(event: SetPwdEvent) {

        //保存登录后的用户值
        UserUtil.saveToken(mActivity, mBean!!.token)
        UserUtil.saveUserId(mActivity, mBean!!.customer_id)
        UserUtil.saveIsNew(mActivity, mBean!!.is_new)
        UserUtil.saveMobile(mActivity, et_mobile_num.text.toString().trim())
        postLoginSuccessful()
        finish()

    }

    private fun postLoginSuccessful() {
        EventBus.getDefault().post(LoginSuccesfulEvent())
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
