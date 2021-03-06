package com.dudubaika.ui.fragment

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.HomeFragmentIsDown
import com.dudubaika.event.HomeReturnTop
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.HomeListIsDown
import com.dudubaika.model.bean.HomeRefreshEvent
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.ProductContract
import com.dudubaika.presenter.impl.ProductPresenter
import com.dudubaika.ui.activity.ProductInfoActivity
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.ui.adapter.HomeFtagmentListAdapter
import com.dudubaika.ui.adapter.ProductAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.RecycleUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.home_fragment1.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.support.v4.startActivity

@SuppressLint("ValidFragment")
/**
 * 第5个fragment
 */
class FiveFragment @SuppressLint("ValidFragment")
constructor(var product_type:String ): BaseFragment<ProductPresenter>(), ProductContract.View{

    private var mBean: ProductInfoListBean? =null
    private var totalList:ArrayList<ProductInfoListBean.ProductListBean> ?=null
    private var mCashListData:ArrayList<ProductInfoListBean.ProductListBean> ?=null
    private var index:Int=1
    private var layoutManager:VirtualLayoutManager?=null
    private var hotProductAdapter:BaseDelegateAdapter?=null
    private var mFindListAdapter:HomeFtagmentListAdapter?=null
    constructor() : this("")

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
        refresh.visibility = View.GONE
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE
    }
    override fun hideProgress() {
        refresh.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    override fun showError(url: String, msg: String) {
        if (ApiSettings.GET_BANNER_LIST == url || ApiSettings.GET_PRODUCT_LIST == url) {
            refresh.visibility = View.GONE
            progress.visibility = View.VISIBLE
            progress.ll_loading.visibility = View.GONE
            progress.ll_error.visibility = View.VISIBLE
            progress.ll_error.setOnClickListener {
                mPresenter.getProductInfo(product_type,index.toString(),"50")
            }
        }
    }

    private var hot_list:ArrayList<String>?=null
    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()
    override fun getLayout(): Int = R.layout.home_fragment1

    override fun initView() {
        defaultTitle="首页tab第5个界面"
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (recyclerView!!.canScrollVertically(-1)){
                    // -1 表示 页面内容向下滑动， 1 表示向上。
                    LogUtil.i("recyclerView","可以继续向下滑动 ")
                    EventBus.getDefault().postSticky(HomeListIsDown(true,"5"))
                    EventBus.getDefault().postSticky(HomeFragmentIsDown(true))
                }else{
                    EventBus.getDefault().postSticky(HomeListIsDown(false,"5"))
                    EventBus.getDefault().postSticky(HomeFragmentIsDown(false))
                }

                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        refresh.isEnableLoadMore = true
        refresh.isEnableRefresh = false

        refresh.setOnLoadMoreListener {
            mPresenter.getProductInfo(product_type,index.toString(),"50")
        }

    }

    override fun initData() {
        index = 1
        totalList = ArrayList()
        mCashListData = ArrayList()
        mPresenter.getProductInfo(product_type,"1","50")

    }



    override fun getPrductInfo(data: ProductInfoListBean?) {

        if (null==data){
            return
        }
        mBean = data
        mCashListData = mBean!!.product_list
        if (index==1){
            totalList?.clear()
            totalList=mCashListData
            showListData()
        }else{
            totalList?.addAll(mCashListData!!)
        }
        mFindListAdapter?.notifyDataSetChanged()
        if (mBean!!.product_list.size<50){
            refresh.finishLoadMoreWithNoMoreData()
        }

        refresh.finishLoadMore()

    }

    private fun showListData() {
        mFindListAdapter = HomeFtagmentListAdapter(totalList!!,mContext!!,product_type)
        val manager = MyGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(true)
        recyclerView.layoutManager =   manager
        recyclerView.adapter = mFindListAdapter
    }

    @Subscribe
    public fun refreshDaata(event: HomeRefreshEvent){

        if (!TextUtils.isEmpty(event.type)){
            if (product_type==event.type){
                index=1
                mPresenter.getProductInfo(event.type,index.toString(),"50")
            }
        }
    }

    @Subscribe
    public fun OnHomeReturnTop(event: HomeReturnTop){
        if ("5".equals(event.type)){
            RecycleUtils.smoothMoveToPosition(recyclerView,0)
        }

    }

}