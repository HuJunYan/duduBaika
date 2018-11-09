package com.dudubaika.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.log.LogUtil
import com.dudubaika.log.LogUtilHelper
import com.dudubaika.model.bean.VerifyProductDetailBean
import com.dudubaika.presenter.contract.TransficCardConstract
import com.dudubaika.presenter.impl.TransficCardPresenter
import com.dudubaika.ui.adapter.ProductDetailAdapter
import kotlinx.android.synthetic.main.activity_product_detail.*
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.dudubaika.util.StatusBarUtil


/**
 * Created by lenovo on 2018/3/28.
 */
class ProductDetailActivity : BaseActivity<TransficCardPresenter>(), TransficCardConstract.View {
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mList: ArrayList<VerifyProductDetailBean.ProductDetailBean>? = null
    private var mAdapter: ProductDetailAdapter? = null
    override fun showProgress() {

    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }


    override fun getLayout(): Int = R.layout.activity_product_detail

    override fun initView() {

        srl_home_detail.isEnableLoadMore = false
        srl_home_detail.isEnableRefresh = true
        iv_show_data_return.setOnClickListener(View.OnClickListener { backActivity() })


    }

    override fun initData() {
        StatusBarUtil.setPaddingSmart(this, tb_home_detail)
        var mIntent = intent
        val product_id = mIntent.getStringExtra("product_id")
        mPresenter.setCard_id(product_id)

        srl_home_detail.setOnRefreshListener {
            mList?.clear()
            mPresenter.setCard_id(product_id)

        }
    }

    override fun show_product_detail(cardBean: VerifyProductDetailBean) {
        var list = cardBean.product_detail_list
        if (mList == null) {
            mList = ArrayList()
        }
        mList?.addAll(list)
        initRecyclerView()
        srl_home_detail.finishRefresh()
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    fun initRecyclerView() {
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        if (mAdapter == null) {
            mAdapter = ProductDetailAdapter(this, mList)
        }
        recyclerView_detail.layoutManager = mLinearLayoutManager
        //设置适配器
        recyclerView_detail.adapter = mAdapter

    }


}

