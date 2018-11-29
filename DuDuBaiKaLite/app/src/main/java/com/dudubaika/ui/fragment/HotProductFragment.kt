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
import com.dudubaika.event.HomeReturnTop
import com.dudubaika.event.HomeFragmentIsDown
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.HomeButtomDialogBean
import com.dudubaika.model.bean.HomeListIsDown
import com.dudubaika.model.bean.HomeRefreshEvent
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.ProductContract
import com.dudubaika.presenter.impl.ProductPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.ui.adapter.HomeFtagmentListAdapter
import com.dudubaika.ui.adapter.ProductAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.RecycleUtils
import com.dudubaika.util.ToastUtil
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment1.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@SuppressLint("ValidFragment")
class HotProductFragment @SuppressLint("ValidFragment")
constructor(var product_type:String):BaseFragment<ProductPresenter>(), ProductContract.View{

   constructor() : this("")

    private var  hotProductAdapter:BaseDelegateAdapter?=null
    private var mBean:ProductInfoListBean? =null
    private var totalList:ArrayList<ProductInfoListBean.ProductListBean> ?=null
    private var mCashListData:ArrayList<ProductInfoListBean.ProductListBean> ?=null
    private var index:Int=1
    private var pageSize:String="10"
    private var  layoutManager:VirtualLayoutManager?=null
    private  var delegateAdapter:DelegateAdapter?=null
    private  var mFindListAdapter: HomeFtagmentListAdapter?=null




    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress(){
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

        if (null !=refresh){
            refresh?.finishLoadMore(false)
        }
        if (ApiSettings.GET_BANNER_LIST == url || ApiSettings.GET_PRODUCT_LIST == url) {
            refresh.visibility = View.GONE
            progress.visibility = View.VISIBLE
            progress.ll_loading.visibility = View.GONE
            progress.ll_error.visibility = View.VISIBLE
            progress.ll_error.setOnClickListener {
                mPresenter.getProductInfo(product_type,index.toString(),pageSize)
            }
        }

    }

    private var hot_list:ArrayList<String>?=null
    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()
    override fun getLayout(): Int = R.layout.fragment1

    override fun initView() {
        defaultTitle="首页tab第1个界面"
        val refreshFooter = refresh.refreshFooter
        val header = refresh.refreshHeader

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (recyclerView!!.canScrollVertically(-1)){
                    // -1 表示 页面内容向下滑动， 1 表示向上。
                    LogUtil.i("recyclerView","可以继续向下滑动 ")
                    EventBus.getDefault().postSticky(HomeListIsDown(true,"1"))
//                    EventBus.getDefault().postSticky(IsShowGoTopEvent(false,"1"))
                    EventBus.getDefault().postSticky(HomeFragmentIsDown(true))
                }else{
                    EventBus.getDefault().postSticky(HomeListIsDown(false,"1"))
//                    EventBus.getDefault().postSticky(IsShowGoTopEvent(true,"1"))
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
     /*   //是否在加载的时候禁止列表的操作
        refresh.setDisableContentWhenLoading(true)
//        refresh.setEnableOverScrollBounce(true);//是否启用越界回弹
        refresh.setEnableOverScrollDrag(false)//是否启用越界拖动
        refresh.setEnableNestedScroll(false)//是否启用嵌套滚动*/
        refresh.setEnableOverScrollDrag(false)
        refresh.isEnablePureScrollMode = false

        refresh.setOnLoadMoreListener {
            index++
            mPresenter.getProductInfo("1",index.toString(),pageSize)
        }
    }

    override fun initData() {
        index =1
        totalList = ArrayList()
        mCashListData = ArrayList()
        mPresenter.getProductInfo("1",index.toString(),pageSize)
    }

    override fun getPrductInfo(data: ProductInfoListBean?) {

        if (null==data){
            return
        }
        mBean = data

        if (null!=mBean?.product_list) {
            //----
            mCashListData = mBean?.product_list
            if (index==1){
                totalList?.clear()
                totalList=mCashListData
                showListData()
            }else{
                totalList?.addAll(mCashListData!!)
            }
            mFindListAdapter?.notifyDataSetChanged()
            if (mBean!!.product_list.size<10){
                refresh.finishLoadMoreWithNoMoreData()
            }

        }else{
            ToastUtil.showToast(mContext,"数据错误")
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

    override fun getButtomDialogData(data: HomeButtomDialogBean) {

        //底部首页数据
    }

    //界面数据赋值
    private fun showView() {
//        mAdapters.clear()
        //初始化
        //创建VirtualLayoutManager对象
        layoutManager = VirtualLayoutManager(activity!!)
        recyclerView.layoutManager = layoutManager!!

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        recyclerView.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        delegateAdapter = DelegateAdapter(layoutManager!!, true)
        recyclerView.adapter = delegateAdapter

        //热门数据
//        hotProductAdapter = initHotProductAdapter()
        hotProductAdapter = ProductAdapter(mContext!!,totalList!!,product_type,GlobalParams.FALG_TWO).initHotProductAdapter()
        mAdapters.add(hotProductAdapter!!)
        delegateAdapter!!.setAdapters(mAdapters)
    }

    /**
     * 热门item
     */
    fun initHotProductAdapter(): BaseDelegateAdapter {


        return object : BaseDelegateAdapter(activity, LinearLayoutHelper() , R.layout.item_hot_product2, totalList!!.size, GlobalParams.VLAYOUT_HOTLIST) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)

                if (null !=totalList && totalList!!.size>0) {
                    val item = totalList!![position]

                    val circleImageView = holder.getView<CircleImageView>(R.id.profile_image)
                    ImageUtil.loadNoCache(mContext!!, circleImageView, item.product_logo, R.drawable.ic_error_close)
                    holder.getView<TextView>(R.id.product_name).text = item.product_name

                    if (!TextUtils.isEmpty(item.activity_name)) {
                        var tv = holder.getView<TextView>(R.id.hot)
                        tv.text = item.activity_name

                        when (item!!.activity_name) {
                            "周期长" -> tv.setBackgroundResource(R.drawable.zqc)
                            "下款快" -> tv.setBackgroundResource(R.drawable.mine_ed_2)
                            "额度高" -> tv.setBackgroundResource(R.drawable.mine_ed_3)
                            "新上线" -> tv.setBackgroundResource(R.drawable.hd)
                            else -> tv.setBackgroundResource(R.drawable.hd)

                        }
                    } else {
                        holder.getView<TextView>(R.id.hot).visibility = View.GONE
                    }

                    //额度
                    holder.getView<TextView>(R.id.ed_money_value1).text = item.quota_start_value

                    if (!TextUtils.isEmpty(item.quota_end_value)) {
                        holder.getView<TextView>(R.id.ed_money_value2).text = ""
                        holder.getView<TextView>(R.id.ed_money_value3).text = "-" + item.quota_end_value
                        holder.getView<TextView>(R.id.ed_money_value4).text = item.quota_end_unit
                    } else {
                        holder.getView<TextView>(R.id.ed_money_value2).text = item.quota_start_unit
                    }
                    holder.getView<TextView>(R.id.ed).text = item.quota_name

                    //利率
                    holder.getView<TextView>(R.id.qx).text = item.rate_unit + item.rate_name
                    holder.getView<TextView>(R.id.qx_time_value3).text = item.rate_value

                    //下款时长
                    holder.getView<TextView>(R.id.yll).text = item.loan_name
                    holder.getView<TextView>(R.id.yll_value1).text = item.loan_time_value
                    holder.getView<TextView>(R.id.yll_value2).text = item.loan_time_unit

                    //期限20天-36月
                    if (TextUtils.isEmpty(item.term_end_value)) {
                        holder.getView<TextView>(R.id.cycle).text = item.term_start_value + " " + item.term_start_unit
                    } else {
                        holder.getView<TextView>(R.id.cycle).text = item.term_start_value + " " + item.term_start_unit + "-" + item.term_end_value + " " + item.term_end_unit
                    }
                    //已放款人数
                    holder.getView<TextView>(R.id.money_spend).text = "今日已放款" + " " + item.apply_count + " 人"

                    val linearLayout = holder.getView<LinearLayout>(R.id.tag_list)

                    if (linearLayout.childCount > 0) {
                        linearLayout.removeAllViews()
                    }

                    var index = 1
                    for (item in item.product_tags) {

                        //添加标签组
                        val showText = TextView(mContext)
                        showText.textSize = 10f
                        showText.text = item.tag_name
                        showText.gravity = Gravity.CENTER
                        showText.setPadding(20, 5, 20, 5)
                        if ("1" == item.is_light) {
                            showText.setBackgroundResource(R.drawable.shape_home_tag)
                            showText.setTextColor(resources.getColor(R.color.home_item_tag))
                        } else {
                            showText.setBackgroundResource(R.drawable.shape_home_tag)
                            showText.setTextColor(resources.getColor(R.color.global_edit))
                        }
                        if (index != 1) {
                            // set 文本大小
                            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT)
                            //set 四周距离
                            params.setMargins(40, 0, 0, 0)
                            showText.layoutParams = params
                        }
                        linearLayout.addView(showText)
                        index++
                    }
                    holder.getView<LinearLayout>(R.id.itme_product).setOnClickListener {

                    }
                }
            }
        }
    }

    @Subscribe
    public fun refreshDaata(event:HomeRefreshEvent){
        //刷新事件
        if (!TextUtils.isEmpty(event.type)){
            if (product_type==event.type){
                index=1
                mPresenter.getProductInfo(event.type,index.toString(),pageSize)
            }
        }
    }

    @Subscribe
    public fun OnHomeReturnTop(event: HomeReturnTop){
        //回到顶部
        if ("1".equals(event.type)){
            RecycleUtils.smoothMoveToPosition(recyclerView,0)
            refresh.finishLoadMore()
        }

    }


}