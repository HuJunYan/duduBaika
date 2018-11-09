package com.dudubaika.ui.activity

import android.text.TextUtils
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.RefreshMsgCenterListData
import com.dudubaika.event.SetMsgIsRead
import com.dudubaika.model.bean.MsgCenterListBean
import com.dudubaika.presenter.contract.MsgCenterListContract
import com.dudubaika.presenter.impl.MsgCenterListPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import kotlinx.android.synthetic.main.activity_msg_center_list.*
import kotlinx.android.synthetic.main.item_meessage_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity

/**
 * 消息中心列表
 */
class MsgCenterListActivity : BaseActivity<MsgCenterListPresenter>(), MsgCenterListContract.View {

    private var mBean:MsgCenterListBean?=null
    //总的数据集合
    private var mDatas:ArrayList<MsgCenterListBean.MsgListBean>?=null
    //临时数据集合
    private var mCashData:ArrayList<MsgCenterListBean.MsgListBean>?=null
    private var pageIndex:Int=1

    //记录用户上次点过消息id 的集合
//    private var map:HashMap<String,Boolean>? = null


    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() = Unit

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        swirefresh.finishLoadMore()
        swirefresh.finishRefresh()
    }


    override fun getLayout(): Int =R.layout.activity_msg_center_list

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_view)
        defaultTitle="消息中心"
        iv_return.setOnClickListener {
            backActivity()
        }

        swirefresh.isEnableRefresh = true
        swirefresh.isEnableLoadMore  = false
        swirefresh.setOnRefreshListener({
            //下拉刷新
            pageIndex = 1
            mPresenter.getListData(pageIndex.toString(),"50","","")
        })

        swirefresh.setOnLoadMoreListener({
            //加载更多
            pageIndex++
            mPresenter.getListData(pageIndex.toString(),"50","","")
        })

        all_read.setOnClickListener {
            //全部设为已读
            EventBus.getDefault().postSticky(SetMsgIsRead(""))

            for(item in mDatas!!){

                if ("2".equals( item.is_read)){
                    item.is_read = "1"
                }

            }
            showView()
        }

    }

    override fun initData() {
        mDatas = ArrayList()
        mCashData = ArrayList()

    }
    override fun showDtaa(data: MsgCenterListBean) {
        swirefresh.finishLoadMore()
        swirefresh.finishRefresh()
        if (null==data){
            return
        }

        if (pageIndex==1){
            //刷新或者第一次使用
            mDatas = data!!.msg_list as ArrayList<MsgCenterListBean.MsgListBean>?
        }else{
            mCashData = data!!.msg_list as ArrayList<MsgCenterListBean.MsgListBean>?
            mDatas?.addAll(mCashData!!)
        }

        swirefresh.isEnableLoadMore = mCashData!!.size>50
        showView()
        pageIndex++

    }


    private fun showView() {


        listview.adapter = object : CommonAdapter<MsgCenterListBean.MsgListBean>(mActivity,mDatas,R.layout.item_meessage_list){
            override fun convert(holder: ViewHolder?, item: MsgCenterListBean.MsgListBean?, posotion: Int) {

                holder?.setOnClickListener(R.id.content,{

                    when(item?.msg_type){

                        "1"->{
                            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to item?.msg_url , WebActivity.WEB_URL_TITLE to item?.title)
                        }
                        "2"->{
                            startActivity<JpushDetailActivity>(JpushDetailActivity.MSG_ID to item?.msg_id)
                        }
                        "3"->{
                            ToastUtil.showToast(mActivity,"服务器维护，请稍后再试")
                        }
                    }
                    if ("2".equals(item!!.is_read) ) {
                       EventBus.getDefault().postSticky(SetMsgIsRead(item!!.msg_id))
                    }
                })
                holder?.setText(R.id.tv_message_time,item?.date_str)
                holder?.setText(R.id.tv_message_title,item?.title)
                holder?.setText(R.id.tv_message_content,item?.des)

                if ("1".equals(item!!.is_read)){
                  holder?.setTextColor(R.id.tv_message_title,resources.getColor(R.color.edit_text_hint_color))
                  holder?.setTextColor(R.id.tv_message_content,resources.getColor(R.color.edit_text_hint_color))
                }else{
                    holder?.setTextColor(R.id.tv_message_title,resources.getColor(R.color.global_txt_black4))
                    holder?.setTextColor(R.id.tv_message_content,resources.getColor(R.color.global_txt_black5))
                }
            }
        }
    }

    @Subscribe
    fun OnRefreshMsgCenterListData(event: RefreshMsgCenterListData){
        pageIndex =1
        mPresenter.getListData(pageIndex.toString(),"50","","")
    }

    override fun onResume() {
        super.onResume()
        pageIndex =1
        mPresenter.getListData(pageIndex.toString(),"50","","")
    }


    override fun onDestroy() {
        super.onDestroy()
        mDatas = null
        mCashData =null
    }

}
