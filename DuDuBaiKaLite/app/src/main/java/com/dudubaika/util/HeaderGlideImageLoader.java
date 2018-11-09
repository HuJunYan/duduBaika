package com.dudubaika.util;

import android.content.Context;
import android.widget.ImageView;

import com.dudubaika.R;
import com.dudubaika.model.bean.BannerListBean;
import com.youth.banner.loader.ImageLoader;


public class HeaderGlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        String url = (String) path;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageUtil.INSTANCE.loadNoCache(context, imageView, url, R.drawable.ic_banner_placeholder);
    }

//    @Override
//    public ImageView createImageView(Context context) {
    //圆角
//        return new RoundAngleImageView(context);
//    }
}
