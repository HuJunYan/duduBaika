package com.dudubaika.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dudubaika.R;
import com.dudubaika.model.bean.CardMoneyListBean;
import com.dudubaika.model.bean.FindInitBean;
import com.dudubaika.ui.activity.ImageLookActivity;
import com.dudubaika.ui.view.MyGridLayoutManager;
import com.dudubaika.util.ImageUtil;
import com.dudubaika.util.RegexUtil;
import com.dudubaika.util.StringUtils;
import com.dudubaika.util.Utils;

import java.util.List;

/**
 * 数据适配器
 */
public class TalkHomeListAdapter extends BaseQuickAdapter<CardMoneyListBean.DiscussListBean,BaseViewHolder> {

    private Context mContext;

    public TalkHomeListAdapter(@Nullable List<CardMoneyListBean.DiscussListBean> data, Context context) {
        super(R.layout.item_card_talk_list, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CardMoneyListBean.DiscussListBean item) {


        if (RegexUtil.IsTelephone(item.getDiscuss_name())) {
            helper.setText(R.id.tv_phone, Utils.INSTANCE.encryptPhoneNum(item.getDiscuss_name()));
        }else {
            helper.setText(R.id.tv_phone, item.getDiscuss_name());
        }
        //tvContent.text = StringUtils.getInstance(mActivity).checkAutoLink(mBean?.discuss_content)
        //                    tvContent.movementMethod = LinkMovementMethod.getInstance()
        //                    tvContent.autoLinkMask = 0
        helper.addOnClickListener(R.id.rl_layout).addOnClickListener(R.id.tv_content);
        TextView tv = helper.getView(R.id.tv_content);
        tv.setText(StringUtils.getInstance(mContext).checkAutoLink(item.getDiscuss_title()));
        tv.setMovementMethod( LinkMovementMethod.getInstance());
        tv.setAutoLinkMask(0);

//        ImageUtil.INSTANCE.loadNoCache(mContext,(ImageView) helper.getView(R.id.item_iv),item.getDiscuss_logo(),R.drawable.money_da);
        ImageUtil.INSTANCE.loadWithCache(mContext,(ImageView) helper.getView(R.id.item_iv),item.getDiscuss_logo(),R.drawable.money_da);

        GridView gridview = helper.getView(R.id.gridview);
        if (null !=item.getDiscuss_logo_list() && item.getDiscuss_logo_list().size()>0){
            gridview.setVisibility(View.VISIBLE);
            gridview.setAdapter(new NoScrollGridAdapter(mContext, item.getDiscuss_logo_list(),item.getDiscuss_logo_url()));


        }else {
            gridview.setVisibility(View.GONE);
        }


    }
}
