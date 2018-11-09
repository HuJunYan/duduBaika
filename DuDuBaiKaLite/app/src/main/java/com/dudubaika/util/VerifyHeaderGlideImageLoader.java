package com.dudubaika.util;

import android.content.Context;
import android.widget.ImageView;

import com.dudubaika.R;
import com.dudubaika.model.bean.VerifyHomeDataBean;
import com.youth.banner.loader.ImageLoader;


public class VerifyHeaderGlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        VerifyHomeDataBean.VerifyBannerBean bannerBean = (VerifyHomeDataBean.VerifyBannerBean) path;
        ImageUtil.INSTANCE.loadNoCache(context, imageView, bannerBean.getImg_url(), R.drawable.ic_banner_placeholder);
    }

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
