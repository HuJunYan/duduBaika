package com.dudubaika.ui.fragment


import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.bingoogolapple.bgabanner.BGABanner
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.model.bean.HomeCreditCardBean
import com.dudubaika.presenter.contract.CreditCardContract
import com.dudubaika.presenter.impl.CreditCardPresenter
import com.dudubaika.ui.adapter.HomeCardBankListAdapter
import com.dudubaika.ui.view.MyGridLayoutManager
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.base.GlobalParams
import com.dudubaika.ui.activity.BankListActivity
import com.dudubaika.ui.activity.CreditCardDetailActivity
import com.dudubaika.ui.activity.WebVerifyActivity
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.ui.adapter.HomeCardListAdapter
import com.dudubaika.util.ImageUtil
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_credit_card.*
import org.jetbrains.anko.support.v4.startActivity
import kotlin.collections.ArrayList
import android.graphics.drawable.Drawable
import java.io.IOException
import java.net.URL


/**
 *信用卡fragment
 */
class CreditCardFragment : BaseFragment<CreditCardPresenter>(),CreditCardContract.View {
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

    private var mAdapters : MutableList<DelegateAdapter.Adapter<*>> =  mutableListOf()
    var cashList: ArrayList<HomeCreditCardBean.BankListBean> = ArrayList()
    var moreList: ArrayList<HomeCreditCardBean.BankListBean> = ArrayList()
    private var delegateAdapter: DelegateAdapter?=null

    override fun getLayout(): Int = R.layout.fragment_credit_card


    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
        swirefresh.finishRefresh()
    }

    override fun showError(url: String, msg: String) {
        swirefresh.finishRefresh()

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mContext,tb_card_fg)
        mBanList = ArrayList()
        mCardList = ArrayList()
        mTitletxt = ArrayList()
        mImageView = ArrayList()
        mDesctxt = ArrayList()
        mRelativeLayout = ArrayList()

       /* mImageView?.add(iv1)
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
        }
        tv_gnk_more.setOnClickListener {
            startActivity<BankListActivity>(BankListActivity.TYPE to "1")
        }


        swirefresh.setEnableNestedScroll(true)//是否启用嵌套滚动
        swirefresh.isEnableRefresh = true//是否启用下拉刷新功能
        swirefresh.isEnableLoadMore = false
        swirefresh.setOnRefreshListener {
            mPresenter.getCreditCardData()
        }*/


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
        initRecyclerView()
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
        delegateAdapter= DelegateAdapter(layoutManager, true)
        recyclerView.adapter = delegateAdapter

        //判断是否有banner数据
        val discuss_list = mBean?.banner_list
        if (discuss_list != null  ) {
            val discussAdapter = initBannerAdapter()
            mAdapters.add(discussAdapter)
        }

        //判断是否有银行卡数据
        val bank_list = mBean?.bank_list
        if (null != bank_list){
            val bankListAdapter =  initBankListAdapter()
            mAdapters.add(bankListAdapter)
        }

        //判断银行数据是否大于8个
        if(null != bank_list && bank_list.size>8){
            val moreViewAdapter = initMoreBankView()
            mAdapters.add(moreViewAdapter)
        }



        delegateAdapter?.setAdapters(mAdapters)

    }

    //banner 数据
    private fun initBannerAdapter(): BaseDelegateAdapter {
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),R.layout.view_card_banner, 1, GlobalParams.VLAYOUT_BANNER) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                var item = mBean!!.banner_list[position]
                var banner_guide_content  =helper.getView<BGABanner>(R.id.banner_guide_content)
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
        }

    }

    //银行列表数据
    private fun initBankListAdapter(): BaseDelegateAdapter {

        val functionGridLayoutHelper =  GridLayoutHelper(4)
        functionGridLayoutHelper.setWeights(floatArrayOf(25f, 25f, 25f,25f))//设置权重
        functionGridLayoutHelper.setAutoExpand(true)

        //初始化adapter
        mBanList= mBean?.bank_list

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

        return object : BaseDelegateAdapter(activity, functionGridLayoutHelper,R.layout.item_home_card_list,  cashList.size, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                var item = cashList[position]
                ImageUtil.loadWithCache(mContext!!, helper.getView<CircleImageView>(R.id.item_circle_bank_icon), item.bank_logo, R.drawable.product_logo_default)
                helper.setText(R.id.item_bank_name, item.bank_name)

//                if (null != mBean?.bank_list && mBean!!.bank_list.size>8){
//                    val footView = View.inflate(mContext, R.layout.item_home_card_foot_view, null)
//                    footView.setOnClickListener {
//                        if (!isOpen) {
//                            footView.item_foot_icon.rotation = 180f
//                            isOpen = true
//                            cashList!!.addAll(moreList)
//                        }else{
//                            cashList.removeAll(moreList)
//                            footView.item_foot_icon.rotation = 0f
//                            isOpen = false
//                        }
//                        mBankListAdapter?.notifyDataSetChanged()
//                    }
//                    if(!mBankListAdapter!!.isFooterViewAsFlow) {
//                        mBankListAdapter?.addFooterView(footView)
//                    }
//                }
                helper.getView<RelativeLayout>(R.id.item_bank).setOnClickListener {


                    startActivity<BankListActivity>(BankListActivity.TYPE to "2",BankListActivity.BANKID to item.bank_id )
                }

            }
        }
    }

    private fun loadImageFromNetwork(imageUrl: String): Drawable? {
        var drawable: Drawable? = null
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    URL(imageUrl).openStream(), "$imageUrl.jpg")
        } catch (e: IOException) {
        }

        if (drawable == null) {
        } else {
        }

        return drawable
    }

    //更多view
    private fun initMoreBankView(): BaseDelegateAdapter {

        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),R.layout.item_home_card_foot_view,  1, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)

                val footView  = helper.getView<ImageView>(R.id.item_foot_icon)
                footView.setOnClickListener {
                        if (!isOpen) {
                            footView.rotation = 180f
                            isOpen = true
                            cashList!!.addAll(moreList)
                        }else{
                            cashList.removeAll(moreList)
                            footView.rotation = 0f
                            isOpen = false
                        }
                    delegateAdapter?.notifyDataSetChanged()
                    }
            }
        }
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
//        if (null !=mBean?.quality_list && mBean!!.quality_list.size>0){
//            mgoodAdapter  = HomeCardListAdapter(mBean!!.quality_list,mContext!!)
//            jp_recycleview.layoutManager =   StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//            jp_recycleview.adapter = mgoodAdapter
//
//            mgoodAdapter?.setOnItemClickListener { adapter, _, position ->
//
//                val item = adapter.getItem(position) as HomeCreditCardBean.QualityListBean
//                if (!TextUtils.isEmpty(item.credit_id)){
//                    startActivity<CreditCardDetailActivity>(CreditCardDetailActivity.CARDID to item.credit_id)
//                }
//            }
//        }
    }



    //显示银行卡数据
   /* private fun showBankList() {
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
        }
    }*/

    fun setRelativeClick(view: RelativeLayout,posotion:Int){
        //功能卡专区点击事件
        view.setOnClickListener {
            val ability_id = mBean!!.ability_list[posotion].ability_id
            startActivity<BankListActivity>(BankListActivity.ABILITY_ID to ability_id,BankListActivity.TYPE to "3")
        }
    }
}
