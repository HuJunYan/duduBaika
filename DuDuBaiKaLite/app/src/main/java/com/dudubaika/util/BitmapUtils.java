package com.dudubaika.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import com.dudubaika.log.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class BitmapUtils {
    /**
     * 图片处理：裁剪.
     */
    public static final int CUTIMG = 0;

    /**
     * 图片处理：缩放.
     */
    public static final int SCALEIMG = 1;

    /**
     * 图片处理：不处理.
     */
    public static final int ORIGINALIMG = 2;

    public static final String IMAGE_UNSPECIFIED = "image/*";

    /**
     * 个人头像裁剪图片方法实现
     *
     * @param uri
     *//*
    public static void startPhotoZoom(Activity activity, Uri uri, int width, int hight) {
		*//*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 *//*
        Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		if(width+1 == hight){
			intent.putExtra("aspectX", 100);
			intent.putExtra("aspectY", 101);
		}else{
			intent.putExtra("aspectX", 300);
			intent.putExtra("aspectY", 200);
		}
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", hight);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, 102);
	}
*/

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    public static Bitmap setPicToView(Intent picdata) {
        if (picdata != null) {
            Bitmap photo = picdata.getParcelableExtra("data");
            Bitmap bitmap = BitmapUtils.getRoundedCornerBitmap(photo);
            return bitmap;
        }
        return null;
    }

    /**
     * 保存裁剪之后的图片数据
     */
    public static Bitmap setBackPicToView(Intent picdata) {
        if (picdata != null) {
            Bitmap photo = picdata.getParcelableExtra("data");
            return photo;
        }
        return null;
    }

    /**
     * bitmap---->byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap对象转换Drawable对象.
     *
     * @param bitmap 要转化的Bitmap对象
     * @return Drawable 转化完成的Drawable对象
     */
    public static Drawable bitmapToDrawable(Activity activity, Bitmap bitmap) {
        BitmapDrawable mBitmapDrawable = null;
        try {
            if (bitmap == null) {
                return null;
            }
            mBitmapDrawable = new BitmapDrawable(activity.getResources(), bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmapDrawable;
    }

    /**
     * 切圆形
     *
     * @param bitmap 已经切好的方形头像
     * @return 去掉矩形边角的圆形bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() / 2 : bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将bitmap变为byte[] 数组
     *
     * @param
     * @return
     */
    public static byte[] byteToBitmap(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 保存图片到本地
     */
    public static void saveBitmap(Bitmap bm, String pathDir, String fileName) {
        File dirFile = new File(pathDir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File f = new File(pathDir, fileName);
        if (f.exists()) {
            LogUtil.d("ret", "filefullname = " + pathDir + fileName);
            f.delete();
            if (f.exists()) {
                LogUtil.d("ret", "filefullname = " + pathDir + fileName + " delete failed");
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param urlString
     * @param filename
     * @throws Exception
     */
    public static void DownloadImage(final String urlString, final String filename) throws Exception {
        new Thread() {
            public void run() {
                try {
                    // 构造URL
                    URL url = new URL(urlString);
                    Log.d("ret", "urlString = " + urlString);
                    // 打开连接
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    // 输入流
                    InputStream is = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流
                    OutputStream os = new FileOutputStream(filename);
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 读取本地图片
     *
     * @param pathString
     * @return
     */
    public static Bitmap getSDCardBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return bitmap;
    }

    /**
     * 调用系统的照相机
     */
    public static Uri getoSystemCamera(Context context, int photoCode) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(Media.TITLE, filename);
        Uri mCameraUri = context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
        ((Activity) context).startActivityForResult(intent, photoCode);

        return mCameraUri;
    }

    /**
     * 从相册里面获取图片
     */
    public static void getoSystemPhoto(Context context, int photoCode) {
        Intent intent_pick = new Intent(Intent.ACTION_PICK, null);
        intent_pick.setDataAndType(Media.EXTERNAL_CONTENT_URI, BitmapUtils.IMAGE_UNSPECIFIED);
        ((Activity) context)
                .startActivityForResult(intent_pick, photoCode);
    }


    /**
     * 生成指定宽高的图片
     *
     * @param source
     * @param width
     * @param height
     */
    public static Bitmap generateBitmapUseWidthAndHeight(Bitmap source, int width, int height) {
        Bitmap bitmap;
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        Matrix matrix = new Matrix();
        float scaleX = width / (sourceWidth * 1.0f);
        float scaleY = height / (sourceHeight * 1.0f);
        matrix.postScale(scaleX, scaleY);
        bitmap = Bitmap.createBitmap(source, 0, 0, sourceWidth, sourceHeight, matrix, false);
        bitmap.recycle();
        return bitmap;
    }

    /**
     * 设置bitmap 宽高 大于800 则限制在800以内 不大于则不处理
     *
     * @param source
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap source) {
        Bitmap bitmap;
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float scale = 1.0f;
        int screenWidth = 800;
        int screenHeight = 800;
        if (newWidth > screenWidth || newHeight > screenHeight) {
            //格式化宽和高
            if (newWidth > screenWidth) {
                scale = screenWidth / (newWidth * 1.0f);
                newHeight = (int) (newHeight * screenWidth / newWidth);
            }
            if (newHeight > screenHeight) {
                scale = scale * screenHeight / (newHeight * 1.0f);
            }
        }
        if (scale == 1.0f) {
            return source;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
        source.recycle();
        return bitmap;
    }
}
