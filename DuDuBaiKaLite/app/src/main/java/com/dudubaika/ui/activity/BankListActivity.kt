package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.CreditCardListBean
import com.dudubaika.presenter.contract.CreditCardListContract
import com.dudubaika.presenter.impl.CreditCardListPresenter
import com.dudubaika.ui.adapter.CardBankListAdapter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_bank_list.*
import org.jetbrains.anko.startActivity

/**
 * 银行卡列表
 */
class BankListActivity : BaseActivity<CreditCardListPresenter>(), CreditCardListContract.View {

    private var mBankId :String?= ""
    private var mAbilityId :String?= ""
    private var mType :String?= ""
    private var mBean :CreditCardListBean?= null
    private var mAdapter : CardBankListAdapter?= null
    private var mList :List<CreditCardListBean.CreditListBean> ?= null

    companion object {
        var ABILITY_ID= "ability_id"
        var TYPE= "type"
        var BANKID= "bank_id"
    }

    override fun getLayout(): Int = R.layout.activity_bank_list

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

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_bank_list)
        defaultTitle="信用卡卡列表"
        iv_return.setOnClickListener {
            backActivity()
        }
    }

    override fun initData() {
        mList = ArrayList()
        mAbilityId = intent.getStringExtra(ABILITY_ID)
        mType = intent.getStringExtra(TYPE)
        mBankId = intent.getStringExtra(BANKID)
        if (TextUtils.isEmpty(mAbilityId)){
            mAbilityId=""
        }
        if (TextUtils.isEmpty(mType)){
            mType = ""
        }
        if (TextUtils.isEmpty(mBankId)){
            mBankId = ""
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getCreditCardList(mBankId!!,mAbilityId!!,mType!!)
    }

    override fun showError(url: String, msg: String) {

    }

    override fun getCreditListComplete(data: CreditCardListBean) {

        if (null==data){
            return
        }
        mBean = data
        showData()
    }

    private fun showData() {
        if (null !=mBean?.credit_list && mBean!!.credit_list.size>0){
            mList  =mBean?.credit_list
            mAdapter  =CardBankListAdapter(mList,baseContext)
            bank_list_rl.layoutManager =   StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            bank_list_rl.adapter = mAdapter

           mAdapter?.setOnItemClickListener { adapter, view, position ->

               val item = adapter.getItem(position) as CreditCardListBean.CreditListBean
               if (!TextUtils.isEmpty(item.credit_id)){
                   startActivity<CreditCardDetailActivity>(CreditCardDetailActivity.CARDID to item.credit_id)
               }
           }
        }
    }
}
