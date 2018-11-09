package com.dudubaika.ui.activity

import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.onekeyshare.OnekeyShare
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.AuthStatus
import com.dudubaika.model.bean.NowApplyBean
import com.dudubaika.model.bean.ProductInfoBean
import com.dudubaika.presenter.contract.BuyDetailContract
import com.dudubaika.presenter.impl.BuyDetailPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.util.*
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_product_info.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import java.util.HashMap
import java.util.logging.Handler


/**
 * 产品详情页
 */
class ProductInfoActivity : BaseActivity<BuyDetailPresenter>(), BuyDetailContract.View  {

    private var mAuthBean:AuthStatus?=null
    private var product_id:String? =null
    private var title:String? =null
    private var mProductType:String? =null
    private var mBean:ProductInfoBean? =null
    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()

    companion object {
        public var PRODUCT_ID:String=  "product_id"
        public var TILTLE:String=  "tiltle"
        public var PRODUCT_TYPE:String=  "product_type"
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int = R.layout.activity_product_info

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_product_info)
        defaultTitle="产品详情"
        iv_regist_return.setOnClickListener {
            backActivity()
        }
        now_pay.setOnClickListener {
            if (UserUtil.isLogin(mActivity)) {
                 //不是本公司产品 直接跳转h5
                if (!"1".equals(mBean?.is_personal)){
                    mPresenter.nowApply(product_id!!)
                }else{
                    //是本公司产品
                 mPresenter.getAuthStatush(true)
                }
            }else{
                startActivity<LoginActivity>(LoginActivity.ISTOGODETAIL to true)
            }

            val kv =  hashMapOf<String,String>()
            kv.put("productId", product_id!!)
            kv.put("productId",product_id!! )
            TCAgent.onEvent(mActivity, TalkingDataParams.APPLY_NOW, mProductType, kv)

        }

