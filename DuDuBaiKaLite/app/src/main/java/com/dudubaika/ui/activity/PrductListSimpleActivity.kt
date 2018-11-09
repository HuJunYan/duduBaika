package com.dudubaika.ui.activity

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.PrductNameEvent
import com.dudubaika.model.bean.MsgListBean
import com.dudubaika.model.bean.ProductListSimpleBean
import com.dudubaika.model.bean.SimpleProductBean
import com.dudubaika.presenter.contract.ProductListSimpleContract
import com.dudubaika.presenter.impl.ProductListSimplePresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_prduct_list_simple.*
import kotlinx.android.synthetic.main.dialog_add_product.view.*
import me.drakeet.materialdialog.MaterialDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

/**
 * 下款口子界面
 */
class PrductListSimpleActivity : BaseActivity<ProductListSimplePresenter>(), ProductListSimpleContract.View {

    private var mBean:ProductListSimpleBean?=null
    private var mMsgList:MsgListBean?=null
    private var mList:ArrayList<SimpleProductBean>?=null
    private var mCurrentTag:Boolean = false
    private var mCurrentIndex: Int = 0//当前滚动条的位置

    private val mHandler = object : Handler() {
        override fun handleMessage(message: Message) {
            refreshStaticsRollUI()
        }
    }


    companion object {
         //当前是否来自首页
        var CURRENTTAG:String="currentTag"
        private val MSG_SHOW_TEXT = 1
        private val SHOW_TEXT_TIME = 5 * 1000
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() =Unit

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int = R.layout.activity_prduct_list_simple

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb)
        defaultTitle="下款口子"
        initTextSwitcher()
        mCurrentTag  =intent.getBooleanExtra(CURRENTTAG,false)
        iv_return.setOnClickListener {
            backActivity()
        }
        search.setOnClickListener {
            startActivity<SearchActivity>(SearchActivity.CURRENTTAG to mCurrentTag)
        }
        tv_add.setOnClickListener {
            showDialogAdd()
        }
        if (mCurrentTag){
            tv_add.visibility = View.GONE
        }else{
            tv_add.visibility = View.VISIBLE
        }
    }

    override fun initData() {
        mList = ArrayList()

        mPresenter.getData()
        mPresenter.getMsgListData()
    }

    /**
     * 初始化TextSwitcher
     */
    private fun initTextSwitcher() {

        main_top_txt.setFactory({
            val tv = TextView(mActivity)
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
        if (null!=mMsgList &&  null != mMsgList?.msg_list && mMsgList!!.msg_list.size>0) {
            when (mCurrentIndex) {
                0 -> //第一次不执行动画，立刻显示
                    main_top_txt.setCurrentText(mMsgList!!.msg_list[mCurrentIndex])
                mMsgList!!.msg_list.size -> {
                    //跳回第一次
                    mCurrentIndex = 0
                    main_top_txt.setText(mMsgList!!.msg_list[mCurrentIndex])
                }
                else -> //执行动画
                    main_top_txt.setText(mMsgList!!.msg_list[mCurrentIndex])
            }
            mCurrentIndex++
            mHandler.removeMessages(MSG_SHOW_TEXT)
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_TEXT, SHOW_TEXT_TIME.toLong())
        }


    }


    override fun showData(data: ProductListSimpleBean?) {
        if (null==data){
            return
        }
        mBean =data
        if (mBean!!.name_list.size>0) {
            for (item in mBean!!.name_list) {
                   for (it in item.list){
                       mList?.add(SimpleProductBean(it.product_id,it.product_name,it.product_logo))
                   }
            }
        }

        showListData()

    }

    override fun showMsgList(data: MsgListBean) {
        //快讯消息数据源
        if (null==data){
            return
        }
        mMsgList = data
        refreshStaticsRollUI()
    }


    //展示列表数据
    private fun showListData() {
        //我的记账条目数据
        listview.adapter = object : CommonAdapter<SimpleProductBean>(mActivity, mList, R.layout.item_simple_product) {
            override fun convert(holer: ViewHolder?, item: SimpleProductBean?, posotion: Int) {

                holer?.setOnClickListener(R.id.content, {
                    if (!mCurrentTag) {
                        EventBus.getDefault().post(PrductNameEvent(item!!.productName, "", item!!.productId))
                        mActivity.finish()
                    }else{
                        startActivity<ProductInfoActivity>(ProductInfoActivity.PRODUCT_ID to item!!.productId)
                    }

                })

                ImageUtil.loadWithCache(mActivity,holer!!.getView<CircleImageView>(R.id.p_logo),item!!.productLogo,R.drawable.product_logo_default)
                holer.setText(R.id.p_name, item?.productName)

            }
        }

    }

    private fun showDialogAdd() {

        val mDialog = Dialog(this,R.style.MyDialog)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        contentView.dialog_confirm.setOnClickListener {
            //确定

            val productname = contentView.p_name.text.toString().trim()
            if (TextUtils.isEmpty(productname)) {
               ToastUtil.showToast(mActivity,"请输入下款口子")
                return@setOnClickListener
            }
            //机构名称
            EventBus.getDefault().post(PrductNameEvent(productname,"",""))
            mDialog.dismiss()
            backActivity()
        }
        contentView.dialog_cancle.setOnClickListener {
            //确定
            mDialog.dismiss()
        }

        mDialog?.setContentView(contentView)
        mDialog?.setCancelable(true)
        mDialog?.show()
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }


    @Subscribe
    public fun OnPrductNameEvent(event: PrductNameEvent){
        if ("2".equals(event.isFinish)){
            //搜索界面请求关掉当前界面
            backActivity()
        }
    }

}
