package com.dudubaika.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.presenter.contract.TagProductContract
import com.dudubaika.presenter.impl.TagProductPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.ui.adapter.HomeFtagmentListAdapter
import com.dudubaika.ui.adapter.ProductAdapter
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_tag_product.*

/**
 * 根据标签来筛选的产品
 */
class TagProductActivity : BaseActivity<TagProductPresenter>(), TagProductContract.View {

    private var mBean:ProductInfoListBean?=null
    private var tagAdapter: HomeFtagmentListAdapter?=null
    private var totalList: ArrayList<ProductInfoListBean.ProductListBean>?=null
    private var cashList: ArrayList<ProductInfoListBean.ProductListBean>?=null
    private var pageSize:String="10"
    private var index:Int=1
    private var mTagId :String?= null
    private var mTitle :String?= null

    companion object {
        var TAG_ID :String="tag_id"
        var TITLE :String="title"
    }


    override fun getLayout(): Int =R.layout.activity_tag_product

    override fun initView() {
         StatusBarUtil.setPaddingSmart(mActivity,tb_title)
        totalList = ArrayList()
        cashList = ArrayList()
        refresh.isEnableLoadMore =true
        refresh.isEnableRefresh = true
        iv_return.setOnClickListener {
            backActivity()
        }
    }

    override fun initData() {

        showData()
        mTagId= intent.getStringExtra(TAG_ID)
        mTitle= intent.getStringExtra(TITLE)
        tv_title.text = mTitle
        if (!TextUtils.isEmpty(mTagId)){
            mPresenter.getTagProductList(mTagId!!,index.toString(),pageSize)
        }

        refresh.setOnRefreshListener {
            index= 1
            mPresenter.getTagProductList(mTagId!!,index.toString(),pageSize)
        }
        refresh.setOnLoadMoreListener {
            index++
            mPresenter.getTagProductList(mTagId!!,index.toString(),pageSize)
        }
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

    //机构数据源
    override fun showTagProductListDat(data: ProductInfoListBean) {
        refresh.finishRefresh()
        if (null==data){
            return
        }
        mBean = data
        cashList?.clear()
        if (index==1) {
            cashList = data.product_list
            totalList?.addAll(cashList!!)
        }else{
            cashList = data.product_list
            totalList?.addAll(cashList!!)
        }
        if(cashList!!.size<pageSize.toInt()){
            refresh.finishLoadmoreWithNoMoreData()
        }

        tagAdapter?.notifyDataSetChanged()
        refresh.finishLoadMore()
    }

    private fun showData() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        tagAdapter = HomeFtagmentListAdapter(totalList!!, mActivity,GlobalParams.PRODUCT_TYPE_TAg)
        recyclerView.adapter = tagAdapter
    }

}
