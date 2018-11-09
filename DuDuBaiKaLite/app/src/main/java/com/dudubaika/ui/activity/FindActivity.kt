package com.dudubaika.ui.activity

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.FindInitBean
import com.dudubaika.model.bean.UsersAuthLimitBean
import com.dudubaika.presenter.contract.FindContract
import com.dudubaika.presenter.impl.FindPresneter
import com.dudubaika.ui.adapter.FindListAdapter
import com.dudubaika.ui.adapter.FindTagAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_find.*
import org.jetbrains.anko.startActivity

/**
 * 机构筛选界面
 */
class FindActivity : BaseActivity<FindPresneter>(), FindContract.View {

    private var mInitBean:FindInitBean?=null
    private var mListBean:UsersAuthLimitBean?=null
    private var mFindTagAdapter:FindTagAdapter?=null
    private var mFindListAdapter:FindListAdapter?=null
    private var mTagList:List<FindInitBean.TagListBean>?=null
    private var mListData:ArrayList<UsersAuthLimitBean.ProductListBean>?=null
    private var mCashListData:ArrayList<UsersAuthLimitBean.ProductListBean>?=null
    private var mCorenerList:List<FindInitBean.CornerListBean>?=null
    private var index:Int=1

    //排序所需要传递的值
    private var quota:String=""
    private var lony_term:String=""
    private var cycle:String=""
    private var rate:String=""

    private var mech:String=""
    private var mark:String=""

