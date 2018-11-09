package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.CardMoneyListBean
import com.dudubaika.presenter.contract.TalkHomeContract
import com.dudubaika.presenter.impl.MineTalkPresenter
import com.dudubaika.ui.adapter.TalkHomeListAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_mine_talk.*
import org.jetbrains.anko.startActivity

/**
 * 我的帖子
 */
class MineTalkActivity : BaseActivity<MineTalkPresenter>(), TalkHomeContract.View {

    private var type:String?=null
    private var mBean :CardMoneyListBean?=null
    private var mAdapter :TalkHomeListAdapter?=null
    private var mList :ArrayList<CardMoneyListBean.DiscussListBean>?=null

    companion object {
        var TYPE ="type"
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

    override fun getLayout(): Int = R.layout.activity_mine_talk

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,mine_talk_tb)
        defaultTitle="我的帖子"
        iv_return.setOnClickListener {
            backActivity()
        }
        fatie.setOnClickListener {
            startActivity<WriteMyTalkActivity>(WriteMyTalkActivity.TYPE to type!!)
        }
        p_name_key.setOnClickListener {

            startActivity<SearchTalkActivity>(SearchTalkActivity.TYPE to type!!)
        }
    }

    override fun initData() {
        type  = intent.getStringExtra(TYPE)
//        if (!TextUtils.isEmpty(type)){
//            if ("1".equals(type)){
//                tv_title.text = "贷款论坛"
//            }
//
//        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.loadListData(type!!)
    }

    override fun showListData(data: CardMoneyListBean) {
        if (null==data){
            return
        }
        mBean = data
        mList = mBean!!.discuss_list as ArrayList<CardMoneyListBean.DiscussListBean>?
        showView()

    }

    private fun showView() {

        if ("1".equals(mBean?.is_publish)){
            fatie.visibility = View.VISIBLE
        }else{
            fatie.visibility = View.GONE
        }
        mAdapter = TalkHomeListAdapter(mList,this)
        val manager = MyGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(true)
        recyclerView.layoutManager =   manager
        recyclerView.adapter = mAdapter

        mAdapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->

            val item =  adapter.getItem(position) as CardMoneyListBean.DiscussListBean
            //详情
            var isComment = true
            if ("2".equals(mBean?.is_publish)){
                isComment =false
            }
            startActivity<TalkDetailActivity>(TalkDetailActivity.ARTICEID to item?.discuss_id,TalkDetailActivity.ISPUBLISH to isComment )
        }

    }



}
