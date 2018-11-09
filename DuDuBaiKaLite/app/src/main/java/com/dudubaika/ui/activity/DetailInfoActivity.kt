package com.dudubaika.ui.activity

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.BuyDetailBean
import com.dudubaika.model.bean.NowApplyBean
import com.dudubaika.model.bean.ProductInfoBean
import com.dudubaika.presenter.contract.BuyDetailContract
import com.dudubaika.presenter.impl.BuyDetailPresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_detail_info.*
import kotlinx.android.synthetic.main.view_buy_detail_item.view.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.jetbrains.anko.startActivity

/**
 * 商品详情
 */
class DetailInfoActivity : BaseActivity<BuyDetailPresenter>(), BuyDetailContract.View {

    private var product_id :String= ""
    private var product_name :String= ""

    override fun initInject() {
        activityComponent.inject(this)

    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    private var mData: ProductInfoBean? = null

    companion object {
        var PRODUCT_NAME = "product_name"//商品名称
        var PRODUCT_ID = "product_id"// 商品id
    }


    override fun getLayout(): Int = R.layout.activity_detail_info

    override fun showProgress() {
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE

    }

    override fun showError(url: String, msg: String) {
        progress.ll_loading.visibility = View.GONE
        progress.ll_error.visibility = View.VISIBLE
        progress.ll_error.setOnClickListener {
            initData()
        }
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_info_detail)
        iv_buy_detail.setOnClickListener { backActivity() }
        tv_buy_detail_product_name.paint.isFakeBoldText = true
        tv_buy_detail_apply.setOnClickListener { onClickApply() }
    }

    private fun onClickApply() {
        if (TextUtils.isEmpty(product_id)) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        mPresenter.nowApply(product_id)
    }

    override fun initData() {


        if (!TextUtils.isEmpty(intent.getStringExtra(PRODUCT_ID))) {
            product_id = intent.getStringExtra(PRODUCT_ID)
        }

        mPresenter.getProductDetailData(product_id,"")
    }

    override fun processProductDetailData(data: ProductInfoBean?) {
        mData = data
        refreshUI()
    }

    private fun refreshUI() {
        if (mData == null) {
            return
        }
       /* product_name = mData!!.product_name

        ImageUtil.loadNoCache(mActivity,iv_buy_detail_icon,mData!!.product_logo_url,R.drawable.ic_item_placeholder)

        tv_buy_detail_title.text = mData?.product_name
        tv_buy_detail_product_name.text = mData?.product_name
        tv_buy_detail_people_num.text = "已申请成功: ${mData?.product_apply_count} 人"
        tv_buy_detail_amount.text = "${mData?.product_loan_limit}元"
        tv_buy_detail_date.text = "${mData?.product_loan_term}天"
        tv_buy_detail_rate.text = "日费率${mData?.product_day_rate}"
        tv_buy_detail_rate_desc.text = mData?.product_day_rate_desc
        //处理换行问题
        var replacedLimitStr = mData?.product_apply_limit?.replace("\\n", "\n")*/
//        tv_buy_detail_conditions.text = replacedLimitStr
        refreshFlowLayoutData()

    }

    private fun refreshFlowLayoutData() {
        val widthPixels = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        var itemWidth = ((widthPixels - 30 * density) / 4).toInt()
        var mInflater = LayoutInflater.from(this)
      /*  tfl_flowlayout.adapter = object : TagAdapter<BuyDetailBean.ProductCreditInfo>(mData?.product_credit_info) {
            override fun getView(parent: FlowLayout, position: Int, data: BuyDetailBean.ProductCreditInfo): View {
                val itemView = mInflater.inflate(R.layout.view_buy_detail_item,
                        tfl_flowlayout, false)
                val layoutParams = itemView.layoutParams
                layoutParams.width = itemWidth
                itemView.layoutParams = layoutParams
                itemView.tv_auth_item.text = data.credit_name
                ImageUtil.loadNoCache(mActivity, itemView.iv_view_buy_detail_icon, data.logo_url, R.drawable.ic_item_placeholder)
                return itemView
            }
        }*/
    }

    override fun processNowApplyData(data: NowApplyBean?) {
        if (data == null) {
            return
        }
        startActivity<WebActivity>(
                WebActivity.WEB_URL_KEY to data.jump_url,
                WebActivity.WEB_URL_TITLE to product_name)
    }


}
