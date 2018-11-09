package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.CardMoneyListBean
import com.dudubaika.presenter.contract.TalkHomeContract
import com.dudubaika.presenter.impl.TalkHomePresenter
import com.dudubaika.ui.adapter.TalkHomeListAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.UserUtil
import kotlinx.android.synthetic.main.activity_card_talk.*
import org.jetbrains.anko.startActivity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.WriteCompleteEvent
import com.tendcloud.tenddata.TCAgent
import org.greenrobot.eventbus.Subscribe


/**
 * 信用卡、贷款论坛首页
 */
class TalkHomeActivity : BaseActivity<TalkHomePresenter>(), TalkHomeContract.View {

    private var type:String?=null
    private var mBean:CardMoneyListBean?=null
    private var mAdapter:TalkHomeListAdapter?=null
    private var mList:ArrayList<CardMoneyListBean.DiscussListBean>?=null


    companion object {
        var TYPE:String = "type"
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
        refresh.finishRefresh()

    }

    override fun showError(url: String, msg: String) {
        refresh.finishRefresh()

    }

    override fun getLayout(): Int = R.layout.activity_card_talk

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,home_card_tb)
        iv_return.setOnClickListener {
            backActivity()
        }
        mine_money_talk.setOnClickListener {
            if (UserUtil.isLogin(mActivity)) {
                startActivity<MineTalkActivity>(MineTalkActivity.TYPE to type!!)
            }else{
                startActivity<LoginActivity>()
            }
        }

        p_name_key.setOnClickListener {
            //搜索帖子
            startActivity<SearchTalkActivity>(SearchTalkActivity.TYPE to type!!)

        }
        fatie.setOnClickListener {
            //发帖
            if (UserUtil.isLogin(mActivity)) {
                startActivity<WriteMyTalkActivity>(WriteMyTalkActivity.TYPE to type!!)
            }else{
                startActivity<LoginActivity>()
            }

        }
    }

    override fun initData() {
        mList = ArrayList()
        type  =intent.getStringExtra(TYPE)
        defaultTitle="贷款论坛"
        if (!TextUtils.isEmpty(type)) {
            if ("2".equals(type)){
                tv_title.text= "信用卡论坛"
                defaultTitle="信用卡论坛"
            }
        }
        mPresenter.loadListData(type!!)
        refresh.isEnableLoadMore = false
        refresh.isEnableRefresh =  true
        refresh.setOnRefreshListener( {
            mPresenter.loadListData(type!!)
        })

    }

    override fun onResume() {
        super.onResume()

    }



    override fun showListData(data: CardMoneyListBean) {
        refresh.finishRefresh()
        if (null==data){
            return
        }
        TCAgent.onEvent(mActivity, TalkingDataParams.DISCUSS_LIST, type)
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

            if (view.id == R.id.rl_layout) {
                val item = adapter.getItem(position) as CardMoneyListBean.DiscussListBean
                //详情
                var isComment = true
                if ("2".equals(mBean?.is_publish)) {
                    isComment = false
                }
                startActivity<TalkDetailActivity>(TalkDetailActivity.ARTICEID to item?.discuss_id, TalkDetailActivity.ISPUBLISH to isComment)
            }
        }
    }



    @Subscribe
    fun onWriteCompleteEvent(ecent : WriteCompleteEvent){
        //帖子发布成功 刷新
        mPresenter.loadListData(type!!)
    }

}
