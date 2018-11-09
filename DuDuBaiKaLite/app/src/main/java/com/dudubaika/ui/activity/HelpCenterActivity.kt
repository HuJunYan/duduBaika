package com.dudubaika.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.HelpCenterBean
import com.dudubaika.presenter.contract.HelpCenterContract
import com.dudubaika.presenter.impl.HelpcenterPresenter
import com.dudubaika.ui.adapter.UseRulesHelperCenterAdapter
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_help_center.*

/**
 * 帮助中心界面
 */
class HelpCenterActivity : BaseActivity<HelpcenterPresenter>(), HelpCenterContract.View {
    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showError(url: String, msg: String) {
    }

    var mList: ArrayList<HelpCenterBean.HelpListBean>? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mAdapter: UseRulesHelperCenterAdapter? = null
    var mData: HelpCenterBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun getLayout(): Int = R.layout.activity_help_center
    override fun showProgress() {
    }

    override fun hideProgress() {
    }



    override fun initView() {
        StatusBarUtil.setPaddingSmart(this, tb_use_rule)
        defaultTitle="帮助中心"
        tv_use_rule_back.setOnClickListener { backActivity() }
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        if (mList == null) {
            mList = ArrayList()
           
        }
        //添加一个头部的背景item
        if (mAdapter == null) {
            mAdapter = UseRulesHelperCenterAdapter(mActivity, mList)
        }
        rv_use_rule.layoutManager = mLinearLayoutManager
        rv_use_rule.adapter = mAdapter
    }

    override fun initData() {
        mPresenter.getData("2")

    }

    override fun processData(data: HelpCenterBean?) {
        if (data == null) {
            return
        }
        mData = data
        val qaList = data.help_list
        if (qaList != null) {
            mList?.addAll(qaList)
            mAdapter?.notifyDataSetChanged()
        }

    }


}