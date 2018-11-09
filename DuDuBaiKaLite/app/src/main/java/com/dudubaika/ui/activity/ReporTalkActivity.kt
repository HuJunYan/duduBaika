package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.ReportBean
import com.dudubaika.presenter.contract.ReportContract
import com.dudubaika.presenter.impl.ReportPresnter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.*
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_repor_talk.*

/**
 * 举报页面
 */
class ReporTalkActivity : BaseActivity<ReportPresnter>(), ReportContract.View {

    private var mBea:ReportBean?=null
    private var disscusId:String?=null
    private var mList:ArrayList<String>?=null


    companion object {
        var DISSCUSID:String="disscusId"
        var TITLE:String="TITLE"
        var PHONE:String="Phone"
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

    override fun getLayout(): Int =R.layout.activity_repor_talk

    override fun initView() {

        StatusBarUtil.setPaddingSmart(mActivity,tb_title)
        defaultTitle = "举报帖子"
        val title = intent.getStringExtra(TITLE)
        val phone = intent.getStringExtra(PHONE)
        val phoneNum = Utils.encryptPhoneNum(phone)
        reported_title.text = title
        reported_person.text = "举报"+phoneNum+"的文章"

        iv_return.setOnClickListener {
            backActivity()
        }
        tv_jubao.setOnClickListener {
            if (mList?.size==0){
                ToastUtil.showToast(mActivity,"请选择举报信息")
                return@setOnClickListener
            }

            var ids = ""
            for(i in mList!!.indices){
                ids += if (i==mList!!.size || i==0){
                    mList!![i]
                }else{
                    mList!![i]+","
                }

            }

            mPresenter.reportTalk(disscusId!!,ids)
        }
    }

    override fun initData() {
        mList = ArrayList()
        disscusId  =intent.getStringExtra(DISSCUSID)

        if (!TextUtils.isEmpty(disscusId)) {
            mPresenter.getReportInfo(disscusId!!)
        }
    }

    override fun getReportInfoComplete(data: ReportBean) {

        if (null==data){
            return
        }
        mBea = data
        showView()
    }

    private fun showView() {

        val manager = MyGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        report_info.layoutManager =manager
        report_info.adapter  =object : BaseQuickAdapter<ReportBean.ReportListBean,BaseViewHolder>(R.layout.item_report_info,mBea?.report_list){
            override fun convert(helper: BaseViewHolder?, item: ReportBean.ReportListBean?) {
                 helper?.setText(R.id.cb, item?.report_name)
                val cb  =helper?.getView<CheckBox>(R.id.cb)
                cb?.setOnClickListener {
                    if(cb.isChecked){
                        if (!mList!!.contains(item?.report_id)){
                            mList?.add(item!!.report_id)
                        }
                    }else{
                        //未选中
                        if(mList!!.contains(item?.report_id)){
                            mList!!.remove(item?.report_id)
                        }
                    }
                }
            }
        }
    }

    override fun reportComplete() {

        ToastUtil.showToast(mActivity,"我们已收到您的举报，审核后会做相关处理，请勿重复举报!")
        val kv =  hashMapOf<String,String>()
        kv.put("discussId", disscusId!!)
        TCAgent.onEvent(mActivity, TalkingDataParams.REPORT_DISCUSS, "", kv)
        backActivity()
    }


}
