package com.dudubaika.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.TextView
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseFragment
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.*
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.*

import com.dudubaika.presenter.contract.HomeContract
import com.dudubaika.presenter.impl.HomePresenter
import com.dudubaika.ui.activity.*
import com.dudubaika.ui.adapter.FragAdapter
import kotlinx.android.synthetic.main.fragment_home2.*
import kotlinx.android.synthetic.main.item_home2_top_banner.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.support.v4.startActivity
import com.dudubaika.util.*
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.dialog_home_bottom_product.*
import kotlinx.android.synthetic.main.dialog_home_bottom_product.view.*
import kotlinx.android.synthetic.main.dialog_home_message.view.*
import kotlinx.android.synthetic.main.dialog_home_product.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * 有时间重构下（需求太多，没时间搞）
 */

class HomeFragment2 : BaseFragment<HomePresenter>(), HomeContract.View {

    private var mTopBean:HomeTopInfo?=null
    private var mAuthBean:AuthStatus?=null
    private var mBottomBean:HomeButtomDialogBean?=null
    private var hotProductFragment:HotProductFragment?=null
    private var rapidFragment:RapidFragment?=null
    private var threeFragment:ThreeFragment?=null
    private var fourFragment:FourFragment?=null
    private var fiveFragment:FiveFragment?=null
    private var sixFragment:SixFragment?=null
    private var pagerAdapter: FragAdapter? =null
    private var listFragment: ArrayList<Fragment>? =null
    private var mTitleList: ArrayList<String> = ArrayList()
    private var mCurrentIndex: Int = 0//当前滚动条的位置
    private var type:String="1"//当前选中的产品类型 默认是热门推荐
    private var isRefresh:Boolean=true//当前整个界面刷新是否可用
    private var isDown:Boolean=false//当前产品列表是否可以下滑
    private var isStart:Boolean= true
    private var isRefreshAction:Boolean= false
    //当前标题个数 默认为1
    private var currentCount:Int= 1
//    private var returnTop:String= "1"
    //回到顶部是否显示
    private var isShowGoTop:Boolean= false
    private var mProductDialog : Dialog?=null
    private var mMessageDialog : Dialog?=null
    private var mDialog:HomeDialogForUser?=null

    private val mHandler = object : Handler() {
        override fun handleMessage(message: Message) {
            refreshStaticsRollUI()
        }
    }
    companion object {
        const val HOME_AUTO_JUMP = "isAutoJump"
        private val MSG_SHOW_TEXT = 1
        private val SHOW_TEXT_TIME = 5 * 1000
    }
    override fun getLayout(): Int = R.layout.fragment_home2


    override fun initView() {
        StatusBarUtil.setPaddingSmart(activity, tb_home2)
        defaultTitle="首页"
        isStart = false
        initTextSwitcher()
        isShowGoTop(isShowGoTop)

        find.setOnClickListener {
            //筛选
        }



        refresh.setColorSchemeResources(R.color.red_home)

       // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        refresh.setOnRefreshListener {
            /*if (search_key.visibility==View.GONE){
                search_key.visibility= View.VISIBLE
            }
            isRefreshAction = true
            // 开始刷新，设置当前为刷新状态，经测试这里可以省略
            refresh.isRefreshing = true
            refresh.isEnabled= false
            mPresenter.getBannerInfo(false)
            EventBus.getDefault().post(HomeRefreshEvent(type))
            if (UserUtil.isLogin(mContext!!)) {
                mPresenter.getAuthStatush(false)
            }
            mPresenter.getBottomInfo(false)*/

            mPresenter.test()

        }

        return_top.setOnClickListener {
            EventBus.getDefault().postSticky(HomeReturnTop(type))
        }

        p_name_key.setOnClickListener {
        }

