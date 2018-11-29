package com.dudubaika.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dudubaika.R
import com.dudubaika.event.MyLoanEvent
import com.dudubaika.model.bean.MineLoanBean
import com.dudubaika.ui.activity.WebActivity
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.UserUtil
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import com.mcxtzhang.swipemenulib.SwipeMenuLayout
import de.hdodenhof.circleimageview.CircleImageView
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

class LoanAdapter(var context: Context, private var list:List<MineLoanBean.PaynoteListBean>, resId:Int): CommonAdapter<MineLoanBean.PaynoteListBean>(context,list,resId) {


    override fun convert(holder: ViewHolder?, item: MineLoanBean.PaynoteListBean?, posotion: Int) {

        if (null==item){
            return
        }

        if (!UserUtil.getUserIsWhite(context)){
            if (posotion==0){
                holder?.setVisible(R.id.is_show_gif,true)
                Glide.with( context ).asGif().load( R.drawable.gif_yh ).into(holder!!.getView<ImageView>( R.id.is_show_gif ))
            }
        }
        UserUtil.saveUserIsWhite(context,true)

        holder?.setOnClickListener(R.id.content, {
            if (!TextUtils.isEmpty(item!!.jump_url)){
                context.startActivity<WebActivity>(WebActivity.WEB_URL_KEY to item!!.jump_url)
            }else{
                context.startActivity<LoanDetailActivity>(LoanDetailActivity.PRODUCT_ID to item?.product_id)
            }
        })

        if (null !=item!!.product_logo) {
            ImageUtil.loadWithCache(context,holder!!.getView<CircleImageView>(R.id.product_logo),item!!.product_logo,R.drawable.product_logo_default)
        }
        holder?.setText(R.id.product_name,item?.product_name)
        holder?.setText(R.id.item_dh_key,item?.quota_title)
        holder?.setText(R.id.item_dh_money,item?.quota_value)
        holder?.setText(R.id.item_dh_date_key,item?.lastdate_title)
        holder?.setText(R.id.item_dh_date,item?.lastdate_value)
        when(item?.note_status){
            "1"-> {
                holder?.setImageResource(R.id.loan_status, R.drawable.loan_dh)
                holder?.setVisible(R.id.btnDelete,false)
                holder?.setVisible(R.id.btnred,true)
                holder?.getView<TextView>(R.id.item_dh_date)?.setTextColor(context.resources.getColor(R.color.global_txt_black4))
            }
            "2"-> {
                holder?.setVisible(R.id.loan_status,false)
                holder?.setVisible(R.id.btnDelete,true)
                holder?.setVisible(R.id.btnred,false)
                holder?.getView<TextView>(R.id.item_dh_date)?.setTextColor(context.resources.getColor(R.color.global_txt_black4))
            }

            "3"-> {
                holder?.setImageResource(R.id.loan_status, R.drawable.loan_yyq)
                holder?.setVisible(R.id.btnDelete,false)
                holder?.setVisible(R.id.btnred,true)
                holder?.setTextColor(R.id.item_dh_date,R.color.red)
                holder?.getView<TextView>(R.id.item_dh_date)?.setTextColor(context.resources.getColor(R.color.red))
            }
            else->{
                holder?.setImageResource(R.id.loan_status, R.drawable.loan_yh)
                holder?.setVisible(R.id.btnDelete,true)
                holder?.setVisible(R.id.btnred,false)
        }
        }
        holder!!.setOnClickListener(R.id.btnred, {
            //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
            EventBus.getDefault().post(MyLoanEvent(item?.product_id,"1"))//标记为已还
            (holder.convertView as SwipeMenuLayout).quickClose()
            mDatas.removeAt(posotion)
            notifyDataSetChanged()
        })
        holder!!.setOnClickListener(R.id.btnDelete, {
            //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
            EventBus.getDefault().post(MyLoanEvent(item?.product_id,"2"))//删除已还
            (holder.convertView as SwipeMenuLayout).quickClose()
            mDatas.removeAt(posotion)
            notifyDataSetChanged()
        })

        if (posotion==0 && holder?.getView<ImageView>(R.id.is_show_gif).visibility== View.VISIBLE ) {
            holder?.setOnTouchListener(R.id.content) { _, _ ->
                holder?.setVisible(R.id.is_show_gif, false)
                false
            }
        }

    }

}