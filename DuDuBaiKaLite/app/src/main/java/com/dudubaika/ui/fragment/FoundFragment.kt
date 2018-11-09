package com.dudubaika.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.base.GlobalParams
import com.dudubaika.model.bean.HomeFoundBean
import com.dudubaika.presenter.contract.HomeFoundContract
import com.dudubaika.presenter.impl.HomeFoundPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.util.*
import kotlinx.android.synthetic.main.fragment_found.*
import org.jetbrains.anko.support.v4.startActivity
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.dudubaika.ui.activity.*


/**
 *发现
 *
 */
class FoundFragment : BaseFragment<HomeFoundPresenter>(), HomeFoundContract.View {

    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()
    private var mBean:HomeFoundBean?=null
    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
        refresh.finishRefresh()
    }

    override fun showError(url: String, msg: String) {
        refresh.finishRefresh()
    }

    override fun getLayout(): Int = R.layout.fragment_found

    override fun initView() {

        StatusBarUtil.setPaddingSmart(mContext,tb_found)
        defaultTitle="发现首页"
        //嵌套滚动
        refresh.setEnableNestedScroll(true)
        //启用下拉刷新功能
        refresh.isEnableRefresh = true
        //上拉加载关闭
        refresh.isEnableLoadMore = false
        refresh.setOnRefreshListener({
            mPresenter.loadData()
        })
    }

    override fun initData() {
        mPresenter.loadData()
    }


    override fun showData(bean: HomeFoundBean) {
        //显示数据
        refresh.finishRefresh()
        if (null==bean){
            return
        }
        mBean = bean
//        showView()
         if ("2".equals(mBean?.is_discuss)){
            //论坛关闭
            jqqd.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }else{
            jqqd.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        initRecyclerView()
    }

   /* private fun showView() {


        if (null!= mBean?.discuss_list && mBean!!.discuss_list.size>0) {
            recyclerView.layoutManager = LinearLayoutManager(mContext)
            recyclerView.adapter = object : BaseQuickAdapter<HomeFoundBean.DiscussListBean, BaseViewHolder>(R.layout.item_found_home, mBean?.discuss_list) {
                override fun convert(helper: BaseViewHolder?, item: HomeFoundBean.DiscussListBean?) {

                    helper?.setText(R.id.tv_phone, "")
                    ImageUtil.loadNoCache(mContext, helper!!.getView<ImageView>(R.id.iv), item!!.discuss_logo, R.drawable.default_bank_card_logo)
                    val encryptPhoneNum = Utils.encryptPhoneNum(item?.discuss_name)
                    helper?.setText(R.id.tv_phone, encryptPhoneNum)
                    helper?.setText(R.id.tv_title, item?.discuss_title)
                    helper?.getView<RelativeLayout>(R.id.item_home_talk).setOnClickListener {
                        startActivity<TalkDetailActivity>(TalkDetailActivity.ARTICEID to item?.discuss_id)
                    }
                }

            }
        }



    }*/

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
        val delegateAdapter = DelegateAdapter(layoutManager, true)
        recyclerView.adapter = delegateAdapter

        //标签分类的展示
        val tagList = mBean?.tags_arr
        if (null != tagList && tagList?.size>0){
            val tagArrAdapter = initTagArrAdapter()
            mAdapters.add(tagArrAdapter)
        }


        //判断是否有热门数据
        val discuss_list = mBean?.discuss_list
        if (discuss_list != null  ) {
            val discussAdapter = initDiscussAdapter()
            mAdapters.add(discussAdapter)
        }

        //判断是否有图片数据

       /* if (!TextUtils.isEmpty(mBean?.left_pic) && !TextUtils.isEmpty(mBean?.right_pic)) {
            val imageAdapter = initImageAdapter()
            //添加置顶数据
            mAdapters.add(imageAdapter)
        }*/

        //是否有活动
        if (null !=mBean?.active_list&& mBean!!.active_list.size >0){

            val activityAdapter = initActivityAdapter()
            mAdapters.add(activityAdapter)
        }

   delegateAdapter.setAdapters(mAdapters)

}

    /**
     * 顶部tag 数据展示
     */
    fun initTagArrAdapter(): BaseDelegateAdapter {

        //图中中间布局部分
        val gridLayoutHelper = GridLayoutHelper(4)//列，
        gridLayoutHelper.vGap = 4
        //顶部
        return object : BaseDelegateAdapter(activity, gridLayoutHelper,R.layout.item_found_home_tag, mBean!!.tags_arr.size, GlobalParams.VLAYOUT_TOPLIST) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                var item = mBean!!.tags_arr[position]
                helper?.setText(R.id.tv_tag_name, item.tag_name)
                ImageUtil.loadNoCache(mContext!!, helper!!.getView<ImageView>(R.id.iv), item!!.tag_logo, R.drawable.default_bank_card_logo)

                helper?.getView<RelativeLayout>(R.id.item_home_talk).setOnClickListener {
                    startActivity<TagProductActivity>(TagProductActivity.TAG_ID to item?.tag_id,
                            TagProductActivity.TITLE to item?.tag_name)
                }
            }
        }
    }


