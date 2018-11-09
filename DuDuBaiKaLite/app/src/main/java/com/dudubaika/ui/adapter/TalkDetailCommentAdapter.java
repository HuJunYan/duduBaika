package com.dudubaika.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dudubaika.R;
import com.dudubaika.model.bean.CardMoneyListBean;
import com.dudubaika.model.bean.TalkDetailBean;
import com.dudubaika.util.ImageUtil;
import com.dudubaika.util.Utils;

import java.util.List;

/**
 * 帖子详情评论数据适配器
 */
public class TalkDetailCommentAdapter extends BaseQuickAdapter<TalkDetailBean.CommentListBean,BaseViewHolder> {

    private Context mContext;


    public TalkDetailCommentAdapter(@Nullable List<TalkDetailBean.CommentListBean> data, Context context) {
        super(R.layout.item_talk_detail_comment, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TalkDetailBean.CommentListBean item) {

        helper.setText(R.id.tv_phone, Utils.INSTANCE.encryptPhoneNum(item.getCus_name()));
        helper.setText(R.id.comment_content, item.getComment_content());
        helper.setText(R.id.comment_time, item.getComment_time());
        ImageUtil.INSTANCE.loadNoCache(mContext,(ImageView) helper.getView(R.id.item_iv),item.getCus_logo(),R.drawable.money_da);


    }
}
