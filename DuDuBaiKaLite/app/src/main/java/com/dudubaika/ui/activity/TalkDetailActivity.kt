package com.dudubaika.ui.activity

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.Spannable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.TalkDetailBean
import com.dudubaika.presenter.contract.TalkDetailContact
import com.dudubaika.presenter.impl.TalkDetailPresenter
import com.dudubaika.ui.adapter.BaseDelegateAdapter
import com.dudubaika.ui.adapter.NoScrollGridAdapter
import com.dudubaika.ui.adapter.TalkDetailCommentAdapter
import com.dudubaika.ui.view.NoUnderlineSpan
import com.dudubaika.util.*
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_talk_detail.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList
import android.text.Spanned

/**
 * 帖子详情
 */
class TalkDetailActivity : BaseActivity<TalkDetailPresenter>(), TalkDetailContact.View {

    /** 存放各个模块的适配器 */
    private var mAdapters: MutableList<DelegateAdapter.Adapter<*>> = mutableListOf()
    //是否能评论 //isPublish  1=显示；2=不显示
    private var isComment:Boolean=true
    private var topView:View?=null
//    private var manager: RecyclerViewNoBugLinearLayoutManager?=null
    private var mBean:TalkDetailBean?=null
    private var articeId:String?=null
    private var mCommentAdapter: TalkDetailCommentAdapter?=null

    companion object {

        var ARTICEID :String = "articeId"
        var ISPUBLISH :String = "isPublish"
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int = R.layout.activity_talk_detail

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_title)
        defaultTitle="帖子详情"
        topView = View.inflate(mActivity,R.layout.view_talk_detail_top,null)
        iv_return.setOnClickListener {
            backActivity()
        }

        fabu.setOnClickListener {
            //评论

            if (UserUtil.isLogin(mActivity)) {
                if (TextUtils.isEmpty(fabu_content.text.toString().trim())) {
                    ToastUtil.showToast(mActivity, "请您输入评论内容")
                    return@setOnClickListener
                }
                if (!TextUtils.isEmpty(articeId)) {
//                    fabu.isClickable = false
                    mPresenter.Talkdiss(articeId!!, fabu_content.text.toString().trim())
                }
            }else{
                startActivity<LoginActivity>()
            }


        }



