package com.dudubaika.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dudubaika.R;
import com.dudubaika.model.bean.FindInitBean;
import com.dudubaika.model.bean.HomeCreditCardBean;
import com.dudubaika.util.ImageUtil;

import java.util.List;

/**
 * 筛选标签展示(机构标签)
 */
public class FindTagAdapter extends BaseQuickAdapter<FindInitBean.TagListBean,BaseViewHolder> {

    private Context mContext;
    private Boolean isOpen = false;


    public FindTagAdapter(@Nullable List<FindInitBean.TagListBean> data, Context context) {
        super(R.layout.item_find_tag, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FindInitBean.TagListBean item) {
        helper.setText(R.id.tv_tag, item.getTitle());

    }
}
