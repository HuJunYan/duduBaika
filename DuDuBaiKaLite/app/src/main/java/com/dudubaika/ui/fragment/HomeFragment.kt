package com.dudubaika.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.base.GlobalParams
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.HomeContract
import com.dudubaika.presenter.impl.HomePresenter
import com.dudubaika.ui.activity.*
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.util.*
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.dialog_goto_auth.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

/**
 * 首页
 */
class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View {
    override fun showDialogForUser(data: HomeDialogForUser) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showHomeBottomData(data: HomeButtomDialogBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showBannerInfo(data: HomeTopInfo) {
    }

    //广告轮播图
    private var mBannerListBean: BannerListBean? = null

    //产品列表
    private var mProductListBean: ProductListBean? = null

    //用户点击的产品
    private var mClickProduct: ProductListBean.ProductBean? = null

    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()

    override fun getLayout(): Int = R.layout.fragment_home

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.setPaddingSmart(activity, tb_home)
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun initView() {
    }

    /**
     * 初次加载数据
     */
    override fun initData() {
    }

    /**
     * 登录成功刷新数据
     */
    fun refreshData() {
        initData()
    }

    /**
     * banner数据回调
     */
     fun showBanner(data: BannerListBean) {
        mBannerListBean = data
    }

    /**
     * 商品数据回调
     */
     fun showProduct(data: ProductListBean) {
        mProductListBean = data
        initRecyclerView()
    }

    /**
     * 显示loading圈
     */
    override fun showProgress() {
        srl_home.visibility = View.GONE
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE
    }

    /**
     * 隐藏loading圈
     */
    override fun hideProgress() {
        srl_home.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

    /**
     * 显示错误的ui
     */
    override fun showError(url: String, msg: String) {
        if (ApiSettings.GET_BANNER_LIST == url || ApiSettings.GET_PRODUCT_LIST == url) {
            srl_home.visibility = View.GONE
            progress.visibility = View.VISIBLE
            progress.ll_loading.visibility = View.GONE
            progress.ll_error.visibility = View.VISIBLE
            progress.ll_error.setOnClickListener {
                initData()
            }
        }
    }

    /**
     * 用户状态数据回调
     */
     fun onUserAuthResult(data: UserAuthResultBean) {
        action(activity!!, data, mClickProduct!!)
    }

    private fun initRecyclerView() {

        mAdapters.clear()

        //初始化
        //创建VirtualLayoutManager对象
        val layoutManager = VirtualLayoutManager(activity!!)
        recyclerView.layoutManager = layoutManager!!

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        recyclerView.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        val delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView.adapter = delegateAdapter

        //判断是否有banner数据
        val banner_list = mBannerListBean?.banner_list
        if (banner_list != null
        //当没有数据时 显示banner  显示出默认图来
//                && banner_list.size > 0
        ) {
            val bannerAdapter = initBannerAdapter()
            mAdapters.add(bannerAdapter)
        }

        //判断是否有置顶数据
        val top_list = mProductListBean?.top_list
        if (top_list != null && top_list.size > 0) {
            val titleAdapter1 = initTitleAdapter(mProductListBean?.top_title!!)
            //添加置顶数据上面的title
            mAdapters.add(titleAdapter1)
            val topAdapter = initTopProductAdapter()
            //添加置顶数据
            mAdapters.add(topAdapter)
            //添加置顶数据下面的分割线
            val wideLineAdapter = initWideLineAdapter()
            mAdapters.add(wideLineAdapter)
        }

        //判断是否有热门数据
        val hot_list = mProductListBean?.hot_list
        if (hot_list != null && hot_list.size > 0) {
            //热门数据的标题
            val titleAdapter2 = initTitleAdapter(mProductListBean?.hot_title!!)
            mAdapters.add(titleAdapter2)
            //热门数据
            val hotProductAdapter = initHotProductAdapter()
            mAdapters.add(hotProductAdapter)
        }

        srl_home.isEnableLoadMore = false
        srl_home.isEnableRefresh = true
        srl_home.setOnRefreshListener {
        }

        srl_home.finishRefresh()

        delegateAdapter.setAdapters(mAdapters)

    }

    /**
     * banner
     */
    fun initBannerAdapter(): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(), R.layout.item_banner, 1, GlobalParams.VLAYOUT_BANNER) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val banner = holder.getView<Banner>(R.id.banner)

                val urlList = mutableListOf<String>()
                for (bannerBean in mBannerListBean!!.banner_list) {
                    urlList.add(bannerBean.banner_url)
                }
                banner.setImages(urlList)
                        .setImageLoader(HeaderGlideImageLoader())
                        .setDelayTime(5000)
                        .setOnBannerListener { position ->
                            val banner = mBannerListBean!!.banner_list[position]
                            val jump_type = banner.jump_type

                            when (jump_type) {
                                "1" -> {//不能点击

                                }
                                "2" -> {//跳转到H5
                                    val banner_url = banner.jump_url
                                    startActivity<WebActivity>(WebActivity.WEB_URL_KEY to banner_url, WebActivity.WEB_URL_TITLE to "")
                                }
                                "3" -> {//跳转到详情
                                    val product_id = banner.product_id
                                    startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to product_id)
                                }
                            }
                        }
                        .start()