        fabu_content.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString().trim())){
                    fabu.setTextColor(resources.getColor(R.color.home_zx_text))
//                    fabu.isClickable = false
                }else{
                    fabu.setTextColor(resources.getColor(R.color.global_txt_black9))
//                    fabu.isClickable = true
                }
            }

        })

    }

    override fun initData() {
        articeId =  intent.getStringExtra(ARTICEID)
        isComment = intent.getBooleanExtra(ISPUBLISH,true)


        if (!TextUtils.isEmpty(articeId)){
            mPresenter.getDeatilData(articeId!!)
        }
//        manager = RecyclerViewNoBugLinearLayoutManager(this, StaggeredGridLayoutManager.VERTICAL,false)

        val kv =  hashMapOf<String,String>()
        kv.put("discussId", articeId!!)
        TCAgent.onEvent(mActivity, TalkingDataParams.DISCUSS_DETIAL, "", kv)
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }
    override fun dissTalkCompete() {
        //评论帖子成功
        fabu.isClickable = true
        fabu_content.setText("")
        mCommentAdapter?.removeAllHeaderView()
        mBean= null
        mPresenter.getDeatilData(articeId!!)

        val kv =  hashMapOf<String,String>()
        kv.put("discussId", articeId!!)
        TCAgent.onEvent(mActivity, TalkingDataParams.PUBLISH_COMMENT, "", kv)
    }


    override fun showDetailData(data: TalkDetailBean) {
        if (null==data){
            return
        }
        mBean = data
//        showView()

        initRecyclerView()
    }

    //界面展示
    private fun showView() {

        if ("2".equals(mBean?.dis_comment_status)){
            //不能评论
            fabu_content.isEnabled= false
            fabu_content.hint  = "您的账号被冻结，请联系管理员"
        }
        tv_title.text =mBean?.discuss_title
        topView?.findViewById<TextView>(R.id.tv_phone)?.text = mBean?.discuss_title
        topView?.findViewById<TextView>(R.id.tv_content)?.text = mBean?.discuss_content
        topView?.findViewById<TextView>(R.id.comments_num)?.text = "评论 "+mBean?.comment_count

        ImageUtil.loadNoCache(mActivity,topView!!.findViewById<ImageView>(R.id.detail_iv),mBean!!.discuss_logo,R.drawable.default_bank_logo)

        topView?.findViewById<TextView>(R.id.fabu_time)?.text = "发表于："+mBean?.discuss_push_time
        var gridview  =topView!!.findViewById<GridView>(R.id.gridview)
        if (null != mBean?.discuss_logo_list && mBean!!.discuss_logo_list.size > 0) {
            gridview.visibility = View.VISIBLE
            gridview.adapter = NoScrollGridAdapter(mActivity,  mBean!!.discuss_logo_list, mBean?.discuss_logo_url)
        } else {
            gridview.visibility = View.GONE
        }

        //评论

        /*mCommentAdapter  = TalkDetailCommentAdapter(mBean?.comment_list,mActivity)
        comment_recyclerview.layoutManager =manager
        comment_recyclerview.adapter  =mCommentAdapter
        mCommentAdapter?.addHeaderView(topView)
        comment_recyclerview.smoothScrollToPosition(0)*/
    }

    private fun initRecyclerView() {
        if ("2".equals(mBean?.dis_comment_status)){
            //不能评论
            fabu_content.isEnabled= false
            fabu_content.hint  = "您的账号被冻结，请联系管理员"
        }
        tv_title.text =mBean?.discuss_title

        mAdapters.clear()

        //初始化
        //创建VirtualLayoutManager对象
        val layoutManager = VirtualLayoutManager(mActivity)
        comment_recyclerview.layoutManager = layoutManager!!

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        val viewPool = RecyclerView.RecycledViewPool()
        comment_recyclerview.recycledViewPool = viewPool
        viewPool.setMaxRecycledViews(0, 20)

        //设置适配器
        val delegateAdapter = DelegateAdapter(layoutManager, true)
        comment_recyclerview.adapter = delegateAdapter

        //顶部数据
        val discussAdapter = initDiscussAdapter(1)
        mAdapters.add(discussAdapter)

        //是否有图片数据
        if (null != mBean?.discuss_logo_list && mBean!!.discuss_logo_list.size>0){
            val imageAdapter = initImageAdapter()
            mAdapters.add(imageAdapter)
        }
        //图片下面的数据
        val discussAdapter2 = initDiscussAdapter(2)
        mAdapters.add(discussAdapter2)



        //判断是否有评论

        if (null!=mBean?.comment_list && mBean!!.comment_list.size>0) {
            val imageAdapter = initCommentAdapter()
            //添加置顶数据
            mAdapters.add(imageAdapter)

        }



        delegateAdapter.setAdapters(mAdapters)

    }


    //顶部数据
    fun initDiscussAdapter(type :Int): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(),R.layout.view_talk_detail_top, 1, GlobalParams.VLAYOUT_BANNER) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)

                 helper.getView<ImageView>(R.id.right_icon).visibility = View.GONE
                if (1==type) {
                    val tvContent = helper?.getView<TextView>(R.id.tv_content)
                    val tvPhone =   helper?.getView<TextView>(R.id.tv_phone)
                    ImageUtil.loadNoCache(mActivity,   helper?.getView<ImageView>(R.id.detail_iv),mBean!!.discuss_logo,R.drawable.default_bank_logo)
                    tvPhone.text =  StringUtils.getInstance(mActivity).checkAutoLink(mBean?.discuss_title)
                    tvPhone.movementMethod = LinkMovementMethod.getInstance()
                    tvPhone.autoLinkMask = 0
                    tvContent.text = StringUtils.getInstance(mActivity).checkAutoLink(mBean?.discuss_content)
                    tvContent.movementMethod = LinkMovementMethod.getInstance()
                    tvContent.autoLinkMask = 0
                    helper?.getView<RelativeLayout>(R.id.rl_layout).visibility = View.VISIBLE
                    tvContent.visibility = View.VISIBLE


                    helper?.getView<RelativeLayout>(R.id.fb_rlayout).visibility = View.GONE
                    helper?.getView<RelativeLayout>(R.id.comment_num_rlayout).visibility = View.GONE
                }else if (2==type) {
                    helper?.getView<TextView>(R.id.comments_num)?.text = "评论 " + mBean?.comment_count
                    helper?.getView<TextView>(R.id.fabu_time)?.text = "发表于：" + mBean?.discuss_push_time

                    helper?.getView<RelativeLayout>(R.id.rl_layout).visibility = View.GONE
                    helper?.getView<TextView>(R.id.tv_content).visibility = View.GONE

                    helper?.getView<RelativeLayout>(R.id.fb_rlayout).visibility = View.VISIBLE
                    helper?.getView<RelativeLayout>(R.id.comment_num_rlayout).visibility = View.VISIBLE
                }


                helper?.getView<TextView>(R.id.jubao)?.setOnClickListener {
                    //举报
                    if (UserUtil.isLogin(mActivity)) {
                        startActivity<ReporTalkActivity>(ReporTalkActivity.DISSCUSID to articeId!!, ReporTalkActivity.TITLE to mBean!!.discuss_title,
                                ReporTalkActivity.PHONE to mBean!!.discuss_name)
                    }else{
                        startActivity<LoginActivity>()
                    }
                }
            }
        }
    }

    //图片adapter
    fun initImageAdapter(): BaseDelegateAdapter {

         val functionGridLayoutHelper =  GridLayoutHelper(3)
             functionGridLayoutHelper.setAutoExpand(false)
             functionGridLayoutHelper.setWeights( floatArrayOf(33f,33f,33f))

        return object : BaseDelegateAdapter(mActivity, functionGridLayoutHelper,R.layout.item_single_imageview, mBean!!.discuss_logo_list.size, GlobalParams.VLAYOUT_TITLE) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)
                val item = mBean!!.discuss_logo_list[position]

              val iv =  helper?.getView<ImageView>(R.id.item_iv)

                ImageUtil.loadWithCache(mActivity,iv,mBean?.discuss_logo_url+item,R.drawable.default_banner_icon)

                iv.setOnClickListener(View.OnClickListener {
                    val intent = Intent(mActivity, ImageLookActivity::class.java)
                    intent.putStringArrayListExtra(ImageLookActivity.LIST, mBean?.discuss_logo_list as ArrayList<String>)
                    intent.putExtra(ImageLookActivity.POSOTION, position)
                    intent.putExtra(ImageLookActivity.IMGURL, mBean?.discuss_logo_url)
                    startActivity(intent)
                })
            }
        }
    }

    //评论数据
    fun initCommentAdapter(): BaseDelegateAdapter {
        //banner
        return object : BaseDelegateAdapter(mActivity, LinearLayoutHelper(),R.layout.item_talk_detail_comment, mBean!!.comment_list.size, GlobalParams.VLAYOUT_TOPLIST) {
            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                super.onBindViewHolder(helper, position)

                if (null !=mBean && mBean!!.comment_list.size>0) {
                    val item  = mBean!!.comment_list[position]
                    helper.setText(R.id.tv_phone, Utils.encryptPhoneNum(item.cus_name))
                    helper.setText(R.id.comment_content, item.comment_content)
                    helper.setText(R.id.comment_time, item.comment_time)
                    ImageUtil.loadNoCache(mActivity, helper.getView<View>(R.id.item_iv) as ImageView, item.cus_logo, R.drawable.money_da)
                }

            }
        }
    }

}
