package com.dudubaika.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.model.bean.ProductListBean
import com.dudubaika.model.bean.ProductRecommentBean
import com.dudubaika.model.bean.UserAuthResultBean
import com.dudubaika.presenter.contract.RepositoriesRusultContract
import com.dudubaika.presenter.impl.RepositoriesRusultPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.util.*
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.activity_recommend_result.*
import kotlinx.android.synthetic.main.dialog_goto_auth.view.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.jetbrains.anko.startActivity

/**
 * 推荐结果页面
 */

class RecommendResultActivity : BaseActivity<RepositoriesRusultPresenter>(), RepositoriesRusultContract.View {

    private var mProductType: String = ""

    companion object {
        var PRODUCT_TYPE_KEY = "product_type"//产品类型
    }

    override fun getLayout(): Int = R.layout.activity_recommend_result

    private var mProductRecommentBean: ProductRecommentBean? = null

    private var mProductListBean: ProductListBean? = null

    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()

    //用户点击的产品
    private var mClickProduct: ProductListBean.ProductBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProductType = intent.getStringExtra(PRODUCT_TYPE_KEY)
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.setPaddingSmart(this, tb_recommend_result)
    }

    override fun initView() {
        iv_recommend_result_back.setOnClickListener {
            backActivity()
        }
    }

    override fun initData() {
        mPresenter.productRecommend()
    }

    override fun showProductRecommentTop(data: ProductRecommentBean) {
        mProductRecommentBean = data
        if (mClickProduct != null) {
            mProductType = mClickProduct!!.product_type
        }
        mPresenter.getProductList(mProductType)
    }

    override fun showProduct(data: ProductListBean) {
        mProductListBean = data
        initRecyclerView()
    }

    override fun onUserAuthResult(data: UserAuthResultBean) {
        action(mActivity, data, mClickProduct!!)
    }

    override fun showProgress() {
        recyclerView_recommend.visibility = View.GONE
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
        recyclerView_recommend.visibility = View.VISIBLE
    }

    override fun showError(url: String, msg: String) {
        recyclerView_recommend.visibility = View.GONE
        progress.ll_loading.visibility = View.GONE
        progress.ll_error.visibility = View.VISIBLE
        progress.ll_error.setOnClickListener {
            initData()
        }
    }

    fun initRecyclerView() {

        mAdapters.clear()

        //初始化
        //创建VirtualLayoutManager对象
        val layoutManager = VirtualLayoutManager(mActivity)
        recyclerView_recommend.layoutManager = layoutManager

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        recyclerView_recommend.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        val delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView_recommend.adapter = delegateAdapter

        //判断是否有顶部图片
        val recommend_image = mProductRecommentBean?.recommend_image
        if (!TextUtils.isEmpty(recommend_image)) {

            //添加置顶数据下面的分割线
            val wideLineAdapter = initWideLineAdapter()
            mAdapters.add(wideLineAdapter)

            val bannerAdapter = initBannerAdapter()
            mAdapters.add(bannerAdapter)

            //添加置顶数据下面的分割线
            val wideLineAdapter222 = initWideLineAdapter()
            mAdapters.add(wideLineAdapter222)
        }

        //判断是否有热门数据
        val hot_list = mProductListBean?.hot_list
        if (hot_list != null && hot_list.size > 0) {

            //热门数据
            val hotProductAdapter = initHotProductAdapter()
            mAdapters.add(hotProductAdapter)
        }

        delegateAdapter.setAdapters(mAdapters)

    }

    /**
     * banner
     */
    fun initBannerAdapter(): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(), R.layout.item_banner, 1, GlobalParams.VLAYOUT_BANNER) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val banner = holder.getView<Banner>(R.id.banner)
                val urlList = mutableListOf<String>()
                urlList.add(mProductRecommentBean!!.recommend_image)
                banner.setImages(urlList)
                        .setImageLoader(HeaderGlideImageLoader())
                        .setDelayTime(5000)
                        .start()

                // 根据屏幕宽度设置view 高度
                if (urlList.size > 0) {
                    DisplayUtil.setViewHeightByScreenWidth(mActivity, urlList[0], banner)
                }
            }
        }
    }

    /**
     * 热门item
     */
    fun initHotProductAdapter(): BaseDelegateAdapter {

        val hot_list = mProductListBean!!.hot_list

        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(), R.layout.item_hot_product, hot_list.size, GlobalParams.VLAYOUT_HOTLIST) {
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
                ImageUtil.loadNoCache(mActivity, iv_item_hot_product_logo, product.logo_url, R.drawable.ic_banner_placeholder)
                //设置产品名称
                tv_item_hot_product_name.text = product.product_name
                //额度范围
                tv_item_hot_product_quota_limit.text = product.quota_limit
                //产品描述
                tv_item_hot_product_des.text = product.product_des
                //产品期限
                tv_hot_product_item_loan_time.text = product.loan_time

                tv_hot_product_item_now_apply.setOnClickListener {
                    if (!UserUtil.isLogin(mActivity)) {
                        startActivity<LoginActivity>()
                        return@setOnClickListener
                    }
                    mClickProduct = product
                    mPresenter.getUserAuthResult()
                }

            }
        }
    }

    /**
     * 置顶产品和热门产品中间的分割线
     */
    fun initWideLineAdapter(): BaseDelegateAdapter {
        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(), R.layout.item_wide_line_product, 1, GlobalParams.VLAYOUT_LINE) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
            }
        }
    }

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
                initData()
            }
            return
        }

        if (risk_status == "4") {
            if (clickProductType == "1") {
                initData()
            } else if (clickProductType == "2") {
                activity.startActivity<DetailInfoActivity>(DetailInfoActivity.PRODUCT_ID to clickProductId)
            }
            return
        }

        if (risk_status == "3" && order_status == "1") {
            if (clickProductType == "1") {
                initData()
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