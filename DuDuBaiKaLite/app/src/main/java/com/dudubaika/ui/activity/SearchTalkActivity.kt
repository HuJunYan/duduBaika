package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.CardMoneyListBean
import com.dudubaika.presenter.contract.SearchTalkContract
import com.dudubaika.presenter.impl.SearchTalkPresenter
import com.dudubaika.ui.adapter.TalkHomeListAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_search_talk.*
import org.jetbrains.anko.startActivity

/**
 * 搜索帖子界面
 */
class SearchTalkActivity : BaseActivity<SearchTalkPresenter>(), SearchTalkContract.View {

    private var mBean:CardMoneyListBean?=null
    private var mAdapter:TalkHomeListAdapter?=null
    private var type :String?=null
    private var mList :ArrayList<CardMoneyListBean.DiscussListBean>?=null

    companion object {
        var TYPE="type"
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int = R.layout.activity_search_talk

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb)

        tv_cancle.setOnClickListener {
            backActivity()
        }
        type  = intent.getStringExtra(TYPE)
        defaultTitle="贷款帖子搜索"
         if ("2"==type){
             defaultTitle="信用卡帖子搜索"
         }
        search.setOnEditorActionListener { v, actionId, event ->
           //搜索
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if (!TextUtils.isEmpty(search.text.toString().trim())){
                    mPresenter.search(search.text.toString().trim(),type!!)
                }else{
                    ToastUtil.showToast(mActivity,"请输入搜索内容")
                }
            }
            false
        }


    }

    override fun initData() {
        mList = ArrayList()
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showSearchListData(data: CardMoneyListBean) {
        //搜索的数据
        if (null==data){
            return
        }
        TCAgent.onEvent(mActivity, TalkingDataParams.DISCUSS_SEARCH, type)
        mBean= data
        if (null!=data.discuss_list && data.discuss_list.size==0){
            ToastUtil.showToast(mActivity,"没有您搜索的帖子")
            return
        }
        mList?.clear()
        mList?.addAll(data.discuss_list)
        mAdapter = TalkHomeListAdapter(mList,this)
        val manager = MyGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(true)
        search_list.layoutManager =   manager
        search_list.adapter = mAdapter
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item =  adapter.getItem(position) as CardMoneyListBean.DiscussListBean
            //详情
            var isComment= true
            if ("2".equals(mBean?.is_publish)){
                isComment = false
            }
            startActivity<TalkDetailActivity>(TalkDetailActivity.ARTICEID to item?.discuss_id , TalkDetailActivity.ISPUBLISH to isComment)
        }


    }

}