/**
* 热门帖子数据-》现变为帖子入口
*/
      fun initDiscussAdapter(): BaseDelegateAdapter {
    //帖子入口
       return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),R.layout.recyclerview_found_foot_view, mBean!!.discuss_list.size, GlobalParams.VLAYOUT_BANNER) {
             override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                 var item = mBean!!.discuss_list[position]
                ImageUtil.loadNoCache(mContext!!, helper!!.getView<ImageView>(R.id.found_in), item!!.discuss_logo, R.drawable.default_bank_card_logo)

                helper?.getView<RelativeLayout>(R.id.talk_in).setOnClickListener {
                startActivity<TalkHomeActivity>(TalkHomeActivity.TYPE to item?.discuss_type)
            }
       }
     }
  }

    //论坛入口 --》1.5.0去掉了
   /* fun  initImageAdapter(): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),R.layout.recyclerview_found_foot_view, 1, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                ImageUtil.loadNoCache(mContext!!, helper!!.getView<ImageView>(R.id.found_left_img), mBean!!.left_pic, R.drawable.default_bank_card_logo)
                ImageUtil.loadNoCache(mContext!!, helper!!.getView<ImageView>(R.id.found_right_img), mBean!!.right_pic, R.drawable.default_bank_card_logo)

                helper!!.getView<ImageView>(R.id.found_left_img).setOnClickListener {
                    startActivity<TalkHomeActivity>(TalkHomeActivity.TYPE to "1")
                }
                helper!!.getView<ImageView>(R.id.found_right_img).setOnClickListener {
                    startActivity<TalkHomeActivity>(TalkHomeActivity.TYPE to "2")
                }

            }
        }
    }*/


    //活动入口
    fun initActivityAdapter(): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(),R.layout.item_found_activity_img,  mBean!!.active_list.size, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                val item=  mBean!!.active_list[position]
                if (position==0){
                  helper.getView<View>(R.id.line).visibility = View.VISIBLE
                }else{
                    helper.getView<View>(R.id.line).visibility = View.GONE
                }
                ImageUtil.loadNoCache(mContext!!, helper!!.getView<ImageView>(R.id.activity_img),item.active_img, R.drawable.default_bank_card_logo)

                helper!!.getView<ImageView>(R.id.activity_img).setOnClickListener {
                    startActivity<WebVerifyActivity>(WebVerifyActivity.WEB_URL_KEY to item!!.active_url , WebVerifyActivity.WEB_URL_TITLE to "活动详情")
                    mPresenter.dian(GlobalParams.FALG_TWOSEVEN,item!!.active_id)
                }


            }
        }
    }

}
