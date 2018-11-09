package com.dudubaika.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dudubaika.R;
import com.dudubaika.model.bean.CreditCardListBean;
import com.dudubaika.model.bean.HomeCreditCardBean;
import com.dudubaika.util.ImageUtil;

import java.util.List;

/**
 * 信用卡列表数据
 */
public class HomeCardListAdapter extends BaseQuickAdapter<HomeCreditCardBean.QualityListBean,BaseViewHolder> {

    private Context mContext;

    public HomeCardListAdapter(@Nullable List<HomeCreditCardBean.QualityListBean> data, Context context) {
        super(R.layout.item_bank_list, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper,HomeCreditCardBean.QualityListBean item) {

            ImageUtil.INSTANCE.loadWithCache(mContext, (ImageView) helper.getView(R.id.item_back_icon), item.getCredit_logo(), R.drawable.product_logo_default);
            helper.setText(R.id.item_bank_list_title, item.getCredit_name());
            helper.setText(R.id.item_bank_list_desc, item.getCredit_des());
            helper.setText(R.id.item_bank_list_now_pay, item.getCredit_apply() +" >");

    }
}
