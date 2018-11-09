package com.dudubaika.ui.activity

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.LoginSuccesfulEvent
import com.dudubaika.model.bean.LoginBean
import com.dudubaika.presenter.contract.ReviewLoginContract
import com.dudubaika.presenter.impl.ReviewLoginPresenter
import com.dudubaika.util.*
import kotlinx.android.synthetic.main.activity_review_login.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import java.util.*

class ReviewLoginActivity : BaseActivity<ReviewLoginPresenter>(), ReviewLoginContract.View {

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    var mLastHeightDifferece: Int = 0;
    var ssList: MutableList<CharacterStyle>? = null
    var webTitle: String? = null
    var mLoginBean: LoginBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        et_mobile_num.finishTimer()
    }

    override fun getLayout(): Int = R.layout.activity_review_login

    override fun initView() {
        StatusBarUtil.setPaddingSmart(this, tb_lgoin)

        tv_home_title.text = "欢迎登录${getString(R.string.app_name)}"
        tv_home_title.getPaint().setFakeBoldText(true)
        val d = getDeviceHeight(mActivity) * 0.33
        val lLayoutlayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, d.toInt())
        rl_logo.setLayoutParams(lLayoutlayoutParams)

        //协议
        initGotoWebData()

        tv_login.setOnClickListener({
            val userName = et_mobile_num.text.toString().trim()
            val verryCode = et_phone_number2.text.toString().trim()
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToast(mActivity, "登录手机号码不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(verryCode)) {
                ToastUtil.showToast(mActivity, "验证码不能为空")
                return@setOnClickListener
            }
            mPresenter.login(userName, verryCode)
        })
        et_mobile_num.setListener { getVeryCode() }
        iv_login_return.setOnClickListener({
            backActivity()
        })
        /*---------------------start适配软键盘弹起 引发的布局变化  只是一个合适的适配方案 不是完美适配的方案------------------------------------*/
        lgogin.getViewTreeObserver().addOnGlobalLayoutListener(
                {
                    val r = Rect()
                    lgogin.getWindowVisibleDisplayFrame(r)
                    val screenHeight = lgogin.getRootView().getHeight()
                    val statusBarHeight = StatusBarUtil.getStatusBarHeight()
                    val navigationBarHeight = StatusBarUtil.getNavigationBarHeight()
                    val heightDifference = screenHeight - (r.bottom - r.top) - statusBarHeight - navigationBarHeight
                    if (heightDifference > screenHeight / 4 && heightDifference != mLastHeightDifferece) {
                        mLastHeightDifferece = heightDifference
                        rl_activity.animate().translationY(-resources.displayMetrics.density * 60).setDuration(200).start()
                    } else if (heightDifference <= 0 && heightDifference != mLastHeightDifferece) {
                        mLastHeightDifferece = heightDifference
                        rl_activity.animate().translationY(0F).setDuration(200).start()
                    }
                }
        )
        /*---------------------end适配软键盘弹起 引发的布局变化  只是一个合适的适配方案 不是完美适配的方案--------------------------------------*/
    }

    private fun getVeryCode(): Boolean {
        val userName = et_mobile_num.text.toString().trim()


        if (TextUtils.isEmpty(userName) || !RegexUtil.IsTelephone(userName)) {
            ToastUtil.showToast(mActivity, "手机号格式不正确")
            return false
        }
        //获取验证码
        et_mobile_num.startTimer()
        mPresenter.getVeryCode(userName, "2")

        return true
    }

    override fun initData() {

    }

    override fun loginCompelete(data: LoginBean) {

        if (data == null) {
            return
        }


        UserUtil.saveUserId(mActivity, data.customer_id)
        UserUtil.saveToken(mActivity, data.token)
        UserUtil.saveMobile(mActivity, et_mobile_num.text.toString().trim())
        EventBus.getDefault().post(LoginSuccesfulEvent())
        gotoActivity(mActivity, MainActivity::class.java, null)

    }

    override fun finishActivity() {
        finish()
    }

    override fun getVeryCodeResult(data: Any) {
        ToastUtil.showToast(mActivity, "验证码发送成功")
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
        val text = "登录即表示您同意《用户服务协议》"
        SpannableUtils.setWebSpannableString(service, text, "《", "》", ssList, resources.getColor(R.color.black))
    }


    private val webSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            webTitle = "用户服务协议"
            val userServiceUrl = UserUtil.getUserServiceUrl(App.instance)
            if (TextUtils.isEmpty(userServiceUrl)) {
                ToastUtil.showToast(mActivity, "协议错误")
                return
            }
            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to userServiceUrl)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }


    /**
     * 获取屏幕的高
     */
    fun getDeviceHeight(context: Activity): Int {
        val display = context.windowManager.defaultDisplay
        val p = Point()
        display.getSize(p)
        return p.y
    }

}