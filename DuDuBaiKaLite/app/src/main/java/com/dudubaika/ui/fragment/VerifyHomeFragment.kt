package com.dudubaika.ui.fragment

import android.content.Context
import android.net.Uri
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.event.HomeDataRefreEvent
import com.dudubaika.model.bean.VerifyHomeDataBean
import com.dudubaika.presenter.contract.VerifyHomeContract
import com.dudubaika.presenter.impl.VerifyHomePresenter
import com.dudubaika.ui.adapter.HomeVerifyAdapter
import com.dudubaika.ui.adapter.VerifyAdapter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.VerifyHeaderGlideImageLoader
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_verify_home.*
import kotlinx.android.synthetic.main.layout_header_verify_banner.view.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.displayMetrics
import android.content.Intent
import android.widget.Toast
import com.dudubaika.model.bean.VerifyProductDetailBean
import org.jetbrains.anko.support.v4.toast


class VerifyHomeFragment : BaseFragment<VerifyHomePresenter>(), VerifyHomeContract.View {


    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mList: ArrayList<VerifyHomeDataBean.ProductBean>? = null
    private var mAdapter: VerifyAdapter? = null
    private var header: View? = null
    private var mHomeData: VerifyHomeDataBean? = null
    private var productList: ArrayList<VerifyProductDetailBean.ProductDetailBean>?=null

    override fun getLayout(): Int = R.layout.fragment_verify_home

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        srl_verify_home.finishRefresh()
    }

    override fun processVerifyHomeData(data: VerifyHomeDataBean?) {
        srl_verify_home.finishRefresh()
        if (data == null) {
            return
        }
        mHomeData = data
        refreshHeaderData()
        mList?.clear()
        val hot_list = data.hot_list
        if (hot_list != null && hot_list.size != 0) {
            for (index in hot_list.indices) {
                hot_list.get(index).local_item_type = HomeVerifyAdapter.LOCAL_TYPE_HOT
            }
            mList?.addAll(hot_list)
        }
//
        mAdapter?.notifyDataSetChanged()
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(activity, tb_verify_home)
        tv_home_title.paint.isFakeBoldText = true
        srl_verify_home.isEnableLoadMore = false
        srl_verify_home.isEnableRefresh = true
        srl_verify_home.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                mPresenter.getVerifyHomeData()
            }
        })
        initRecyclerView()
        initHeaderView()
    }


    private fun initHeaderView() {
        val mLayoutInflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        header = mLayoutInflater.inflate(R.layout.layout_header_verify_banner, recycler_view_verify_home, false)
        val widthPixels = activity!!.displayMetrics.widthPixels
        val layoutParams = header?.banner?.layoutParams
        layoutParams?.height = widthPixels * 350 / 720
        header?.banner?.layoutParams = layoutParams
        header?.banner?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
        header?.banner?.setBannerStyle(BannerConfig.NOT_INDICATOR)
    }

    fun initRecyclerView() {
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        if (mList == null) {
            mList = ArrayList()
        }

        if (mAdapter == null) {
            mAdapter = VerifyAdapter(activity,mList)
        }
        recycler_view_verify_home.layoutManager = mLinearLayoutManager
        //设置适配器
        recycler_view_verify_home.adapter = mAdapter
    }

    override fun initData() {
        mPresenter.getVerifyHomeData()
    }

    fun refreshHeaderData() {
        if (header == null) {
            initHeaderView()
        }
        if (mHomeData == null || mHomeData?.banner_list == null) {
            return
        }
        //设置banner 数据
        header?.banner?.setImages(mHomeData?.banner_list)
                ?.setImageLoader(VerifyHeaderGlideImageLoader())
                ?.setDelayTime(5000)
                ?.setOnBannerListener { position ->
                    val bannerBean = mHomeData?.banner_list!![position]
                    val jump_url = bannerBean.jump_url
                    val uri = Uri.parse(jump_url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    //跳转到相应的URL地址
//                    startActivity(intent)
                }
                ?.start()

        mAdapter?.setHeaderView(header)
    }


    @Subscribe
    fun refreData(event: HomeDataRefreEvent) {


    }

    fun refreshData() {
        mPresenter.getVerifyHomeData()
    }

}