package com.dudubaika.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dudubaika.R
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.util.ImageUtil

import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.startActivity


/**
 * 筛选界面数据
 * @param tag 区分不同的入口进入机构详情
 */
class HomeFtagmentListAdapter(data: List<ProductInfoListBean.ProductListBean>?, var context: Context, var tag:String) :
        BaseQuickAdapter<ProductInfoListBean.ProductListBean, BaseViewHolder>(R.layout.item_hot_product2, data) {


    override fun convert(holder: BaseViewHolder, item: ProductInfoListBean.ProductListBean) {


        val circleImageView = holder?.getView<CircleImageView>(R.id.profile_image) as ImageView
        ImageUtil.loadNoCache(
                context,
                circleImageView,
                item!!.product_logo,
                R.drawable.ic_error_close
        )
        holder?.getView<TextView>(R.id.product_name)?.text = item.product_name

        if (!TextUtils.isEmpty(item.activity_name)) {
            var tv = holder.getView<TextView>(R.id.hot)
           tv.visibility = View.VISIBLE
            tv.text = item.activity_name

            when (item!!.activity_name) {

//                "周期长" -> {
//                    tv.setBackgroundResource(R.drawable.zqc)}
//                "秒提现" -> tv.setBackgroundResource(R.drawable.mine_ed_2)
//                "额度高" -> tv.setBackgroundResource(R.drawable.mine_ed_3)
//                "新上线" -> tv.setBackgroundResource(R.drawable.hd)
//                "力荐" -> tv.setBackgroundResource(R.drawable.lj)

                "周期长" ->  tv.background= context.resources.getDrawable(R.drawable.zqc)
                "秒提现" -> tv.background= context.resources.getDrawable(R.drawable.mine_ed_2)
                "额度高" -> tv.background=context.resources.getDrawable(R.drawable.mine_ed_3)
                "新上线" -> tv.background=context.resources.getDrawable(R.drawable.hd)
                "力荐" -> tv.background=context.resources.getDrawable(R.drawable.lj)
                else -> tv.background=context.resources.getDrawable(R.drawable.hd)

            }
        } else {
            holder.getView<TextView>(R.id.hot).visibility = View.GONE
        }

        //额度
        holder?.getView<TextView>(R.id.ed_money_value1)?.text = item.quota_start_value
        holder?.getView<TextView>(R.id.ed_money_value2)?.text =""
        if (!TextUtils.isEmpty(item.quota_end_value)) {
            holder?.getView<TextView>(R.id.ed_money_value3)?.text = "-" + item.quota_end_value
        }else{
            holder?.getView<TextView>(R.id.ed_money_value3)?.text = ""
            holder?.getView<TextView>(R.id.ed_money_value2)?.text =  item.quota_start_unit
        }
        holder?.getView<TextView>(R.id.ed_money_value4)?.text = item.quota_end_unit
        holder?.getView<TextView>(R.id.ed)?.text = item.quota_name

        //利率
        holder?.getView<TextView>(R.id.qx)?.text = item.rate_unit+item.rate_name
        holder?.getView<TextView>(R.id.qx_time_value3)?.text = item.rate_value

        //下款时长
        holder?.getView<TextView>(R.id.yll)?.text = item.loan_name
        holder?.getView<TextView>(R.id.yll_value1)?.text = item.loan_time_value
        holder?.getView<TextView>(R.id.yll_value2)?.text = item.loan_time_unit

        //期限20天-36月
        if (!TextUtils.isEmpty(item.term_end_value)) {
            holder?.getView<TextView>(R.id.cycle)?.text = item.term_start_value + " " + item.term_start_unit + "-" + item.term_end_value + " " + item.term_end_unit
        }else{
            holder?.getView<TextView>(R.id.cycle)?.text = item.term_start_value + " " + item.term_start_unit
        }
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
                showText.setTextColor(context.resources.getColor(R.color.home_item_tag))
            }else{
                showText.setBackgroundResource(R.drawable.shape_home_tag)
                showText.setTextColor(context.resources.getColor(R.color.global_edit))
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

            context.startActivity<ProductInfoActivity>(ProductInfoActivity.PRODUCT_ID to item.product_id, ProductInfoActivity.PRODUCT_TYPE to tag)

        }

    }
}