    private var mProduct_type=""


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
        refresh.finishLoadMore()
    }

    override fun showError(url: String, msg: String) {
        refresh.finishRefresh()
        refresh.finishLoadMore()
    }

    override fun getLayout(): Int = R.layout.activity_find

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,product_tb)
        defaultTitle="机构筛选"
        iv_return.setOnClickListener {
            backActivity()
        }
        view1.setText("贷款额度")
        view2.setText("下款时长")
        view3.setText("借款周期")
        view4.setText("利率")
        view1.setmCurrentStatus("up")
        view2.setmCurrentStatus("down")
        view3.setmCurrentStatus("up")
        view4.setmCurrentStatus("down")

        view1.setmStatus("down")
        view2.setmStatus("up")
        view3.setmStatus("down")
        view4.setmStatus("up")

        view1.setMyListener {
            mProduct_type=""
//            mFindListAdapter?.tag="贷款额度"
            index=1
//            hintView()
            quota = view1.getmStatus()
            setTopTitleDefaultStatue()

            lony_term=""
            cycle=""
            rate=""
//            reFreshView()
//            mPresenter.getSortData(index,"10",quota,lony_term,cycle,rate,mech,mark)
            startActivity<FindDetailActivity>(FindDetailActivity.QUOTA to  quota)

        }
        view2.setMyListener {
            mProduct_type=""
//            mFindListAdapter?.tag="下款时长"
//            hintView()
            index=1

            quota=""
            lony_term = view2.getmStatus()
            cycle=""
            rate=""
            setTopTitleDefaultStatue()
//            reFreshView()
            startActivity<FindDetailActivity>(FindDetailActivity.LONY_TERM to  lony_term)
        }
        view3.setMyListener {
            mProduct_type=""
//            mFindListAdapter?.tag="借款周期"
//            hintView()
            index=1

            quota=""
            lony_term=""
            cycle = view3.getmStatus()
            rate=""
            setTopTitleDefaultStatue()
//            reFreshView()
            startActivity<FindDetailActivity>(FindDetailActivity.CYCLE to  cycle)
        }
        view4.setMyListener {
            mProduct_type=""
//            mFindListAdapter?.tag="利率"
//            hintView()
            index=1
            quota=""
            lony_term=""
            cycle=""
            rate = view4.getmStatus()
//            reFreshView()
            setTopTitleDefaultStatue()
            startActivity<FindDetailActivity>(FindDetailActivity.RATE to  rate)
        }

        refresh.isEnableRefresh = false
        refresh.isEnableLoadMore = false
        refresh.setOnRefreshListener {
            index=1
            mPresenter.getSortData(index,"10",quota,lony_term,cycle,rate,mech,mark)
        }

        refresh.setOnLoadMoreListener({
            index++
            mPresenter.getSortData(index,"10",quota,lony_term,cycle,rate,mech,mark)

        })

    }

    private fun setTopTitleDefaultStatue() {
        view2.setDefaultStatus()
        view3.setDefaultStatus()
        view1.setDefaultStatus()
        view4.setDefaultStatus()
    }

    override fun initData() {
        mTagList  = ArrayList()
        mCorenerList  = ArrayList()
        mPresenter.getTopData()

    }

    override fun showSortData(data: UsersAuthLimitBean) {
        refresh.finishRefresh()
        refresh.finishLoadMore()
        if (null == data){
            return
        }
        refresh.isEnableRefresh = true
        refresh.isEnableLoadMore = true
        mListBean = data
        mCashListData = mListBean?.product_list
        if (index==1){
            mListData?.clear()
            mListData=mCashListData
            showListData()
        }else{
            mListData?.addAll(mCashListData!!)
        }
        mFindListAdapter?.notifyDataSetChanged()
        mFindListAdapter?.tag =mProduct_type
        if (mListBean!!.product_list.size<10){
            refresh.finishLoadMoreWithNoMoreData()

        }



    }

    override fun showInitData(data: FindInitBean) {
        refresh.finishRefresh()
        refresh.finishLoadMore()
        if(null==data){
            return
        }
        mInitBean = data
        mTagList = mInitBean?.tag_list
        mCorenerList = mInitBean?.corner_list
        showTag()
        showCorner()

    }


    //显示机构标签
    private fun showCorner() {
        val manager = MyGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(false)
        tag2.layoutManager =   manager
        tag2.adapter = object : BaseQuickAdapter<FindInitBean.CornerListBean, BaseViewHolder>(R.layout.item_find_tag,mCorenerList){
            override fun convert(helper: BaseViewHolder?, item: FindInitBean.CornerListBean?) {
                 helper?.setText(R.id.tv_tag, item?.getTitle())
                helper?.getView<TextView>(R.id.tv_tag)?.setOnClickListener {
                    mark  = item!!.title
                    index = 1
//                    hintView()
                    mProduct_type=mark
                    startActivity<FindDetailActivity>(FindDetailActivity.MARK to  mark,
                            FindDetailActivity.MPRODUCT_TYPE to mProduct_type)
//                    mPresenter.getSortData(1,"10",quota,lony_term,cycle,rate,mech,mark)
                }
            }
        }

    }

    //显示标签数据
    private fun showTag() {
        mFindTagAdapter = FindTagAdapter(mTagList,this)
        val manager = MyGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(false)
        tag1.layoutManager =   manager
        tag1.adapter = mFindTagAdapter
        mFindTagAdapter?.setOnItemClickListener { adapter, view, position ->
            //点击事件
            val item = adapter.getItem(position) as FindInitBean.TagListBean
            mech  = item!!.title
            index =1
//            hintView()
            mProduct_type=mech
            startActivity<FindDetailActivity>(FindDetailActivity.MECH to  mech,
                    FindDetailActivity.MPRODUCT_TYPE to mProduct_type)
//            mPresenter.getSortData(1,"10",quota,lony_term,cycle,rate,mech,mark)
        }
    }

    private fun hintView() {
        tag1.visibility = View.GONE
        tag2.visibility = View.GONE
    }


    private fun showListData() {
        mFindListAdapter = FindListAdapter(mListData,mActivity,mProduct_type)
        val manager = MyGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(true)
        tag3.layoutManager =   manager
        tag3.adapter = mFindListAdapter

    }

    private fun reFreshView(){
        refresh.isEnableRefresh = true
        refresh.isEnableLoadMore = true
        refresh.setNoMoreData(false)
    }


    override fun onResume() {
        super.onResume()
        setTopTitleDefaultStatue()
    }

}