                // 根据屏幕宽度设置view 高度
                if (urlList.size > 0) {
                    DisplayUtil.setViewHeightByScreenWidth(_mActivity, urlList[0], banner)
                }
            }
        }
    }

    /**
     * 标题
     */
    fun initTitleAdapter(title: String): BaseDelegateAdapter {
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(), R.layout.item_title, 1, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                // 绑定数据
                val tv_title = holder.getView<TextView>(R.id.tv_vlayout_title)
                tv_title.text = title
            }
        }
    }

    /**
     * 置顶item
     */
    fun initTopProductAdapter(): BaseDelegateAdapter {

        val topList = mProductListBean!!.top_list

        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(), R.layout.item_top_product, topList.size, GlobalParams.VLAYOUT_TOPLIST) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)

                val product = topList[position]

                val iv_item_top_product_logo = holder.getView<ImageView>(R.id.iv_item_top_product_logo)
                val tv_item_top_product_name = holder.getView<TextView>(R.id.tv_item_top_product_name)
                val tv_item_top_product_quota_limit = holder.getView<TextView>(R.id.tv_item_top_product_quota_limit)
                val tv_item_top_product_des = holder.getView<TextView>(R.id.tv_item_top_product_des)
                val tv_top_product_item_loan_time = holder.getView<TextView>(R.id.tv_top_product_item_loan_time)
                val tv_top_product_item_apply_count = holder.getView<TextView>(R.id.tv_top_product_item_apply_count)
                val tv_top_product_item_now_apply = holder.getView<TextView>(R.id.tv_top_product_item_now_apply)

                //设置产品logo图片
                ImageUtil.loadNoCache(this@HomeFragment, iv_item_top_product_logo, product.logo_url, R.drawable.ic_item_placeholder)

                //设置产品名称
                tv_item_top_product_name.text = product.product_name
                //额度范围
                tv_item_top_product_quota_limit.text = product.quota_limit
                //产品描述
                tv_item_top_product_des.text = product.product_des
                //产品期限
                tv_top_product_item_loan_time.text = product.loan_time
                //申请人数
                tv_top_product_item_apply_count.text = product.apply_count

                tv_top_product_item_now_apply.setOnClickListener {
                    if (!UserUtil.isLogin(activity!!)) {
                        startActivity<LoginActivity>()
                        return@setOnClickListener
                    }
                    mClickProduct = product
                    action(mClickProduct)
//                    mPresenter.getUserAuthResult()
                }
            }
        }
    }

    /**
     * 热门item
     */
    fun initHotProductAdapter(): BaseDelegateAdapter {

        val hot_list = mProductListBean!!.hot_list

        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(), R.layout.item_hot_product, hot_list.size, GlobalParams.VLAYOUT_HOTLIST) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val product = hot_list[position]

                val iv_item_hot_product_logo = holder.getView<ImageView>(R.id.iv_item_hot_product_logo)
                val tv_item_hot_product_name = holder.getView<TextView>(R.id.tv_item_hot_product_name)
                val tv_item_hot_product_quota_limit = holder.getView<TextView>(R.id.tv_item_hot_product_quota_limit)
                val tv_item_hot_product_des = holder.getView<TextView>(R.id.tv_item_hot_product_des)
                val tv_hot_product_item_loan_time = holder.getView<TextView>(R.id.tv_hot_product_item_loan_time)
                val tv_hot_product_item_now_apply = holder.getView<TextView>(R.id.tv_hot_product_item_now_apply)

                //设置产品logo图片
                ImageUtil.loadNoCache(this@HomeFragment, iv_item_hot_product_logo, product.logo_url, R.drawable.ic_item_placeholder)
                //设置产品名称
                tv_item_hot_product_name.text = product.product_name
                //额度范围
                tv_item_hot_product_quota_limit.text = product.quota_limit
                //产品描述
                tv_item_hot_product_des.text = product.product_des
                //产品期限
                tv_hot_product_item_loan_time.text = product.loan_time

                tv_hot_product_item_now_apply.setOnClickListener {
                    if (!UserUtil.isLogin(activity!!)) {
                        startActivity<LoginActivity>()
                        return@setOnClickListener
                    }
                    mClickProduct = product
                    action(mClickProduct)
//                    mPresenter.getUserAuthResult()
                }

            }
        }
    }

    private fun action(bean: ProductListBean.ProductBean?) {
        if (bean == null) {
            return
        }
        val clickProductId = bean.product_id

        activity?.startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to clickProductId)
    }

    /**
     * 置顶产品和热门产品中间的分割线
     */
    private fun initWideLineAdapter(): BaseDelegateAdapter {
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),
                R.layout.item_wide_line_product, 1, GlobalParams.VLAYOUT_LINE) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
    }

    /**
     *   1.是否登录 (之前已经判断过) X
    2.认证项完整 X
    3.(auth_status == 1 && risk_status==1)跳转到认证中心页面
    4.risk_status == 2 跳转到等待中页面 X

    5.(risk_status == 3 && order_status == 2 && 点击自己的产品 )跳转到产品详情页面 X
    6.(risk_status == 3 && order_status == 2 && 点击第三方的产品) 跳转到贷款推荐页面（主推自己的产品） X

    7.(risk_status == 3 && order_status == 1 && 点击自己方的产品 )跳转到贷款推荐页面（主推第三方的产品）
    8.(risk_status == 3 && order_status == 1 && 点击第三方的产品 )产品详情页面

    9.(risk_status == 4 && 点击自己的产品 ) 跳转到贷款推荐页面（主推第三方的产品）
    10.(risk_status == 4 && 点击第三方的产品 ) 跳转到产品详情页面
     */
    fun action(activity: Activity, userAuthResultBean: UserAuthResultBean, product: ProductListBean.ProductBean) {
        val auth_status = userAuthResultBean.auth_status
        val risk_status = userAuthResultBean.risk_status
        val order_status = userAuthResultBean.order_status

        val clickProductId = product.product_id
        val clickProductType = product.product_type

        //显示认证项dialog
        if (auth_status == "2") {
            showGotoAuthDialog(activity)
            return
        }

        //跳转到认证中心页面
        if (auth_status == "1" && risk_status == "1") {
            activity.startActivity<CreditAssessmentActivity>()
            return
        }

        //跳转到等待中页面
        if (risk_status == "2") {
            activity.startActivity<RecommendActivity>()
            return
        }

        if (risk_status == "3" && order_status == "2") {
            if (clickProductType == "1") {
                activity.startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to clickProductId)
            } else if (clickProductType == "2") {
                activity.startActivity<RecommendResultActivity>(RecommendResultActivity.PRODUCT_TYPE_KEY to clickProductType)
            }
            return
        }

        if (risk_status == "4") {
            if (clickProductType == "1") {
                activity.startActivity<RecommendResultActivity>(RecommendResultActivity.PRODUCT_TYPE_KEY to clickProductType)
            } else if (clickProductType == "2") {
                activity.startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to clickProductId)
            }
            return
        }

        if (risk_status == "3" && order_status == "1") {
            if (clickProductType == "1") {
                activity.startActivity<RecommendResultActivity>(RecommendResultActivity.PRODUCT_TYPE_KEY to clickProductType)
            } else if (clickProductType == "2") {
                activity.startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to clickProductId)
            }
            return
        }
        ToastUtil.showToast(activity, "商品状态异常,请稍后再试")
    }

    /**
     * 显示认证的dialog
     */
    private fun showGotoAuthDialog(activity: Activity) {
        val mDialog = Dialog(activity, R.style.MyDialog)

        val mLayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.dialog_goto_auth, null, false)
        mDialog.setContentView(view)
        mDialog.setCancelable(false)
        view.tv_dialog_look_continue.setOnClickListener {
            mDialog.dismiss()
        }
        view.tv_dialog_goto_auth.setOnClickListener {
            activity.startActivity<CreditAssessmentActivity>()
            mDialog.dismiss()
        }
        mDialog.show()
    }


}