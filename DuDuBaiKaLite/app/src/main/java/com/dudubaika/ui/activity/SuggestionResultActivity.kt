package com.dudubaika.ui.activity

import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.presenter.contract.RepositoriesRusultContract
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_suggest_result.*
import kotlinx.android.synthetic.main.fragment_verify_me.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * Created by lenovo on 2018/3/29.
 */
class SuggestionResultActivity : SimpleActivity() {
    override fun getLayout(): Int = R.layout.activity_suggest_result

    override fun initView() {
        StatusBarUtil.setPaddingSmart(this, tb_info_suggest)
    }

    override fun initData() {
        iv_suggest_back.setOnClickListener(View.OnClickListener {
            backActivity()
        })
        btn_suggest_submit.setOnClickListener {
            getSuggestContent()
            progress_suggest.visibility = View.VISIBLE
            Thread.sleep(1000)
            doAsync {
                progress_suggest.visibility = View.INVISIBLE
            }
//            Thread.sleep(1000)
            longToast("提交成功")
        }
    }

    //获取建议内容和
    fun getSuggestContent(){
        var content = tv_suggest.text.toString().trim()
        var tel_num = tv_suggest_tel_num.text.toString().trim()
    }
}