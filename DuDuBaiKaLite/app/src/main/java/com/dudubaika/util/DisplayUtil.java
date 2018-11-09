package com.dudubaika.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dudubaika.dagger.module.GlideApp;

/**
 * DisplayUtil
 *
 * @author liu wei
 * @date 2018/4/26
 */
public class DisplayUtil {
    /**
     * 获取屏幕的宽度
     */
    public static int getDeviceWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.x;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getDeviceHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.y;
    }


    public static void setViewHeightByScreenWidth(final Activity activity, String imageUrl, final View view) {
        GlideApp.with(activity).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
                com.bumptech.glide.request.target.Target.SIZE_ORIGINAL) {

            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                    return;
                }
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double ratio = height * 1.0 / width;
                double viewHeight = ratio * DisplayUtil.getDeviceWidth(activity);

                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = (int) viewHeight;
                view.setLayoutParams(params);
            }
        });
    }
}
