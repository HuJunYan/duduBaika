package com.dudubaika.ui.fragment


import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bingoogolapple.bgabanner.BGABanner
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.model.bean.HomeCreditCardBean
import com.dudubaika.presenter.contract.CreditCardContract
import com.dudubaika.presenter.impl.CreditCardPresenter
import com.dudubaika.ui.adapter.HomeCardBankListAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.item_home_card_foot_view.view.*
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.ui.activity.BankListActivity
import com.dudubaika.ui.activity.CreditCardDetailActivity
import com.dudubaika.ui.activity.WebVerifyActivity
import com.dudubaika.ui.adapter.HomeCardListAdapter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.ToastUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.item_home_card_top_banner.view.*
import kotlinx.android.synthetic.main.view_home_card_layout.*
import org.jetbrains.anko.support.v4.startActivity
import kotlin.collections.ArrayList


/**
 *信用卡fragment
 */
class CardFragment : BaseFragment<CreditCardPresenter>(),CreditCardContract.View {
    private var mRelativeLayout:ArrayList<RelativeLayout>?=null
    private var mImageView:ArrayList<ImageView>?=null
    private var mTitletxt:ArrayList<TextView>?=null
    private var mDesctxt:ArrayList<TextView>?=null
    private var mBankListAdapter:HomeCardBankListAdapter?=null
    //功能卡专区
    private var mgoodAdapter:HomeCardListAdapter?=null
    private var mBanList: List<HomeCreditCardBean.BankListBean>? = null
    private var  mCardList:List<HomeCreditCardBean.QualityListBean>?= null
    private var mBean:HomeCreditCardBean?=null
    private var isOpen:Boolean = false
    override fun getLayout(): Int = R.layout.fragment_card


    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
        if (null!=swirefresh) {
            swirefresh.finishRefresh()
        }
    }

    override fun showError(url: String, msg: String) {

        if (null!=swirefresh) {
            swirefresh.finishRefresh()
        }

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mContext,tb_card_fg)
        defaultTitle="信用卡首页"
        mBanList = ArrayList()
        mCardList = ArrayList()
        mTitletxt = ArrayList()
        mImageView = ArrayList()
        mDesctxt = ArrayList()
        mRelativeLayout = ArrayList()

        mImageView?.add(iv1)
        mImageView?.add(iv2)
        mImageView?.add(iv3)
        mImageView?.add(iv4)
        mImageView?.add(iv5)
        mImageView?.add(iv6)
        mImageView?.add(iv7)
        mImageView?.add(iv8)
        mImageView?.add(iv9)
        mImageView?.add(iv10)

        mTitletxt?.add(t1)
        mTitletxt?.add(t2)
        mTitletxt?.add(t3)
        mTitletxt?.add(t4)
        mTitletxt?.add(t5)
        mTitletxt?.add(t6)
        mTitletxt?.add(t7)
        mTitletxt?.add(t8)
        mTitletxt?.add(t9)
        mTitletxt?.add(t10)

        mDesctxt?.add(des1)
        mDesctxt?.add(des2)
        mDesctxt?.add(des3)
        mDesctxt?.add(des4)
        mDesctxt?.add(des5)
        mDesctxt?.add(des6)
        mDesctxt?.add(des7)
        mDesctxt?.add(des8)
        mDesctxt?.add(des9)
        mDesctxt?.add(des10)

        mRelativeLayout?.add(layout1)
        mRelativeLayout?.add(layout2)
        mRelativeLayout?.add(layout3)
        mRelativeLayout?.add(layout4)
        mRelativeLayout?.add(layout5)
        mRelativeLayout?.add(layout6)
        mRelativeLayout?.add(layout7)
        mRelativeLayout?.add(layout8)
        mRelativeLayout?.add(layout9)
        mRelativeLayout?.add(layout10)



        tv_jptj_more.setOnClickListener {
            startActivity<BankListActivity>(BankListActivity.TYPE to "4")
            TCAgent.onEvent(mContext, TalkingDataParams.MORE_BANK, "quality")
        }
        tv_gnk_more.setOnClickListener {
            startActivity<BankListActivity>(BankListActivity.TYPE to "1")
            TCAgent.onEvent(mContext, TalkingDataParams.MORE_BANK, "ability")
        }


        swirefresh.setEnableNestedScroll(true)//是否启用嵌套滚动
        swirefresh.isEnableRefresh = true//是否启用下拉刷新功能
        swirefresh.isEnableLoadMore = false
        swirefresh.setOnRefreshListener {
            mPresenter.getCreditCardData()
        }


    }

    override fun initData() {
        mPresenter.getCreditCardData()
    }


    override fun getCreditDataComplete(data: HomeCreditCardBean) {
        swirefresh.finishRefresh()
        if (null==data){
            return
        }
        mBean = data

        tv_gnk.text  =mBean?.ability_title
        tv_jptj.text  =mBean?.quality_title
        showBanner()
        showBankList()
        showAblityInfo()
        showGoodCardInfo()

    }

    //显示功能卡专业数据
    private fun showAblityInfo() {
        val list = mBean!!.ability_list
        for(posotion in list.indices){
            val item = list[posotion]
            ImageUtil.loadWithCache(mContext!!, mImageView!![posotion],item.ability_logo,R.drawable.default_bank_card_logo)
            mTitletxt!![posotion].text=  item.ability_name
            mDesctxt!![posotion].text=  item.ability_des
            setRelativeClick(mRelativeLayout!![posotion],posotion)
        }

    }

    //精品推荐数据
    private fun showGoodCardInfo() {
        if (null !=mBean?.quality_list && mBean!!.quality_list.size>0){
            mgoodAdapter  = HomeCardListAdapter(mBean!!.quality_list,mContext!!)
            jp_recycleview.layoutManager =   StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            jp_recycleview.adapter = mgoodAdapter

            mgoodAdapter?.setOnItemClickListener { adapter, _, position ->

                val item = adapter.getItem(position) as HomeCreditCardBean.QualityListBean
                if (!TextUtils.isEmpty(item.credit_id)){
                    startActivity<CreditCardDetailActivity>(CreditCardDetailActivity.CARDID to item.credit_id)
                }
            }
        }
    }

    //展示banner数据
    private fun showBanner() {
//         val views:List<View>


    /*    if (null !=mBean?.banner_list && mBean!!.banner_list.isNotEmpty()) {
            for (item in mBean!!.banner_list) {
                var viewItem = View.inflate(context, R.layout.item_home_card_top_banner, null)
              ImageUtil.loadNoCache(mContext!!,  viewItem.iv_card,item.imgUrl,R.drawable.default_bank_card_logo)
                views.add(viewItem!!)
            }
            banner_guide_content.setData(views)
            banner_guide_content.setAutoPlayAble(true)
        }else{
            ToastUtil.showToast(mContext,"没有信用卡数据")
            return
        }*/

        banner_guide_content.setData(mBean?.banner_list, ArrayList())
        banner_guide_content.setAdapter(object : BGABanner.Adapter<ImageView,HomeCreditCardBean.BannerListBean>{
            override fun fillBannerItem(banner: BGABanner?, itemView: ImageView?, model: HomeCreditCardBean.BannerListBean?, position: Int) {
                ImageUtil.loadWithCache(mContext!!,itemView!!,model!!.imgUrl,R.drawable.default_bank_logo)
            }

        })
        banner_guide_content.setDelegate( { _, _, _, position ->
            val item = mBean!!.banner_list[position]

            when(item.type){
                "1"->{
                  startActivity<CreditCardDetailActivity>(CreditCardDetailActivity.CARDID to item.cardId)
                }
                "2"->{
                    startActivity<WebVerifyActivity>(WebVerifyActivity.WEB_URL_KEY to item.jumpUrl,WebVerifyActivity.WEB_URL_TITLE to item.cardName)

                }
                "3"->{
                    //nothing
                }

            }

            mPresenter.dian(GlobalParams.FALG_NINETEEN,item.cardId)
        })

    }

    //显示银行卡数据
    private fun showBankList() {
        //初始化adapter
        mBanList= mBean?.bank_list
        val cashList: ArrayList<HomeCreditCardBean.BankListBean> = ArrayList()
        val moreList: ArrayList<HomeCreditCardBean.BankListBean> = ArrayList()
        if (mBanList!!.size>=8) {
           for (i in mBanList!!.indices) {
               if (i<8) {
                   cashList.add(mBanList!![i])
               }else{
                   moreList.add(mBanList!![i])
               }
           }
       }else{
            cashList.addAll(mBanList!!)
       }
        mBankListAdapter = HomeCardBankListAdapter(cashList,context)
        val manager = MyGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        manager.setScrollEnabled(false)
        home_card_recyclerview.layoutManager =   manager
        home_card_recyclerview.adapter = mBankListAdapter

        if (null != mBean?.bank_list && mBean!!.bank_list.size>8){
            val footView = View.inflate(mContext, R.layout.item_home_card_foot_view, null)
            footView.setOnClickListener {
                if (!isOpen) {
                    footView.item_foot_icon.rotation = 180f
                    isOpen = true
                    cashList!!.addAll(moreList)
                }else{
                    cashList.removeAll(moreList)
                    footView.item_foot_icon.rotation = 0f
                    isOpen = false
                }
                mBankListAdapter?.notifyDataSetChanged()
            }
            if(!mBankListAdapter!!.isFooterViewAsFlow) {
                mBankListAdapter?.addFooterView(footView)
            }
        }

        mBankListAdapter?.setOnItemClickListener { adapter, view, position ->
           //4列银行点击事件
            val listBean = adapter.getItem(position) as HomeCreditCardBean.BankListBean
            startActivity<BankListActivity>(BankListActivity.TYPE to "2",BankListActivity.BANKID to listBean.bank_id )

            val kv =  hashMapOf<String,String>()
            kv.put("bankId", listBean.bank_id)
            TCAgent.onEvent(mContext, TalkingDataParams.BANK_LIST, "", kv)
        }
    }

    fun setRelativeClick(view: RelativeLayout,posotion:Int){
        //功能卡专区点击事件
        view.setOnClickListener {
            val ability_id = mBean!!.ability_list[posotion].ability_id
            startActivity<BankListActivity>(BankListActivity.ABILITY_ID to ability_id,BankListActivity.TYPE to "3")

            val kv =  hashMapOf<String,String>()
            kv.put("abilityId", ability_id)
            TCAgent.onEvent(mContext, TalkingDataParams.ABILITY_LIST, "", kv)
        }
    }
}
