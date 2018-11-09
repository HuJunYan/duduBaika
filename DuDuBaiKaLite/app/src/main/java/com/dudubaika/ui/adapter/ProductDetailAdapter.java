package com.dudubaika.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dudubaika.R;
import com.dudubaika.model.bean.VerifyHomeDataBean;
import com.dudubaika.model.bean.VerifyProductDetailBean;
import com.dudubaika.ui.activity.VerifyWebActivity;

import java.util.List;

/**
 * Created by lenovo on 2018/3/28.
 */

public class ProductDetailAdapter extends BaseRecyclerViewAdapter<VerifyProductDetailBean.ProductDetailBean> {

    private List<VerifyProductDetailBean.ProductDetailBean> mList = null;
    private Context context;

    public ProductDetailAdapter(Activity context, List<VerifyProductDetailBean.ProductDetailBean> list) {
        super(context, list);
        this.mList = list;
        this.context = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ProductItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_detail, null));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }


    public class ProductItemHolder extends BaseRecyclerViewHolder {


        private final TextView product_detail_title;
        private final TextView product_detail_desc;
        private final ImageView product_detail_logo;

        public ProductItemHolder(View itemView) {
            super(itemView);
            product_detail_title = (TextView) itemView.findViewById(R.id.tv_product_detail_title);
            product_detail_desc = (TextView) itemView.findViewById(R.id.tv_product_detail_des);
            product_detail_logo = (ImageView) itemView.findViewById(R.id.iv_product_detail_logo);
        }

        @Override
        public void onBindViewHolder(final int position) {
            VerifyProductDetailBean.ProductDetailBean productDetailBean = mList.get(position);
            product_detail_title.setText(productDetailBean.getProduct_title());
            product_detail_desc.setText(productDetailBean.getProduct_des());
            Glide.with(context).load(productDetailBean.getProduct_url()).into(product_detail_logo);
        }

    }
}
