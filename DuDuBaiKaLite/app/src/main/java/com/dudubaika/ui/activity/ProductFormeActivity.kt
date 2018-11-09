package com.dudubaika.ui.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.UsersAuthLimitBean
import com.dudubaika.presenter.contract.GetUserLoanContract
import com.dudubaika.presenter.impl.GetUserLoanPresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_product_forme.*
import org.jetbrains.anko.startActivity
import android.widget.ImageView
import com.dudubaika.base.GlobalParams


/**
 * 我的额度界面
 */
class ProductFormeActivity : BaseActivity<GetUserLoanPresenter>(), GetUserLoanContract.View {

    //当前界面展示的方式
    private var type:String?=null
    private var mBean:UsersAuthLimitBean?=null
    var mDatas:ArrayList<UsersAuthLimitBean.ProductListBean>?= ArrayList()
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

    override fun getLayout(): Int =R.layout.activity_product_forme


    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_product_forme)
        defaultTitle="我的额度"
        iv_return.setOnClickListener {
            backActivity()
        }

        initAnimate()


    }

    override fun initData() {
        mPresenter.getData()
    }

    override fun showData(data: UsersAuthLimitBean) {
        if (null==data){
            return
        }
        mBean= data
        mDatas= mBean!!.product_list
        mine_ed_value.text= mBean!!.loan_value
        wnpe.text= mBean!!.list_label
        when(mBean!!.status){

            "1"->{
                showMineData()
            }
            "2"->{
                showOtersData()
            }
            else->{
                showOtersData()
            }

        }

    }


    private fun showOtersData() {

        //未过风控展示
        listview2.visibility= View.GONE
        listview.visibility= View.VISIBLE
        listview.adapter = object : CommonAdapter<UsersAuthLimitBean.ProductListBean>(mActivity, mDatas, R.layout.item_hot_product2) {
            override fun convert(holder: ViewHolder?, item: UsersAuthLimitBean.ProductListBean?, posotion: Int) {


                val item = mDatas!!.get(posotion)

                val circleImageView = holder?.getView<CircleImageView>(R.id.profile_image) as ImageView
                ImageUtil.loadNoCache(mContext!!,circleImageView,item!!.product_logo,R.drawable.ic_error_close)
                holder?.getView<TextView>(R.id.product_name)?.text = item.product_name

                if (!TextUtils.isEmpty(item.activity_name)) {
                    var tv = holder.getView<TextView>(R.id.hot)
                    tv.text = item.activity_name
                    var tagName  =item!!.activity_name.toString().trim()

                    when (tagName){
                        "周期长"-> tv.setBackgroundResource(R.drawable.zqc)
                        "秒提现"->tv.setBackgroundResource(R.drawable.mine_ed_2)
                        "额度高"-> tv.setBackgroundResource(R.drawable.mine_ed_3)
                        "新上线"-> tv.setBackgroundResource(R.drawable.hd)
                        "力荐" -> tv.setBackgroundResource(R.drawable.lj)
                        else-> tv.setBackgroundResource(R.drawable.hd)

                    }
                }else{
                    holder?.getView<TextView>(R.id.hot)?.visibility = View.GONE
                }

                //额度
                holder?.getView<TextView>(R.id.ed_money_value1)?.text = item.quota_start_value
                if (!TextUtils.isEmpty(item.quota_end_value)) {
                    holder?.getView<TextView>(R.id.ed_money_value2)?.text = ""//item.quota_start_unit
                    holder?.getView<TextView>(R.id.ed_money_value3)?.text = "-" + item.quota_end_value
                    holder?.getView<TextView>(R.id.ed_money_value4)?.text = item.quota_end_unit
                }else{
                    holder?.getView<TextView>(R.id.ed_money_value2)?.text = item.quota_start_unit
                }
                holder?.getView<TextView>(R.id.ed)?.text = item.quota_name

                //利率
                holder?.getView<TextView>(R.id.qx)?.text = item.rate_unit+item.rate_name
                holder?.getView<TextView>(R.id.qx_time_value3)?.text = item.rate_value

                //下款时长
                holder?.getView<TextView>(R.id.yll)?.text = item.loan_name
                holder?.getView<TextView>(R.id.yll_value1)?.text = item.loan_time_value
                holder?.getView<TextView>(R.id.yll_value2)?.text = item.loan_time_unit

                //期限20天-36月
                holder?.getView<TextView>(R.id.cycle)?.text = item.term_start_value +" "+ item.term_start_unit+"-"+item.term_end_value+" "+item.term_end_unit
                //已放款人数
                holder?.getView<TextView>(R.id.money_spend)?.text = "今日已放款"+" "+item.apply_count+" 人"

                val linearLayout = holder?.getView<LinearLayout>(R.id.tag_list)

                if (linearLayout!!.childCount>0) {
                    linearLayout?.removeAllViews()
                }

                var index= 1
                for (item in item.product_tags){

                    //添加标签组
                    val showText =  TextView(mContext)
                    showText.textSize = 10f
                    showText.text=item.tag_name
                    showText.gravity = Gravity.CENTER
                    showText.setPadding(20,5,20,5)
                    if ("1"==item.is_light) {
                        showText.setBackgroundResource(R.drawable.shape_home_tag)
                        showText.setTextColor(resources.getColor(R.color.home_item_tag))
                    }else{
                        showText.setBackgroundResource(R.drawable.shape_home_tag)
                        showText.setTextColor(resources.getColor(R.color.global_edit))
                    }
                    if (index!=1){
                        // set 文本大小
                        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                        //set 四周距离
                        params.setMargins(40, 0, 0, 0)
                        showText.layoutParams =params
                    }
                    linearLayout.addView(showText)
                    index++
                }
                holder.getView<LinearLayout>(R.id.itme_product).setOnClickListener {
                    startActivity<ProductInfoActivity>(ProductInfoActivity.PRODUCT_ID to item.product_id, ProductInfoActivity.TILTLE to item.product_name)
                }

            }
        }
    }

    private fun showMineData() {

        //我的记账条目数据
        listview.visibility= View.GONE
        listview2.visibility= View.VISIBLE
        listview2.adapter = object : CommonAdapter<UsersAuthLimitBean.ProductListBean>(mActivity, mDatas, R.layout.item_hot_product4) {
            override fun convert(holder: ViewHolder?, item: UsersAuthLimitBean.ProductListBean?, posotion: Int) {

                if (posotion==mDatas.size-1){
                   holder?.setVisible(R.id.bottom_line,true)
                }else{
                    holder?.setVisible(R.id.bottom_line,false)
                }

                val circleImageView = holder?.getView<CircleImageView>(R.id.product_logo2)
                ImageUtil.load(mActivity, item!!.product_logo, R.drawable.ic_error_close, circleImageView)
                holder!!.getView<TextView>(R.id.product_name2).text = item!!.product_name
                holder!!.getView<TextView>(R.id.desc).text = item!!.product_desc

                if (!TextUtils.isEmpty(item.activity_name)) {
                    holder.getView<TextView>(R.id.hot2).text = item.activity_name
                }else{
                    holder.getView<TextView>(R.id.hot2).visibility = View.GONE
                }

                holder.getView<RelativeLayout>(R.id.itme_product2).setOnClickListener {

                    startActivity<ProductInfoActivity>(ProductInfoActivity.PRODUCT_ID to item.product_id, ProductInfoActivity.TILTLE to item.product_name)
                }

            }
        }
    }
    private fun initAnimate() {
        val animate = ObjectAnimator.ofFloat(white_circle, "rotation", 0f, 360f)
        animate.interpolator = LinearInterpolator()
        animate.repeatMode = ValueAnimator.RESTART
        animate.repeatCount = 0
        animate.duration = 1500L
        animate.start()
    }

}