        tv_share.setOnClickListener {
            showShare()

        }


    }

    private fun  showShare() {
        val oks =  OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(mBean?.share_title)
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(mBean?.share_url)
        // text是分享文本，所有平台都需要这个字段
        oks.text = mBean?.share_des
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(mBean?.share_img)//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(mBean?.share_url)


        oks.callback= object :PlatformActionListener{

            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
            }

            override fun onCancel(p0: Platform?, p1: Int) {
            }

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                LogUtil.i("shareshare",p2?.message)
            }
        }

        // 启动分享GUI
        oks.show(this)
    }


    override fun initData() {
        product_id = intent.getStringExtra(PRODUCT_ID)
        mProductType = intent.getStringExtra(PRODUCT_TYPE)
        tv_home_title.text = "产品详情"
        title = intent.getStringExtra(TILTLE)
        if (null!=product_id) {
            if (TextUtils.isEmpty(mProductType)){
                mProductType=""
            }
            mPresenter.getProductDetailData(product_id!!,mProductType!!)
        }


        val kv =  hashMapOf<String,String>()
        kv.put("productId", product_id!!)
        kv.put("productId",product_id!! )
        TCAgent.onEvent(mActivity, TalkingDataParams.LOAN_DETIAL, mProductType, kv)


    }

    //加载界面数据
    override fun processProductDetailData(data: ProductInfoBean?) {
        if (null==data){
            return
        }
        mBean =data
        showView()
    }

    private fun showView() {

        p_name.text = mBean?.product_name
        ImageUtil.loadNoCache(mActivity,app_logo,mBean!!.product_logo_url,R.drawable.ic_error_close)
        ed_fw.text = mBean?.quota_start_value+mBean?.quota_start_unit+"-"+mBean?.quota_end_value+mBean?.quota_end_unit
        have_fk.text = "今日已放款 "+mBean?.apply_count+" 人"
        //利率
        info_yll_value.text = mBean?.rate_value+"%"
        info_yll_key.text = mBean?.rate_unit+mBean?.rate_name

        //期限
        if (!TextUtils.isEmpty(mBean?.term_end_value)) {
            info_qx_value.text = mBean?.term_start_value + mBean?.term_start_unit + "-" + mBean?.term_end_value + mBean?.term_end_unit
        }else{
            info_qx_value.text = mBean?.term_start_value + mBean?.term_start_unit
        }
        info_qx_key.text = mBean?.term_name


        //时长
        info_xk_value.text = mBean?.loan_time_value+mBean?.loan_time_unit
        info_xk_key.text = mBean?.loan_name
        //申请条件
        sqtj.text = mBean?.product_apply_limit_name

        sqtj_value.text =  mBean?.product_apply_limit_str?.replace("\\n","\n")
        //认证资料
        rzzl_title.text = mBean?.product_credit_name
        if (null!=mBean?.product_credit_list && mBean!!.product_credit_list.size>0){
            initRecycleView()
        }else{
            rzzl_title.visibility =View.GONE
            recyclerView.visibility =View.GONE
        }

        //还款方式
        hkfs.text = mBean?.repay_type_name
        hk_way_value.text = mBean?.repay_type_des

        now_pay.text = mBean?.apply_button_name


    }

    override fun showStatus(data: AuthStatus) {
        //得到用的认证状态 以及是否开启认证
        if(null==data){
            return
        }
        mAuthBean = data
        goWhere()

    }

    override fun processNowApplyData(data: NowApplyBean?) {


        //到h5界面
        if ( null!=data?.jump_url) {
            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to data!!.jump_url,WebActivity.WEB_URL_TITLE to mBean!!.product_name)
        }
    }


    private fun initRecycleView() {

        mAdapters.clear()
        //初始化
        //创建VirtualLayoutManager对象
        val layoutManager = VirtualLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = layoutManager

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        recyclerView.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        val delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView.adapter = delegateAdapter

        //热门数据
        val infotAdapter = initInfoAdapter()
        mAdapters.add(infotAdapter)
        delegateAdapter.setAdapters(mAdapters)


    }

    /**
     * 认证资料item
     */
    fun initInfoAdapter(): BaseDelegateAdapter {


        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(),R.layout.item_image, mBean!!.product_credit_list.size, GlobalParams.VLAYOUT_HOTLIST) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)

                val iv = holder.getView<ImageView>(R.id.iv_info)
                ImageUtil.loadNoCache(mActivity,iv,mBean!!.product_credit_list.get(position).logo_url,R.drawable.ic_error_close)

                if (position!=0){
                    val params = iv.layoutParams
                    val lp = LinearLayout.LayoutParams(params)
                    lp.setMargins(20,0,0,0)
                    iv.layoutParams = lp
                }
            }
        }

    }

    private fun goWhere() {

        if ("2".equals(mAuthBean?.is_auth)){
            mPresenter.nowApply(product_id!!)
        }else {
            when (mAuthBean?.auth_status) {
                "1", "2", "3", "4" -> {
                    AutherUtils.getInstance().StartActivity(mActivity, mAuthBean?.auth_status, mAuthBean?.mobile_url)
                }
                "5" -> {
                    gotoMoXieActivity()
                }
                "6" -> {
                    ToastUtil.showToast(mActivity, "您的额度正在评测中")
                }
                "7" -> {
                    mPresenter.nowApply(product_id!!)
                }
                "8" -> {
                    mPresenter.gotoAuthFK()
                }
                else -> {
                    //nothing
                }
            }
        }
    }

    private fun gotoMoXieActivity() {

        var apiKey = ""
        when (App.instance.mCurrentHost) {
        //测试key
            App.HOST.DEV -> apiKey = GlobalParams.MOXIE_DEV_KEY
        //测试key
            App.HOST.PRE -> apiKey = GlobalParams.MOXIE_PRO_KEY
        //正式key
            App.HOST.PRO -> apiKey = GlobalParams.MOXIE_PRO_KEY
            else -> apiKey = GlobalParams.MOXIE_DEV_KEY
        }

        val mxParam = MxParam()
        mxParam.userId = UserUtil.getUserId(mActivity!!) + "-" + GlobalParams.PLATFORM_FLAG
        mxParam.apiKey = apiKey

        mxParam.taskType = MxParam.PARAM_TASK_TAOBAO
        MoxieSDK.getInstance().start(mActivity, mxParam, object : MoxieCallBack() {
            override fun callback(moxieContext: MoxieContext?, moxieCallBackData: MoxieCallBackData?): Boolean {
                if (moxieCallBackData != null) {
                    when (moxieCallBackData.code) {
                        MxParam.ResultCode.IMPORTING, MxParam.ResultCode.IMPORT_UNSTART -> {
                        }
                        MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR, MxParam.ResultCode.MOXIE_SERVER_ERROR, MxParam.ResultCode.USER_INPUT_ERROR, MxParam.ResultCode.IMPORT_FAIL -> {
                            ToastUtil.showToast(mActivity, "认证失败!")
                            moxieContext!!.finish()
                        }
                        MxParam.ResultCode.IMPORT_SUCCESS ->{
                            //刷新接口
                            mPresenter.changeMoxieStatus()
                            mPresenter.getAuthStatush(true)
                            moxieContext!!.finish()
                        }



                    }

                }
                return false
            }
        })

    }
}
