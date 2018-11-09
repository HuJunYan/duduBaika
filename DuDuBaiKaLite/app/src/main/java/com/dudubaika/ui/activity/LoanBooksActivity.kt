package com.dudubaika.ui.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.model.bean.MyLoadDetailBean
import com.dudubaika.presenter.contract.LoanBookContract
import com.dudubaika.presenter.impl.LoanBookPresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.UserUtil
import kotlinx.android.synthetic.main.activity_loan_books.*
import java.util.ArrayList
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import com.tendcloud.tenddata.TCAgent
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.top_myloan_view.view.*
import org.jetbrains.anko.startActivity


/**
 * 贷款账本界面
 */
class LoanBooksActivity : BaseActivity<LoanBookPresenter>(),LoanBookContract.View{

    var mBean:MyLoadDetailBean?=null
    var mDatas = ArrayList<MyLoadDetailBean.PaynoteListBean>()
    var mDataTimeMoney = ArrayList<MyLoadDetailBean.PaybackListBean>()
    var topView:View?=null
    override fun getLayout(): Int = R.layout.activity_loan_books

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() =Unit

    override fun hideProgress() = Unit

    override fun showError(url: String, msg: String) {

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_loan_book)
        defaultTitle="贷款账本"
        topView= View.inflate(mActivity, R.layout.top_myloan_view, null)

    }

    override fun initData() {
        iv_return.setOnClickListener {
            backActivity()
        }

    }

    override fun onResume() {
        super.onResume()
        mPresenter.getDetailData()

    }

    override fun onStop() {
        super.onStop()
        mBean= null
        listview.removeHeaderView(topView)
        mDatas.clear()
        mDataTimeMoney.clear()
    }

    override fun showData(data: MyLoadDetailBean?) {
        if (null==data){
            return
        }
        mBean= data
        mDatas = mBean!!.paynote_list as ArrayList<MyLoadDetailBean.PaynoteListBean>
        mDataTimeMoney= mBean!!.payback_list as ArrayList<MyLoadDetailBean.PaybackListBean>
//        showMyLoad()
        showTopView()
    }

    override fun changeStatusComplete() {
        mPresenter.getDetailData()
        TCAgent.onEvent(mActivity, TalkingDataParams.CHANGE_NOTE_SATTUS, "waitrepment")
    }


    //显示顶部数据
    private fun showTopView() {

        topView?.dh_key?.text =mBean?.total_loan_title
        topView?.dh_value?.text =mBean?.total_loan_value
        topView?.loan_record?.text =mBean?.left_title
        topView?.loan_record_num?.text =mBean?.left_value
        topView?.add_loan_record?.text =mBean?.right_title
        topView?.loan_remind?.text =mBean?.right_value
        topView?.history?.setOnClickListener {
            startActivity<MineLoanRecordActivity>()
        }

        topView?.add?.setOnClickListener {
            startActivity<AddLoanInfoActivity>()
        }
        iv_white_add.setOnClickListener {
            startActivity<AddLoanInfoActivity>()
        }

        topView!!.my_loan_money.adapter = object : CommonAdapter<MyLoadDetailBean.PaybackListBean>(mActivity, mDataTimeMoney, R.layout.item_myloan_money) {
            override fun convert(holer: ViewHolder?, item: MyLoadDetailBean.PaybackListBean?, posotion: Int) {

                val bundle = Bundle()
                holer?.setOnClickListener(R.id.week_day, {
                    bundle.putString(DayWeekMonthLoanActivity.TITLE,item?.title)
                    bundle.putString(DayWeekMonthLoanActivity.DATE_TAG,item?.date_flag)
                    gotoActivity(mActivity,DayWeekMonthLoanActivity::class.java,bundle)

                })
                holer?.setText(R.id.week_day_txt, item?.title)
                holer?.setText(R.id.week_day_money, item?.loan_value+"元")
            }
        }
        listview.addHeaderView(topView)
        showMyLoad()


    }

    private fun showMyLoad() {

            //我的记账条目数据
            listview.adapter = object : CommonAdapter<MyLoadDetailBean.PaynoteListBean>(mActivity, mDatas, R.layout.item_daihuan) {
                override fun convert(holer: ViewHolder?, item: MyLoadDetailBean.PaynoteListBean?, posotion: Int) {

                    if (UserUtil.getIsNew(mActivity)){
                        if (posotion==0){
                            holer?.setVisible(R.id.is_show_gif,true)
                            Glide.with( mActivity ).asGif().load( R.drawable.gif_yh ).into(holer!!.getView<ImageView>( R.id.is_show_gif ))
//                            Glide.with(mActivity).load(R.drawable.gif_yh).diskCacheStrategy(DiskCacheStrategy.ALL).into(R.id.is_show_gif)
                        }
                    }
                    UserUtil.saveIsNew(mActivity,"2")
                    holer?.setOnClickListener(R.id.content, {
                        if (item!!.jump_url.isNotEmpty()){
                            startActivity<WebActivity>(WebActivity.WEB_URL_KEY to item!!.jump_url)
                        }else{
                            //传递一个账单Id过去 即productId
                            startActivity<LoanDetailActivity>(LoanDetailActivity.PRODUCT_ID to item?.product_id)
                        }
                    })

                    holer?.setVisible(R.id.loan_status, false)
//                    ImageUtil.load(mActivity, item!!.product_logo, R.drawable.product_logo_default, holer!!.getView(R.id.product_logo))
                    ImageUtil.loadWithCache(mActivity,holer!!.getView<CircleImageView>(R.id.product_logo),item!!.product_logo,R.drawable.product_logo_default)
                    holer.setText(R.id.product_name, item?.product_name)
                    holer.setText(R.id.item_dh_key, item?.quota_title)
                    holer.setText(R.id.item_dh_money, item?.quota_value)
                    holer.setText(R.id.item_dh_date_key, item?.lastdate_title)
                    holer.setText(R.id.item_dh_date, item?.lastdate_value)

                    if("3"==item?.note_status){
                        holer.getView<TextView>(R.id.item_dh_date).setTextColor(resources.getColor(R.color.red))
                    }else{
                        holer.getView<TextView>(R.id.item_dh_date).setTextColor(resources.getColor(R.color.global_txt_black4))
                    }

                    holer.setVisible(R.id.btnDelete,false)

                    holer!!.setOnClickListener(R.id.btnred, {
                        //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                        (holer.convertView as SwipeMenuLayout).quickClose()
                        mPresenter.changeNoteStatus(item!!.product_id)
                        listview.removeHeaderView(topView)
                        changeStatus(posotion)
                    })
                    if (posotion==0 && holer?.getView<ImageView>(R.id.is_show_gif).visibility==View.VISIBLE ) {
                        holer?.setOnTouchListener(R.id.content) { _, _ ->
                            holer?.setVisible(R.id.is_show_gif, false)
                            false
                        }
                    }
                }
            }
    }

    private fun CommonAdapter<MyLoadDetailBean.PaynoteListBean>.changeStatus(posotion: Int) {
        mDatas.removeAt(posotion)
        notifyDataSetChanged()
    }


}
