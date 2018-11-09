package com.dudubaika.ui.activity

import android.text.TextUtils
import android.view.KeyEvent
import android.widget.Toast
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.AdvertisingBean
import com.dudubaika.model.bean.UpgradeBean
import com.dudubaika.presenter.contract.NavigationContract
import com.dudubaika.presenter.impl.NavigationPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import me.drakeet.materialdialog.MaterialDialog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread



class NavigationActivity : BaseActivity<NavigationPresenter>(), NavigationContract.View {

    private var mAdBean:AdvertisingBean?=null
    private var deviceId:String?=null

    override fun getLayout(): Int = R.layout.activity_navigation

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.immersive(this)
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        defaultTitle="启屏页"
    }

    override fun initData() {
        UserUtil.saveIsStartApp(mActivity,true)
        deviceId = UserUtil.getDeviceId(this)
        mPresenter.getAdverst()
         Thread(Runnable {
             mPresenter.startAppTime(deviceId!!)
         }).start()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            startActivity<MainActivity>()
        }
    }


    override fun checkUpdateResult(data: UpgradeBean) {
        doAsync {
            Thread.sleep(1000)
            uiThread { checkResult(data) }
        }
    }

    override fun getAdverstData(data: AdvertisingBean) {
        //获取广告信息
        if (null==data){
            ToastUtil.showToast(mActivity,"信息有误，请稍后再试")
            return
        }
        mAdBean = data
        mPresenter.checkUpdate()

    }


    private fun checkResult(upgradeBean: UpgradeBean) {
        UserUtil.saveAuthorUrl(App.instance, upgradeBean.user_service_url)
        UserUtil.saveUserServiceUrl(App.instance, upgradeBean.register_url)
        UserUtil.saveServiceOnlineUrl(App.instance, upgradeBean.service_online_url)
        App.instance.mIsVerify = upgradeBean.on_verify == "1"
          //广告图片为空时 走正常逻辑

            if ("1" == upgradeBean.is_ignore) {
                //当前是最新版本
                if (TextUtils.isEmpty(mAdBean?.img_url)) {
                    //广告为空
                    startActivity<MainActivity>()
                }else{
                    //跳转到广告页
                    startActivity<AdvertisingActivity>(AdvertisingActivity.IMG_URL_KEY to mAdBean!!.img_url
                            ,AdvertisingActivity.TIME_KEY to mAdBean!!.ad_time
                            ,AdvertisingActivity.JUMP_URL to mAdBean!!.jump_url)
                    LauncherActivity@ this.finish()
                }

            } else {

                if (!mActivity.isFinishing) {
                    val alert = MaterialDialog(LauncherActivity@ this)
                    alert.setTitle("版本更新")
                            .setMessage(upgradeBean.introduction)
                            .setPositiveButton("下载", {
                                val download_url = upgradeBean.download_url
//                        Utils.downloadAPP(LauncherActivity@ this, download_url)
                                showDownLoadDialog(download_url)
                                alert.dismiss()
                                LauncherActivity@ this.finish()
                            })
                            .setNegativeButton("忽略", {
                                if ("1" == upgradeBean.force_upgrade) { //强制升级
                                    LauncherActivity@ this.finish()
                                } else {
                                    startActivity<MainActivity>()
                                }
                                alert.dismiss()
                            })
                            .setCanceledOnTouchOutside(false)
                            .show()
                }
            }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
           //屏蔽返回键
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
