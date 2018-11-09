package com.dudubaika.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dudubaika.R;
import com.dudubaika.model.bean.HomeCreditCardBean;
import com.dudubaika.util.ImageUtil;

import java.util.List;

/**
 * 信用卡首页数据
 */
public class HomeCardBankListAdapter extends BaseQuickAdapter<HomeCreditCardBean.BankListBean,BaseViewHolder> {

    private Context mContext;
    private Boolean isOpen = false;


    public HomeCardBankListAdapter(@Nullable List<HomeCreditCardBean.BankListBean> data, Context context) {
        super(R.layout.item_home_card_list, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeCreditCardBean.BankListBean item) {
        //最多显示8个
        ImageUtil.INSTANCE.loadWithCache(mContext, (ImageView) helper.getView(R.id.item_circle_bank_icon), item.getBank_logo(), R.drawable.product_logo_default);
        helper.setText(R.id.item_bank_name, item.getBank_name());

    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
        notifyDataSetChanged();
    }
}
