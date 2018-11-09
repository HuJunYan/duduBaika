package com.dudubaika.ui.activity

import android.text.TextUtils
import android.view.View

import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.JpushDetailBean
import com.dudubaika.presenter.contract.JpushDetailContract
import com.dudubaika.presenter.impl.JpushDetailPresenter
import com.dudubaika.util.StatusBarUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_jpush_detail.*

/**
 * 消息详情界面
 */
class JpushDetailActivity : BaseActivity<JpushDetailPresenter>(), JpushDetailContract.View {

    private var msgId :String?=null
    companion object {
        var MSG_ID="msg_id"
    }

    override fun initInject() {
        activityComponent.inject(this)
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

    override fun getLayout(): Int =R.layout.activity_jpush_detail

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_title)
        defaultTitle="消息详情列表"
        iv_return.setOnClickListener {
            backActivity()
        }
    }

    override fun initData() {
        msgId =intent.getStringExtra(MSG_ID)
         if (!TextUtils.isEmpty(msgId)){
             mPresenter.getMsgDetail(msgId!!)
         }
        val kv =  hashMapOf<String,String>()
        kv.put("newsId", msgId!!)
        TCAgent.onEvent(mActivity, TalkingDataParams.NEWS_DETAIL, "", kv)
    }


    override fun getMsgDetailData(data: JpushDetailBean) {

        if (null==data){
            return
        }
        tv_content_title.text = data?.msg_title
        tv_message_time.text = data?.msg_date
        tv_message_time.visibility = View.VISIBLE

        webview.loadData(data?.msg_content, "text/html", "UTF-8")

//        webview.loadUrl(data?.msg_content)
    }


}
