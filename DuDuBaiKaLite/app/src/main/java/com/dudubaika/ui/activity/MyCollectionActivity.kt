package com.dudubaika.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.dudubaika.R
import com.dudubaika.R.id.recycler
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.RefreshCollectionEvent
import com.dudubaika.model.bean.VerifyHomeDataBean
import com.dudubaika.presenter.contract.MyCollectionContract
import com.dudubaika.presenter.impl.MyCollectionPresenter
import com.dudubaika.ui.adapter.HomeVerifyAdapter
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_my_collection.*
import org.greenrobot.eventbus.Subscribe

/**
 * 我的收藏
 */

class MyCollectionActivity : BaseActivity<MyCollectionPresenter>(), MyCollectionContract.View {

    private var mLinearLayoutManager: LinearLayoutManager? = null
//    private var mList: MutableList<VerifyHomeDataBean.VerifyItemBean>? = null
    private var mAdapter: HomeVerifyAdapter? = null


    override fun getLayout(): Int = R.layout.activity_my_collection

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, my_collection_toobar)
        initRecyclerView()
        my_collection_return.setOnClickListener { backActivity() }
    }

    override fun initData() {
        mPresenter.loadData()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun loadDataComplete(data: VerifyHomeDataBean?) {
//        if (data == null || data.article_list == null || data.article_list.size == 0) {
            recycler.visibility = View.GONE
            rl_no_collection.visibility = View.VISIBLE
            return
        }
//        recycler.visibility = View.VISIBLE
//        rl_no_collection.visibility = View.GONE
//        mList?.clear()
//        mList?.addAll(data.article_list)
//        mAdapter?.notifyDataSetChanged()


    }

    fun initRecyclerView() {
//        if (mLinearLayoutManager == null) {
//            mLinearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
//        }
//        if (mList == null) {
//            mList = ArrayList()
//        }
//
//        if (mAdapter == null) {
//            mAdapter = HomeVerifyAdapter(mActivity, mList)
        }
//        recycler.layoutManager = mLinearLayoutManager
//        recycler.adapter = mAdapter
//    }

    //收藏状态发生变化 刷新数据
    @Subscribe
    fun onRefreshCollectionEvent(event: RefreshCollectionEvent) {
//        mPresenter.loadData()
    }
//}
