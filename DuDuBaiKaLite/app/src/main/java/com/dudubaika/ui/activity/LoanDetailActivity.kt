package com.dudubaika.ui.activity

import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.SimpleActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.LoanDetailBean
import com.dudubaika.presenter.contract.LoadDetailContract
import com.dudubaika.presenter.impl.LoadDetailPresenter
import com.dudubaika.util.StatusBarUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_loan_detail.*
import org.jetbrains.anko.startActivity

/**
 * 账单详情界面
 */
class LoanDetailActivity : BaseActivity<LoadDetailPresenter>(),LoadDetailContract.View {


    private var product_id=""
    private var notice_id=""


    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() = Unit

    override fun hideProgress()= Unit

    override fun showError(url: String, msg: String) {
    }

    companion object {
      var PRODUCT_ID= "product_id"
      var NOTICE_ID= "notice_id"
    }

    override fun getLayout(): Int = R.layout.activity_loan_detail


    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_title)
        defaultTitle="账单详情"
        iv_return.setOnClickListener {
            backActivity()
        }
        tv_change.setOnClickListener {
            startActivity<AddLoanInfoActivity>(AddLoanInfoActivity.PRODUCT_ID to notice_id,AddLoanInfoActivity.NOTE_ID to product_id)
        }
        val kv =  hashMapOf<String,String>()
        kv.put("noteId", notice_id!!)
        TCAgent.onEvent(mActivity, TalkingDataParams.NOTE_DETIAL, "", kv)
    }

    override fun initData() {
        notice_id =  intent.getStringExtra(PRODUCT_ID)
        if (TextUtils.isEmpty(notice_id)){
            notice_id=""
        }

    }

    override fun onResume() {
        super.onResume()
        mPresenter.getLoanDetailData(notice_id)
    }

    override fun showData(data: LoanDetailBean) {

        if (null==data){
            return
        }
//        notice_id = data.n
        p_name.text= data.product_name
        p_money.text= data.loan_money+"元"
        p_start_time.text= data.loan_date
        p_term.text= data.loan_term+"天"
        p_repay_date.text= data.repay_date
    }

    override fun finishActivity() {

    }

    override fun addLoanComplete() {
        //这里用不到
    }


}