        bottom.bottom_dissmiss.setOnClickListener {
            bottom.visibility =View.GONE
            UserUtil.saveIsStartApp(mContext!!,false)
        }
        bottom.setOnClickListener {
            if (!TextUtils.isEmpty(mBottomBean!!.product_list[0].product_id)) {
            }
        }
    }



    override fun initData() {

        mPresenter.getDialogForUser()
        if (listFragment==null){
            listFragment = ArrayList()
        }

        mPresenter.getBannerInfo(false)
        //得到用户认证状态
        if (UserUtil.isLogin(mContext!!)) {
            mPresenter.getAuthStatush(false)
        }
        //得到底部数据
        mPresenter.getBottomInfo(true)
    }

    override fun onResume() {
        super.onResume()
        refresh.isEnabled = isRefresh && search_key.visibility==View.VISIBLE
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress()  = Unit

    override fun hideProgress() {
        refresh.isEnabled= true
        refresh.isRefreshing = false
    }

    override fun showError(url: String, msg: String) {
        refresh.isEnabled= true
        refresh.isRefreshing = false

    }



    //首页推荐位信息展示
    override fun showBannerInfo(data: HomeTopInfo) {
        refresh.isEnabled = isRefresh && search_key.visibility==View.VISIBLE
        refresh.isRefreshing = false

        if (null==data){
            return
        }
        mTopBean =data

        if (null!=mTopBean?.title_list && mTopBean!!.title_list.size>1 ) {
//            mTopBean!!.title_list.add(HomeTopInfo.TitleListBean("5","测试"))
            for (item in mTopBean!!.title_list){
                mTitleList.add(item.title_name)
            }
            when(mTopBean!!.title_list.size){//
                1->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[0].title_id)
                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                }
                2->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[0].title_id)
                    rapidFragment = RapidFragment(mTopBean!!.title_list[1].title_id)
                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                    listFragment?.add(rapidFragment!!)

                }
                3->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[0].title_id)
                    rapidFragment = RapidFragment(mTopBean!!.title_list[1].title_id)
                    threeFragment = ThreeFragment(mTopBean!!.title_list[2].title_id)

                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                    listFragment?.add(rapidFragment!!)
                    listFragment?.add(threeFragment!!)

                }

                4->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[0].title_id)
                    rapidFragment = RapidFragment(mTopBean!!.title_list[1].title_id)
                    threeFragment = ThreeFragment(mTopBean!!.title_list[2].title_id)
                    fourFragment = FourFragment(mTopBean!!.title_list[3].title_id)

                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                    listFragment?.add(rapidFragment!!)
                    listFragment?.add(threeFragment!!)
                    listFragment?.add(fourFragment!!)

                }
                5->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[0].title_id)
                    rapidFragment = RapidFragment(mTopBean!!.title_list[1].title_id)
                    threeFragment = ThreeFragment(mTopBean!!.title_list[2].title_id)
                    fourFragment = FourFragment(mTopBean!!.title_list[3].title_id)
                    fiveFragment = FiveFragment(mTopBean!!.title_list[4].title_id)
                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                    listFragment?.add(rapidFragment!!)
                    listFragment?.add(threeFragment!!)
                    listFragment?.add(fourFragment!!)
                    listFragment?.add(fiveFragment!!)

                }
                6->{
                    hotProductFragment = HotProductFragment(mTopBean!!.title_list[5].title_id)
                    rapidFragment = RapidFragment(mTopBean!!.title_list[1].title_id)
                    threeFragment = ThreeFragment(mTopBean!!.title_list[2].title_id)
                    fourFragment = FourFragment(mTopBean!!.title_list[3].title_id)
                    fiveFragment = FiveFragment(mTopBean!!.title_list[4].title_id)
                    sixFragment = SixFragment(mTopBean!!.title_list[5].title_id)
                    listFragment?.clear()
                    listFragment?.add(hotProductFragment!!)
                    listFragment?.add(rapidFragment!!)
                    listFragment?.add(threeFragment!!)
                    listFragment?.add(fourFragment!!)
                    listFragment?.add(fiveFragment!!)
                    listFragment?.add(sixFragment!!)

                }

            }

        }


        pagerAdapter = FragAdapter(childFragmentManager,listFragment,mTitleList)
        id_stickynavlayout_viewpager.adapter  =pagerAdapter
        tabLayout.setupWithViewPager(id_stickynavlayout_viewpager)
        id_stickynavlayout_viewpager.currentItem = 0




        showTopInfo2()

        refreshStaticsRollUI()
    }

    //底部弹窗数据
    override fun showHomeBottomData(data: HomeButtomDialogBean) {

        if (null==data){
            return
        }
        mBottomBean = data
        //描述是否可见
        if ("1" == data.desc_is_show){
            home_bottom_desc.visibility  =View.VISIBLE
            home_bottom_desc.text = data.product_desc
        }else{
            home_bottom_desc.visibility  =View.INVISIBLE
        }
        //弹窗是否可见

        if ("1".equals(data.list_is_show) && UserUtil.getIsStartApp(mContext!!)){
            bottom.visibility =  View.VISIBLE
            showBottomData()
        }else{
            bottom.visibility = View.GONE
        }

    }

    override fun showDialogForUser(data: HomeDialogForUser) {

        if (null==data){
            return
        }
        mDialog = data
        showHomeDialogForUser(data)

    }

    //显示弹窗
    private fun showHomeDialogForUser(data: HomeDialogForUser) {
        if ("1".equals(data.is_product_dialog)){
            showProductDialog(data)
        }else{
            showMessageDialog(data)
        }
    }

     internal var timer: CountDownTimer = object : CountDownTimer(1000,1000) {
        override fun onFinish() {
            showMessageDialog(mDialog!!)

        }

        override fun onTick(millisUntilFinished: Long) {
        }

    }

    private fun showMessageDialog(data: HomeDialogForUser) {

        if ("1".equals(data.is_message_dialog)) {
            val message = data.message_dialog
            mMessageDialog = Dialog(mContext, R.style.MyDialog)
            val layoutInflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.dialog_home_message, null)

            view.tv_message_title.text = message.message_title
            view.tv_message_time.text = message.message_time
            view.content.text = message.message_desc

            view.rl_message.setOnClickListener {
                when (message.message_type) {
                    "1" -> {
                        //链接链接
                        startActivity<WebActivity>(WebActivity.WEB_URL_TITLE to message.message_title,
                                WebActivity.WEB_URL_KEY to message.message_url)
                    }
                    "2" -> {
                        //原生
                    }
                }
                EventBus.getDefault().postSticky(SetMsgIsRead(message.message_id))
                mMessageDialog?.dismiss()
            }

            view.dialog_message_cancle.setOnClickListener({
                //取消
                mMessageDialog?.dismiss()
            })
            mMessageDialog?.setContentView(view)
            mMessageDialog?.setCancelable(false)
            mMessageDialog?.show()
            if (null !=  timer){
                timer.cancel()
            }
        }
    }

    private fun showProductDialog(data: HomeDialogForUser) {
        mProductDialog = Dialog(mContext,R.style.MyDialog)
        val layoutInflater = mContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_home_product, null)

        view.dialog_product_desc.text = data.product_dialog.product_desc
        view.product_name.text = data.product_dialog.product_name
        ImageUtil.loadNoCache(mContext!!,view.iv_logo,data.product_dialog.product_logo,R.drawable.default_bank_logo)

        view.rl_product.setOnClickListener {

            val product = data.product_dialog
            mProductDialog?.dismiss()
            timer.start()
//            showMessageDialog(data)
            /* Timer().schedule(object :TimerTask() {
                 override fun run() {
                     val message =  Message()
                     message.obj = data
                     handler.sendMessage(message)
                 }
             },0,2000)*/

        }

        view.dialog_product_cancle.setOnClickListener({
            //取消
            mProductDialog?.dismiss()
            showMessageDialog(data)
        })
        mProductDialog?.setContentView(view)
        mProductDialog?.setCancelable(false)
        mProductDialog?.show()
    }


    // 展示底部数据
    private fun showBottomData() {


//        dialog_profile_image
        if(null != mBottomBean?.product_list && mBottomBean!!.product_list.size>0) {

            val item = mBottomBean!!.product_list[0]

            ImageUtil.loadNoCache(mContext!!, dialog_profile_image, item!!.product_logo, R.drawable.product_logo_default)
            dialog_p_name.text = item.product_name
            //额度
            dialog_ed_money_value1.text = item.quota_start_value
            dialog_ed_money_value2.text = ""
            dialog_ed_money_value3.text = "-" + item.quota_end_value
            dialog_ed_money_value4.text = " " + item.quota_end_unit
            dialog_ed.text = item.quota_name

            //下款时长
            dialog_yll_value1.text = item.loan_time_value
            dialog_yll_value2.text = " " + item.loan_time_unit
            dialog_yll.text = item.loan_name

            //利率
            dialog_qx_time_value3.text = item.rate_value
            dialog_qx.text = item.rate_unit + item.rate_name

            hot.text = item.product_tag

            bottom.setOnClickListener {
            }



        }
    }

    //banner
    private fun showTopInfo2() {
        val views = ArrayList<View>()
        if (null !=mTopBean?.recommend_list && mTopBean!!.recommend_list.isNotEmpty()) {
            for (item in mTopBean!!.recommend_list) {
                var viewItem = View.inflate(context, R.layout.item_home2_top_banner, null)
                showBannerView(viewItem, item)
                views.add(viewItem!!)
            }
            banner_guide_content.setData(views)
//            banner_guide_content.setAutoPlayAble(true)
        }else{
            ToastUtil.showToast(mContext,"没有推荐位数据")
            return
        }

        banner_guide_content.setDelegate( { banner, _, _, position ->
        })
    }


    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 初始化TextSwitcher
     */
    private fun initTextSwitcher() {

        main_top_txt.setFactory({
            val tv = TextView(_mActivity)
            tv.textSize = 12f
            tv.setTextColor(resources.getColor(R.color.home_zx_text))
            tv.setSingleLine()
            tv.setPadding(20,20,20,20)
            tv.setBackgroundResource(R.drawable.shape_zx_bg)
            tv.ellipsize = TextUtils.TruncateAt.END
            val drawable = resources.getDrawable(R.drawable.news)
            drawable.setBounds(0, 0, (drawable.minimumWidth*0.8).toInt(), (drawable.minimumHeight*0.8).toInt())
            tv.setCompoundDrawables(drawable, null, null, null)
            tv.compoundDrawablePadding = 30//设置图片和text之间的间距
            val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            lp.setMargins(15,0,0,0)
            lp.gravity =  Gravity.CENTER_HORIZONTAL
            tv.layoutParams = lp
            tv
        })
    }

    /**
     * 刷新滚动条
     */
    private fun refreshStaticsRollUI() {
        if (null!=mTopBean && null!= mTopBean?.recommend_list && mTopBean!!.recommend_list?.isNotEmpty()) {
            when (mCurrentIndex) {
                0 -> //第一次不执行动画，立刻显示
                    main_top_txt.setCurrentText(mTopBean!!.msg_list[mCurrentIndex])
                mTopBean!!.msg_list.size -> {
                    //跳回第一次
                    mCurrentIndex = 0
                    main_top_txt.setText(mTopBean!!.msg_list[mCurrentIndex])
                }
                else -> //执行动画
                    main_top_txt.setText(mTopBean!!.msg_list[mCurrentIndex])
            }
            mCurrentIndex++
            mHandler.removeMessages(MSG_SHOW_TEXT)
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_TEXT, SHOW_TEXT_TIME.toLong())
        }


    }

    @Subscribe
    public fun addPoint(event: PointEvent){
        if (!TextUtils.isEmpty(event.tag)) {
            //没有网络请求的界面来此添加埋点
            mPresenter.dian(event.tag, event.id)
        }

    }

    @Subscribe
    public fun onIsRefreshUsedEvent(event:IsRefreshUsedEvent){

        if (search_key.visibility==View.GONE){

            search_key.visibility = View.VISIBLE
            homeTopAnimation(search_key)
            return
        }

        if (event.isUsed && !isDown) {
            //刷新可用
            isRefresh = true
           LogUtil.i("StickyNavLayout","收到刷新可用消息")
        }else{
            isRefresh = false
            LogUtil.i("StickyNavLayout","收到刷新不可用消息")
        }
        refresh.isEnabled = isRefresh

    }

    @Subscribe
    public fun onHomeListIsDown(event:HomeListIsDown){
        isDown = event.isDown
        if (type.equals(event.posotion)){
            isShowGoTop(event.isDown)
        }
    }



    private fun showBannerView(view:View, topItem:HomeTopInfo.RecommendList){
        view.current_month.text = topItem.recommend_title
        view.money_app_name.text = topItem.recommend_name
        ImageUtil.loadNoCache(mContext!!,view.product_logo,topItem.recommend_logo, R.drawable.ic_error_close)
        view.ed_money_value1.text = topItem.quota_start_value
        view.ed_money_value2.text =""// topItem.quota_start_unit
        view.ed_money_value3.text = "-" + topItem.quota_end_value
        view.ed_money_value4.text =  topItem.quota_end_unit
        view.ed.text  =topItem.quota_name

        view.yll_value1.text =  topItem.rate_value
        view.yll_value2.text =  "%"
        view.yll.text =  topItem.rate_unit+topItem.rate_name

        view.qx_time_value1.text =  topItem.term_start_value
        view.qx_time_value2.text =  topItem.term_start_unit
        view.qx_time_value3.text =  "-"+topItem.term_end_value
        view.qx_time_value4.text =  topItem.term_end_unit
        view.get_money.text =  topItem.recommend_button_name
        view.cuurent_money_last.text =  topItem.recommend_des
        view.qx.text =  topItem.term_name
    }

   //用户认证状态
    override fun showStatus(data: AuthStatus) {


       isStart =true
       isRefreshAction = false
    }



    @Subscribe
    public fun OngetUserAuthStatus(event: getUserAuthStatus) {
        //查询用户的认证状态 到了哪一步
        mPresenter.getAuthStatush(false)
    }

    private fun goWhere() {
        when (mAuthBean?.auth_status) {
            "1", "2", "3", "4" -> {
            }
            "5"->{
                gotoMoXieActivity()
            }
            "6"->{
                ToastUtil.showToast(mContext!!, "您的额度正在估算中")
            }
            "7"->{
            }
            "8" -> {
                mPresenter.gotoAuthFK()
                ToastUtil.showToast(mContext!!, "您的额度正在估算中")
            }
            else -> {
                //nothing
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
        mxParam.userId = UserUtil.getUserId(mContext!!) + "-" + GlobalParams.PLATFORM_FLAG
        mxParam.apiKey = apiKey

        mxParam.taskType = MxParam.PARAM_TASK_TAOBAO
        MoxieSDK.getInstance().start(_mActivity, mxParam, object : MoxieCallBack() {
            override fun callback(moxieContext: MoxieContext?, moxieCallBackData: MoxieCallBackData?): Boolean {
                if (moxieCallBackData != null) {
                    when (moxieCallBackData.code) {
                        MxParam.ResultCode.IMPORTING, MxParam.ResultCode.IMPORT_UNSTART -> {
                        }
                        MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR, MxParam.ResultCode.MOXIE_SERVER_ERROR, MxParam.ResultCode.USER_INPUT_ERROR, MxParam.ResultCode.IMPORT_FAIL -> {
                            ToastUtil.showToast(_mActivity, "认证失败!")
                            moxieContext!!.finish()
                        }
                        MxParam.ResultCode.IMPORT_SUCCESS ->{
                            //刷新接口
                            mPresenter.changeMoxieStatus()
                            Thread.sleep(3000)
                            ToastUtil.showToast(_mActivity, "认证成功")
                            mPresenter.getAuthStatush(true)
                            moxieContext!!.finish()
                        }



                    }

                }
                return false
            }
        })

    }


    private var mJSONObject: JSONObject? = null  // 上传用户信息的json
    private var step = 0   // 获取信息的步数  分别为 app列表 短信
    // 获取用户信息并上传
    fun getUserInfo() {
        //获取用户信息 成功后 提交认证\
        LoadingUtil.showLoadingDialog(this, false)
        mJSONObject = JSONObject()
        PhoneInfoUtil.getApp_list(_mActivity, myCallBack)
    }

    private val myCallBack = object : PhoneInfoUtil.PhoneInfoCallback {
        override fun sendMessageToRegister(jsonArray: JSONArray, jsonArrayName: String) {
            try {
                step++
                //获取app列表完毕后 去获取短信
                if (GlobalParams.USER_INFO_APP_LIST.equals(jsonArrayName)) {

                    PhoneInfoUtil.getMessage_list(_mActivity, this, "")
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
        val deviceId = UserUtil.getDeviceId(_mActivity)
        //开始上传信息
        mPresenter.submitPhoneInfo2(mJSONObject!!, deviceId)
        //保存上传标识用户
        UserUtil.savePhoneInfo(mContext!!,true)
    }


    //是否显示回到顶部按钮按钮
    private fun isShowGoTop(tag:Boolean){

        if (tag){
            return_top.visibility = View.VISIBLE
        }else{
            return_top.visibility = View.GONE
        }
    }


    @Subscribe
    private fun OnRefreshHomeUserMoney(event: RefreshHomeUserMoney){
        //刷新首页额度
        mPresenter.getBannerInfo(false)
    }

    @Subscribe
    private fun OnIsShowGoTopEvent(event: IsShowGoTopEvent){
        //回到顶部按钮是否隐藏
       if (type.equals(event.posotion) ){
           isShowGoTop(!event.istop)
       }
    }

    private fun homeTopAnimation(view: View){

        val animation =  AlphaAnimation(0.4f,1.0f)
        animation.duration = 1000
        view.startAnimation(animation)
    }


//    @Subscribe
//    public fun getRecordStopAppEvent(event: RecordStopAppEvent) {
//        mPresenter.stopAppTime2()
//    }

    private fun recordTalkData(int: Int){
        TCAgent.onEvent(mContext, TalkingDataParams.HOME_TAB, mTopBean!!.title_list[int].title_id)
    }

}




