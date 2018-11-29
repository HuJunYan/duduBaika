package com.dudubaika.ui.fragment

import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseFragment
import com.dudubaika.event.LogOutEvent
import com.dudubaika.event.LoginSuccesfulEvent
import com.dudubaika.presenter.contract.MineverifyContract
import com.dudubaika.presenter.impl.MineverifyPresenter
import com.dudubaika.ui.activity.*
import com.dudubaika.util.*
import kotlinx.android.synthetic.main.fragment_verify_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.support.v4.startActivity


class VerifyMeFragment : BaseFragment<MineverifyPresenter>(), MineverifyContract.View {

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun processExitLoginResult() {

        clearAndRefreshView()
    }

    override fun getLayout(): Int = R.layout.fragment_verify_me

    override fun initView() {
        StatusBarUtil.setPaddingSmart(activity, verify_tb_me)
        tv_me_title.paint.isFakeBoldText = true
        rl_me_exit_login.setOnClickListener({
            //点击了   退出登录
//            val isLogin = UserUtil.isLogin(activity!!)
//            if (isLogin) {
//
//                mPresenter.exitLogin()
//                clearAndRefreshView()
//                ToastUtil.showToast(activity, "退出登录成功")
//                EventBus.getDefault().post(LogOutEvent())
//            } else {
//                startActivity<ReviewLoginActivity>()
//            }
//            startActivity<OpinionUpActivity>()


        })
        refreshUI()
        rl_me_avatar.setOnClickListener({
            if (UserUtil.isLogin(App.instance)) {
                return@setOnClickListener
            }
            startActivity<ReviewLoginActivity>()
        })
        my_collection.setOnClickListener({
            if (!UserUtil.isLogin(App.instance)) {
                startActivity<ReviewLoginActivity>()
            } else {
//                startActivity<MyCollectionActivity>()
            }
        })
        tv_me_setting.setOnClickListener {
            startActivity<SettingActivity>()
        }
    }

    override fun initData() {
        version.text = "当前版本：" + Utils.getVersion(App.instance)
    }


    //未登录UI
    private fun refreshUI() {
        if (UserUtil.isLogin(App.instance)) {

            //设置手机号
            val PhoneNum = UserUtil.getMobile(activity!!)
            val encryptPhoneNum = StringUtil.encryptPhoneNum(PhoneNum)
            tv_user_phone_number.text = encryptPhoneNum
        } else {
            //未登录
            tv_user_phone_number.text = "未登录"
            tv_user_member_date.text = ""
        }

    }

    fun clearAndRefreshView() {
        UserUtil.clearUser(App.instance)
        refreshUI()
    }

    @Subscribe
    fun loginSuccess(event: LoginSuccesfulEvent) {
        refreshUI()
    }
}