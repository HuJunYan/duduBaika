package com.dudubaika.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.PointEvent
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.ui.activity.ProductInfoActivity
import com.dudubaika.util.ImageUtil
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

class ProductAdapter(var activity: Context,var totalList :ArrayList<ProductInfoListBean.ProductListBean>,
                     var product_type :String,var pointFlag :String){

    /**
     * 热门item
     */
    fun initHotProductAdapter(): BaseDelegateAdapter {


        return object : BaseDelegateAdapter(activity, LinearLayoutHelper(), R.layout.item_hot_product2, totalList!!.size, GlobalParams.VLAYOUT_HOTLIST) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)

                if (null !=totalList && totalList!!.size>0) {
                    val item = totalList!![position]

                    val circleImageView = holder.getView<CircleImageView>(R.id.profile_image)
                    ImageUtil.loadNoCache(activity!!, circleImageView, item.product_logo, R.drawable.ic_error_close)
                    holder.getView<TextView>(R.id.product_name).text = item.product_name

                    if (!TextUtils.isEmpty(item.activity_name)) {
                        var tv = holder.getView<TextView>(R.id.hot)
                        tv.text = item.activity_name

                        when (item!!.activity_name) {
                            "周期长" -> tv.setBackgroundResource(R.drawable.zqc)
                            "秒提现" -> tv.setBackgroundResource(R.drawable.mine_ed_2)
                            "额度高" -> tv.setBackgroundResource(R.drawable.mine_ed_3)
                            "新上线" -> tv.setBackgroundResource(R.drawable.hd)
                            "力荐" -> tv.setBackgroundResource(R.drawable.lj)
                            else -> tv.setBackgroundResource(R.drawable.hd)

                        }
                    } else {
                        holder.getView<TextView>(R.id.hot).visibility = View.GONE
                    }

                    //额度
                    holder.getView<TextView>(R.id.ed_money_value1).text = item.quota_start_value

                    if (!TextUtils.isEmpty(item.quota_end_value)) {
                        holder.getView<TextView>(R.id.ed_money_value2).text = ""
                        holder.getView<TextView>(R.id.ed_money_value3).text = "-" + item.quota_end_value
                        holder.getView<TextView>(R.id.ed_money_value4).text = item.quota_end_unit
                    } else {
                        holder.getView<TextView>(R.id.ed_money_value2).text = item.quota_start_unit
                    }
                    holder.getView<TextView>(R.id.ed).text = item.quota_name

                    //利率
                    holder.getView<TextView>(R.id.qx).text = item.rate_unit + item.rate_name
                    holder.getView<TextView>(R.id.qx_time_value3).text = item.rate_value

                    //下款时长
                    holder.getView<TextView>(R.id.yll).text = item.loan_name
                    holder.getView<TextView>(R.id.yll_value1).text = item.loan_time_value
                    holder.getView<TextView>(R.id.yll_value2).text = item.loan_time_unit

                    //期限20天-36月
                    if (TextUtils.isEmpty(item.term_end_value)) {
                        holder.getView<TextView>(R.id.cycle).text = item.term_start_value + " " + item.term_start_unit
                    } else {
                        holder.getView<TextView>(R.id.cycle).text = item.term_start_value + " " + item.term_start_unit + "-" + item.term_end_value + " " + item.term_end_unit
                    }
                    //已放款人数
                    holder.getView<TextView>(R.id.money_spend).text = "今日已放款" + " " + item.apply_count + " 人"

                    val linearLayout = holder.getView<LinearLayout>(R.id.tag_list)

                    if (linearLayout.childCount > 0) {
                        linearLayout.removeAllViews()
                    }

                    var index = 1
                    for (item in item.product_tags) {

                        //添加标签组
                        val showText = TextView(activity)
                        showText.textSize = 10f
                        showText.text = item.tag_name
                        showText.gravity = Gravity.CENTER
                        showText.setPadding(20, 5, 20, 5)
                        if ("1" == item.is_light) {
                            showText.setBackgroundResource(R.drawable.shape_home_tag)
                            showText.setTextColor(activity.resources.getColor(R.color.home_item_tag))
                        } else {
                            showText.setBackgroundResource(R.drawable.shape_home_tag)
                            showText.setTextColor(activity.resources.getColor(R.color.global_edit))
                        }
                        if (index != 1) {
                            // set 文本大小
                            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT)
                            //set 四周距离
                            params.setMargins(40, 0, 0, 0)
                            showText.layoutParams = params
                        }
                        linearLayout.addView(showText)
                        index++
                    }
                    holder.getView<LinearLayout>(R.id.itme_product).setOnClickListener {
                        activity.startActivity<ProductInfoActivity>(ProductInfoActivity.PRODUCT_ID to item.product_id,
                                ProductInfoActivity.TILTLE to item.product_name,
                                ProductInfoActivity.PRODUCT_TYPE to product_type)
//                        mPresenter.dian(GlobalParams.FALG_TWO, item.product_id)
                        //发送event 埋点到homeFragment2
//                        EventBus.getDefault().post(PointEvent(pointFlag, item.product_id))
                    }
                }
            }
        }
    }

}