package com.dudubaika.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.dudubaika.R
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.SimpleActivity
import com.dudubaika.event.PointEvent
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_wei_xin.*
import kotlinx.android.synthetic.main.dialog_islogin_out.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 微信公众号界面
 */
class WeiXinActivity : SimpleActivity() {

    private var mDialog:Dialog?=null
    override fun getLayout(): Int = R.layout.activity_wei_xin

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_weixin)
        defaultTitle="微信公众号"
        weixin_return.setOnClickListener({backActivity()})
        go_weixin.setOnClickListener({
            showIsloginOutDialog()
            EventBus.getDefault().post(PointEvent(GlobalParams.FALG_SIX,""))
        })
        copy.setOnClickListener({copyWeiXin()})
    }

    override fun initData() = Unit

    /**
     * 拷贝微信公众号到剪切版
     */
    private fun copyWeiXin() {

        val cmb = mActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.text = "嘟嘟白卡"
        ToastUtil.showToast(mActivity, "已复制微信公众号")
    }

    /**
     * 弹窗
     */
    private fun showIsloginOutDialog() {
        mDialog = Dialog(this,R.style.MyDialog)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_islogin_out, null)
        view.tv_dialog_assess_success_agreement.text = "确定打开微信吗？"
        view.dialog_confirm.setOnClickListener({

            gotoWeixin2()
            mDialog?.dismiss()
        })

        view.dialog_cancle.setOnClickListener({
            //取消
            mDialog?.dismiss()
        })
        mDialog?.setContentView(view)
        mDialog?.setCancelable(true)
        mDialog?.show()

    }

    private fun gotoWeixin2(){
    try {

        val intent = Intent()
        val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.component = cmp
        startActivity(intent)

    }catch (e:Exception){
        ToastUtil.showToast(mActivity,"请确保您已安装微信")
    }

    }

    private fun gotoWeixin() {
        /*try {
            if (QbSdk.checkApkExist(mActivity, "com.tencent.mm")) {
                val intent = Intent()
                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                intent.action = Intent.ACTION_MAIN
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.component = cmp
                startActivity(intent)

            } else {
                ToastUtil.showToast(mActivity,"本机未安装微信应用")
            }
        } catch (e: Exception) {
            ToastUtil.showToast(mActivity,"打开微信失败")
        } finally {

        }*/
    }

    /**
     * 是否安装微信
     */
    fun isWeixinAvilible(context: Activity): Boolean {
        val packageManager = context.packageManager
        // 获取packagemanager
        val pinfo = packageManager.getInstalledPackages(0)
        // 获取所有已安装程序的包信息

        if (pinfo != null) {
            for (item in pinfo) {
                val pn = item.packageName
                if ("com.tencent.mm".equals(pn)) {
                    return true
                }
            }
        }
        return false
    }

}
