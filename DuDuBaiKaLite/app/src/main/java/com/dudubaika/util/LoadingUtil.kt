package com.dudubaika.util

import android.app.Activity
import android.app.Dialog
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import com.dudubaika.R
import android.view.Gravity


object LoadingUtil {

    var loadingDialog: Dialog? = null


    fun showLoadingDialog(fragment: Fragment, cancelable: Boolean = true) {
        showLoadingDialog(fragment.activity!!, cancelable)
    }

    fun showLoadingDialog(activity: Activity, cancelable: Boolean = true) {

        if (activity.isFinishing) {
            return
        }
        if (loadingDialog != null) {
            if (loadingDialog!!.isShowing) {
                return
            }
        }

        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.view_progress, null)

        if (activity.parent != null) {
            loadingDialog = Dialog(activity.parent, R.style.loading_dialog)
        } else {
            loadingDialog = Dialog(activity, R.style.loading_dialog)
        }

        // 不可以用“返回键”取消
        loadingDialog?.setCancelable(cancelable)
        loadingDialog?.setContentView(view)
        if (activity.isFinishing) {
            return
        }

        val dialogWindow = loadingDialog?.getWindow()
        val m = activity.windowManager
        val d = m.defaultDisplay // 获取屏幕宽、高用
        val p = dialogWindow?.attributes; // 获取对话框当前的参数值
        p?.height = (d.getHeight() * 0.3).toInt() // 高度设置为屏幕的0.6
        p?.width = (d.getWidth() * 0.6).toInt() // 宽度设置为屏幕的0.65
        dialogWindow?.attributes = p;

        loadingDialog?.show()

    }

    fun hideLoadingDialog() {
        if (null != loadingDialog) {
            loadingDialog?.cancel()
//            loadingDialog?.dismiss()
        }
        loadingDialog = null
    }

    fun hideLoadingDialog(activity: Activity) {
        if (activity.isFinishing) {
            loadingDialog = null
        } else {
            hideLoadingDialog()
        }

    }

    fun hideLoadingDialog(fragment: Fragment) {
        if (null !=fragment.activity &&fragment.activity!!.isFinishing) {
            loadingDialog = null
        } else {
            hideLoadingDialog()
        }
    }
}