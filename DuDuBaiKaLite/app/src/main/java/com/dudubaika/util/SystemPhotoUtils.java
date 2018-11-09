package com.dudubaika.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.dudubaika.R;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;


/**
 * Created by 胡俊焰 on 2017/3/30.
 */

public class SystemPhotoUtils {

    public SystemPhotoUtils() {

    }

  // 初始化GalleryFinal配置
    public static   void initGalleryFinal(Context context,int maxNum,boolean isCrop) {
        boolOpenCarmer(context);


        // 配置Theme
        ThemeConfig themeConfig = new ThemeConfig.Builder()
                .setTitleBarBgColor(context.getResources().getColor(R.color.red_home))    //标题栏背景颜色
                .setTitleBarTextColor(Color.WHITE)                  //标题栏字体颜色
//                .setTitleBarIconColor(context.getResources().getColor(R.color.red_home))                  //标题栏icon颜色 如果设置了标题栏icon，设置setTitleBarIconColor将无效
                .setFabNornalColor(context.getResources().getColor(R.color.red_home))                       //设置Floating按钮Normal状态颜色
                .setFabPressedColor(Color.parseColor("#FF6759B1"))                     //设置Floating按钮Pressed状态颜色
                .setCheckNornalColor(Color.TRANSPARENT)                   //选择框未选中的颜色
                .setCheckSelectedColor(context.getResources().getColor(R.color.red_home))                 //选择框选中的颜色
                .setIconBack(R.drawable.login_return)                   //设置返回按钮icon
                /*.setIconRotate(R.drawable.action_repeat)           //设置旋转icon
                .setIconCrop(R.drawable.action_crop)               //设置裁剪icon
                .setIconCamera(R.drawable.action_camera)   */        //设置相机icon
//                .setIconClear()                                   //设置清除按钮icon
//                .setIconFab()                                     //设置Floating按钮icon
//                .setEditPhotoBgTexture()                          //设置图片编辑页面图片margin外的背景
//                .setIconPreview()                                 //设置预览icon
//                .setPreviewBg()                                   //设置预览页背景
//                .setCropControlColor()                            //设置裁剪控制点和裁剪框颜色
//                .setIconFolderArrow()                             //设置标题栏文件夹下拉arrow图标
//                .setIconDelete()                                  //设置多选编辑页删除按钮icon
//                .setIconCheck()                                   //设置checkbox和文件夹已选icon
                .build();

        //配置ImageLoader
        ImageLoader imageLoader = new GlideImageLoader();   // 使用Glide加载图片
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
//                .setMutiSelect(false)         //配置是否多选(此方法已删除)
                .setMutiSelectMaxSize(maxNum)        //配置多选数量
                .setEnableEdit(false)            //开启编辑功能
                .setEnableCrop(isCrop)            //开启裁剪功能
                .setEnableRotate(true)          //开启选择功能
                .setEnableCamera(true)          //开启相机功能
//              .setCropWidth(int width)        //裁剪宽度
//              .setCropHeight(int height)      //裁剪高度
                .setCropSquare(true)            //裁剪正方形
//              .setSelected(List)              //添加已选列表,只是在列表中默认呗选中不会过滤图片
//              .setFilter(List list)           //添加图片过滤，也就是不在GalleryFinal中显示
                .setRotateReplaceSource(false)  //配置选择图片时是否替换原始图片，默认不替换
                .setCropReplaceSource(false)    //配置裁剪图片时是否替换原始图片，默认不替换
                .setForceCrop(true)             //启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .setForceCropEdit(false)         //在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
                .setEnablePreview(true)        //是否开启预览功能
                .build();


        //设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)

                .build();
        GalleryFinal.init(coreConfig);

    }

    //请求权限
    public static void boolOpenCarmer(Context context) {


        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)  //打开相机权限
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)   //可读
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)  //可写
                        != PackageManager.PERMISSION_GRANTED       ) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }

    }

}
