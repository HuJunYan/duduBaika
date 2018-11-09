package com.dudubaika.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.PostAssessmentEvent
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.model.bean.CreditAssessBean
import com.dudubaika.model.bean.CreidtApplyBean
import com.dudubaika.model.bean.LastSmsTimeBean
import com.dudubaika.presenter.contract.CreditAssessmentContract
import com.dudubaika.presenter.impl.CreditAssessmentPresenter
import com.dudubaika.ui.adapter.CreditAdapter
import com.dudubaika.util.*
import kotlinx.android.synthetic.main.activity_credit_assessment.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 认证中心页面
 */
class CreditAssessmentActivity : BaseActivity<CreditAssessmentPresenter>(), CreditAssessmentContract.View {


    companion object {
        var CREDIT_ASSESSMENT_TITLE_TYPE = "title_type"
        var DEFAULT_TITLE_TYPE = -1
        var MY_DATA_TITLE_TYPE = 1
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    var mData: ArrayList<CreditAssessBean.CreditAssessItemBean>? = null
    var mAdapter: CreditAdapter? = null
    var mGridLayoutManager: GridLayoutManager? = null


    private var isCommit = false//是否使下一个页面变成  提交  而不是 下一步
    private var isAllAuth = false;
    private var isSuccessGetSmsLastTime = false
    private var lastTimeStr: String = ""
    override fun getLayout(): Int = R.layout.activity_credit_assessment

    var authCount = 0
    var notAuthCount = 0

    override fun showProgress() {
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE

    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showError(url: String, msg: String) {
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.GONE
        progress.ll_error.visibility = View.VISIBLE
        progress.ll_error.setOnClickListener {
            initData()
        }
    }

    override fun initView() {
        val titleType = intent.getIntExtra(CREDIT_ASSESSMENT_TITLE_TYPE, DEFAULT_TITLE_TYPE)
        if (titleType == DEFAULT_TITLE_TYPE) {
            tv_credit_assessment_title.text = "认证中心"
        } else if (titleType == MY_DATA_TITLE_TYPE) {
            tv_credit_assessment_title.text = "我的资料"
        }
        StatusBarUtil.setPaddingSmart(mActivity, tb_credit_assessment)
        iv_credit_assessment.setOnClickListener({
            backActivity()
        })
        initRecyclerView()
    }


    private fun initRecyclerView() {
        if (mData == null) {
            mData = ArrayList()
        }
        if (mAdapter == null) {
            mAdapter = CreditAdapter(mActivity, mData)
        }
        if (mGridLayoutManager == null) {
            mGridLayoutManager = GridLayoutManager(mActivity, 4)
            mGridLayoutManager?.spanSizeLookup = SpanSize()

        }

        rv_credit.layoutManager = mGridLayoutManager
        rv_credit.adapter = mAdapter

    }

    //提交认证
    private fun submitResult() {
        if (!isAllAuth) {
            ToastUtil.showToast(mActivity, "基础信息还未认证，请认证")
            return
        }
        if (isSuccessGetSmsLastTime) {
            getUserInfo()
        } else {
            mPresenter.getLastSmsTime(false)
        }
    }

    override fun initData() {
        mPresenter.getLastSmsTime(true)

    }

    override fun onResume() {
        super.onResume()
        mPresenter.getCreditAssessData(false)
    }

    override fun getCreditAssessResult(data: CreditAssessBean?, isNeedJump: Boolean) {
        if (data == null || data.required_list == null) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        var isAuthIdentity = false;
        var isNeedJumpLocal = isNeedJump
        mData?.clear()
        if (data.required_list.size > 0) {
            mData?.add(CreditAssessBean.CreditAssessItemBean(CreditAdapter.ItemType.TYPE_TITLE, "基础信息认证", "认证以下信息让我们对您有个基本认识", true))
            val required_list = data.required_list
            authCount = 0
            notAuthCount = 0
            for (index in required_list.indices) {
                val creditAssessItemBean = required_list[index]
                creditAssessItemBean.local_item_type = CreditAdapter.ItemType.TYPE_REQUIRED
                if (index % 4 == 0) {
                    creditAssessItemBean.local_item_is_required = true
                } else {
                    creditAssessItemBean.local_item_is_required = false
                }
                val item_status = creditAssessItemBean.item_status;
                if ("1" == creditAssessItemBean.item_num && "1".equals(item_status)) {
                    //身份认证通过
                    isAuthIdentity = true
                }
                if ("2" == item_status) {
                    notAuthCount++
                    if (isNeedJumpLocal) {
                        //跳转到指定的认证项 并只有一个跳转
                        mAdapter?.checkToJump(creditAssessItemBean)
                        isNeedJumpLocal = false;
                    }
                }
                if ("1".equals(item_status)) {
                    authCount++
                }

            }
            val size = required_list.size
            isAllAuth = authCount == size
            //是否使下一个页面变成  提交  而不是 下一步
            isCommit = notAuthCount == 0 || notAuthCount == 1

            mData?.addAll(required_list)
            //添加空白item
            val i = 4 - required_list.size % 4
            if (i != 0 && i != 4) {
                for (index in 1..i) {
                    mData?.add(CreditAssessBean.CreditAssessItemBean(CreditAdapter.ItemType.TYPE_REQUIRED, "", "", false))
                }
            }

        }

        var hideData = ArrayList<CreditAssessBean.CreditAssessItemBean>()
        if (data.not_required_list != null && data.not_required_list.size > 0) {

            mData?.add(CreditAssessBean.CreditAssessItemBean(CreditAdapter.ItemType.TYPE_TITLE, "信用认证", "认证以下信息会提高推荐的精准度", false))
            val not_required_list = data.not_required_list
            for (index in not_required_list.indices) {
                val creditAssessItemBean = not_required_list[index]
                creditAssessItemBean.local_item_type = CreditAdapter.ItemType.TYPE_REQUIRED
                if (index % 4 == 0) {
                    creditAssessItemBean.local_item_is_required = true
                } else {
                    creditAssessItemBean.local_item_is_required = false
                }
                mData?.add(not_required_list[index])
            }
            val i = 4 - not_required_list.size % 4
            if (i != 0 && i != 4) {
                for (index in 1..i) {
                    mData?.add(CreditAssessBean.CreditAssessItemBean(CreditAdapter.ItemType.TYPE_REQUIRED, "", "", false))
                }
            }
        }
        mData?.add(CreditAssessBean.CreditAssessItemBean(CreditAdapter.ItemType.TYPE_COMMIT, "", "", true))
        mAdapter?.refreshData(isCommit, hideData, isAuthIdentity)

    }

    override fun processCreditApplyData(t: CreidtApplyBean?) {
        if ("1" == t?.jump_flag) {
            //回到首页
        } else if ("2" == t?.jump_flag) {
            gotoActivity(mActivity, RecommendActivity::class.java, null)
        }
        finish()
    }


    inner class SpanSize : GridLayoutManager.SpanSizeLookup() {

        override fun getSpanSize(position: Int): Int {
            if (mData == null) {
                return 1
            } else {
                if (mData!!.get(position).local_item_type == CreditAdapter.ItemType.TYPE_REQUIRED) {
                    return 1
                } else {
                    return 4
                }

            }

        }
    }

    //刷新认证项   为true时在成功后跳转下一个未认证项
    @Subscribe
    fun onRefreshCreditStatusEvent(event: RefreshCreditStatusEvent) {
//        mPresenter.getCreditAssessData(false)
    }

    override fun processLastSmsTime(t: LastSmsTimeBean?, isFirst: Boolean) {
        if (t == null) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        isSuccessGetSmsLastTime = true
        lastTimeStr = t.message_form_time
        if (!isFirst) {
            getUserInfo()
        }
    }

    private var mJSONObject: JSONObject? = null  // 上传用户信息的json
    private var step = 0   // 获取信息的步数  分别为 app列表 短信
    // 获取用户信息并上传
    fun getUserInfo() {
        //获取用户信息 成功后 提交认证\
        LoadingUtil.showLoadingDialog(this, false)
        mJSONObject = JSONObject()
        PhoneInfoUtil.getApp_list(mActivity, myCallBack)
    }

    private val myCallBack = object : PhoneInfoUtil.PhoneInfoCallback {
        override fun sendMessageToRegister(jsonArray: JSONArray, jsonArrayName: String) {
            try {
                step++
                //获取app列表完毕后 去获取短信
                if (GlobalParams.USER_INFO_APP_LIST.equals(jsonArrayName)) {

                    if (!isFinishing) {
                        PhoneInfoUtil.getMessage_list(mActivity, this, lastTimeStr)
                    }
                }
                mJSONObject?.put(jsonArrayName, jsonArray)
                if (step == 2) {
                    step = 0
                    getUserOtherInfo()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                step = 0
            }

        }
    }

    private fun getUserOtherInfo() {
        val deviceId = UserUtil.getDeviceId(this)
        LoadingUtil.hideLoadingDialog(this)
        //开始上传信息
        mPresenter.submitPhoneInfo(mJSONObject!!, deviceId)
    }

    //上传用户信息成功了  去提交认证
    override fun processSubmitPhoneInfoResult() {

        mPresenter.submitCredit();

    }

    @Subscribe
    fun onPostAssessmentEvent(event: PostAssessmentEvent) {
        submitResult()
    }
}
