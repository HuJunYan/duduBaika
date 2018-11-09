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
import com.dudubaika.util.ImageUtil;
import com.dudubaika.util.Utils;

import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 用户选择图片适配器
 */
public class UserImgAdapter extends BaseQuickAdapter<PhotoInfo,BaseViewHolder> {

    private Context mContext;

    public UserImgAdapter(@Nullable List<PhotoInfo> data, Context context) {
        super(R.layout.item_user_chose_img, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PhotoInfo item) {

        ImageUtil.INSTANCE.loadNoCache(mContext,(ImageView) helper.getView(R.id.item_user_chose_img),item.getPhotoPath(),R.drawable.money_da);
        helper.addOnClickListener(R.id.item_delete);

    }
}
