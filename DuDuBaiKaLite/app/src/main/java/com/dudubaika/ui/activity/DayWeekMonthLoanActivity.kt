package com.dudubaika.ui.activity

import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.MyLoanEvent
import com.dudubaika.model.bean.MineLoanBean
import com.dudubaika.presenter.contract.MineLoanContract
import com.dudubaika.presenter.impl.MineLoanPresenter
import com.dudubaika.ui.adapter.LoanAdapter
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_day_week_month_loan.*
import org.greenrobot.eventbus.Subscribe

/**
 * 近几日待还界面
 */
class DayWeekMonthLoanActivity : BaseActivity<MineLoanPresenter>() ,MineLoanContract.View{

    var mAdapter:LoanAdapter?=null
    var mTitle:String?=null
    var mTag:String?=null

    companion object {
        var TITLE="title"
        var DATE_TAG="date_tag"
    }

    override fun getLayout(): Int=R.layout.activity_day_week_month_loan

    override fun initInject() {
        activityComponent.inject(this)

    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() = Unit

    override fun hideProgress() = Unit

    override fun showError(url: String, msg: String) {
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_last_money)
         mTitle =  intent.extras.getString(TITLE)
        defaultTitle=mTitle!!
        tb_title.text  = mTitle
        iv_return.setOnClickListener {
            backActivity()
        }
    }

    override fun initData() {
        mTag =  intent.extras.getString(DATE_TAG)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getData(mTag!!)
    }

    override fun showData(data: MineLoanBean) {
        if (null==data){
            return
        }
        total_money.text = "应还款总额 "+data?.total_money+" 元"
        total_num.text = "("+ data?.total_count+"笔)"
        mAdapter = LoanAdapter(this, data.paynote_list, R.layout.item_daihuan)
        listview.adapter = mAdapter
    }

    override fun changeComplete() {
       mPresenter.getData(mTag!!)
    }

    @Subscribe
    public fun onMyLoanEvent(event : MyLoanEvent){
        val product_id = event.product_id
        if (null!=product_id) {
            mPresenter.changeNoteStatus(product_id)
        }
    }


}
