package com.dudubaika.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dudubaika.R;
import com.dudubaika.model.bean.VerifyHomeDataBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/28.
 */

public class VerifyAdapter extends BaseRecyclerViewAdapter<VerifyHomeDataBean.ProductBean> {
    private Context context;

    public VerifyAdapter(Activity context, List<VerifyHomeDataBean.ProductBean> list) {
        super(context, list);
        this.context = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new HotItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_card, parent, false));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    public class HotItemHolder extends BaseRecyclerViewHolder {

        private final ImageView logo;
        private final TextView name_card;
        private final TextView quoto_limit;
        private final TextView product_desc;
        private final TextView person_num;
        private final TextView now_apply;
        private TextView tv_collection_hot_title;

        public HotItemHolder(View itemView) {
            super(itemView);
            tv_collection_hot_title = (TextView) itemView.findViewById(R.id.tv_collection_hot_title);
            logo = (ImageView) itemView.findViewById(R.id.iv_item_hot_product_logo);
            name_card = (TextView) itemView.findViewById(R.id.tv_item_hot_product_name);
            quoto_limit = (TextView) itemView.findViewById(R.id.tv_item_hot_product_quota_limit);
            product_desc = (TextView) itemView.findViewById(R.id.tv_item_hot_product_des);
            person_num = (TextView) itemView.findViewById(R.id.tv_hot_product_item_loan_time);
            now_apply = (TextView) itemView.findViewById(R.id.tv_hot_product_item_now_apply);
        }

        @Override
        public void onBindViewHolder(final int position) {
            final VerifyHomeDataBean.ProductBean verifyItemBean = mList.get(position);

            Glide.with(context).load(verifyItemBean.getLogo_url()).into(logo);
            name_card.setText(verifyItemBean.getProduct_name());
            quoto_limit.setText(verifyItemBean.getQuota_limit());
            product_desc.setText(verifyItemBean.getProduct_des());
            person_num.setText(verifyItemBean.getApply_count() + "人查看");
            now_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }


    }

}
