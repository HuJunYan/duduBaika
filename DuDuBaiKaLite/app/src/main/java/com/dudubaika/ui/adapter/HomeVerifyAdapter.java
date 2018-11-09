package com.dudubaika.ui.adapter;

import android.app.Activity;
import android.content.Context;
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
 * Created by wang on 2017/12/29.
 */

public class HomeVerifyAdapter extends BaseRecyclerViewAdapter<VerifyHomeDataBean.ProductBean> {
    public static final int LOCAL_TYPE_TITLE = 100;
    public static final int LOCAL_TYPE_ITEM = 101;
    public static final int LOCAL_TYPE_HOT = 102;
    public Context context;

    public HomeVerifyAdapter(Activity context, List<VerifyHomeDataBean.ProductBean> list) {
        super(context, list);
        this.context=context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//        if (viewType == LOCAL_TYPE_TITLE) {
//            return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verify_home_title, parent, false));
//        } else if (viewType == LOCAL_TYPE_ITEM) {
//            return new NormalItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_collection, parent, false));
//
//        } else if (viewType == LOCAL_TYPE_HOT) {
//            return new HotItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_card, parent, false));
//        }
        return new HotItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_card, parent, false));
    }

    @Override
    public int getItemViewType(int position) {

       /* if (hasHeader() && position == 0) {
            return TYPE_HEAD;
        } else if (hasFooter() && position == getItemCount() - 1) {
            return TYPE_FOOT;
        } *//*else if (mList.get(getRealPosition(position)).getLocal_item_type() == LOCAL_TYPE_TITLE) {
            return LOCAL_TYPE_TITLE;
        } else if (mList.get(getRealPosition(position)).getLocal_item_type() == LOCAL_TYPE_HOT) {
            return LOCAL_TYPE_HOT;
        } *//*else {
//            return LOCAL_TYPE_ITEM;
        }*/
        return LOCAL_TYPE_HOT;
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
        private TextView tv_collection_hot_title;

        public HotItemHolder(View itemView) {
            super(itemView);
            tv_collection_hot_title = (TextView) itemView.findViewById(R.id.tv_collection_hot_title);
            logo = (ImageView) itemView.findViewById(R.id.iv_item_hot_product_logo);
            name_card = (TextView) itemView.findViewById(R.id.tv_item_hot_product_name);
            quoto_limit = (TextView) itemView.findViewById(R.id.tv_item_hot_product_quota_limit);
            product_desc = (TextView) itemView.findViewById(R.id.tv_item_hot_product_des);
            person_num = (TextView) itemView.findViewById(R.id.tv_hot_product_item_loan_time);
        }

        @Override
        public void onBindViewHolder(int position) {
            final VerifyHomeDataBean.ProductBean verifyItemBean = mList.get(position);

            Glide.with(context).load(verifyItemBean.getLogo_url()).into(logo);
            name_card.setText(verifyItemBean.getProduct_name());
            quoto_limit.setText(verifyItemBean.getQuota_limit());
            product_desc.setText(verifyItemBean.getProduct_des());
            person_num.setText(verifyItemBean.getApply_count());
//            tv_collection_hot_title.setText(mList.get(position).getArticle_title());
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, VerifyWebActivity.class);
//                    intent.putExtra(VerifyWebActivity.Companion.getVERIFY_WEB_URL_KEY(), verifyItemBean.getJump_url());
//                    intent.putExtra(VerifyWebActivity.Companion.getVERIFY_WEB_ARTICLE_ID(), verifyItemBean.getArticle_id());
//                    intent.putExtra(VerifyWebActivity.Companion.getARTICES_COLLECTED_STATUE(), verifyItemBean.is_collect());
//                    mActivity.startActivity(intent);
//                }
//            });
        }
    }


}
