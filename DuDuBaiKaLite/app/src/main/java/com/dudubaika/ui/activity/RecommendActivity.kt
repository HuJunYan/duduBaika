package com.dudubaika.ui.activity

import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.ShowHomeTabEvent
import com.dudubaika.model.bean.UserAuthResultBean
import com.dudubaika.presenter.contract.RecommendContract
import com.dudubaika.presenter.impl.RecommendPresenter
import com.dudubaika.util.StatusBarUtil
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_recommend.*
import org.greenrobot.eventbus.EventBus

/**
 * 推荐中页面
 */

class RecommendActivity() : BaseActivity<RecommendPresenter>(), RecommendContract.View {

    var isRiskFinish = false;
    override fun getLayout(): Int = R.layout.activity_recommend


    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(this, tb_recommend)
        srl_recommend.isEnableLoadMore = false
        srl_recommend.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                mPresenter.getUserAuthResult()
            }
        })
        tv_back_home.setOnClickListener {
            gotoActivity(mActivity, MainActivity::class.java, null)
            EventBus.getDefault().post(ShowHomeTabEvent())
            backActivity()
        }
    }

    override fun initData() {
        mPresenter.getUserAuthResult()

    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        srl_recommend.finishRefresh()
    }

    override fun processData(userAuthResultBean: UserAuthResultBean) {
        srl_recommend.finishRefresh()
        //风控评测完毕(成功/失败)
        if (userAuthResultBean.risk_status == "3" || userAuthResultBean.risk_status == "4") {
            gotoActivity(mActivity, MainActivity::class.java, null)
            isRiskFinish = true
            backActivity()
        }

    }


}
