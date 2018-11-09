package com.dudubaika.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.dudubaika.R;
import com.dudubaika.base.SimpleActivity;
import com.dudubaika.util.Config;
import com.dudubaika.util.FileUtils;
import com.dudubaika.util.StatusBarUtil;
import com.dudubaika.util.ToastUtil;
import com.dudubaika.util.UserUtil;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.functions.Consumer;

/**
 * 在线客服
 */

public class ServiceOnlineActivity extends SimpleActivity {
    public static final String SERVICE_ONLINE_KEY = "service_online_key"; //在线客服url key
    private WebView webview;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    String mCameraPhotoPath;
    Uri cameraUri;
    String compressPath = "";

    private static final int REQ_CAMERA = 1;
    private static final int REQ_CHOOSE = 2;
    private static final int REQ_FILE_UPLOAD_5 = 3;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
        Window window = getWindow();
        StatusBarUtil.setStatusBarBgWhite(this);
        int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
        systemUiVisibility ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    private void initWebView() {
        mUrl = UserUtil.INSTANCE.getServiceOnlineUrl(this);
        webview = (WebView) findViewById(R.id.sobot_view);
        webview.getSettings().setJavaScriptEnabled(true);//支持js脚本
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭webview中缓存
        webview.getSettings().setAllowFileAccess(true);//设置可以访问文件
        webview.getSettings().setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webview.getSettings().setSupportZoom(true);//支持缩放
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            webview.getSettings().setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webview.setWebChromeClient(new myWebClient());
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(mUrl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            ViewGroup parent = (ViewGroup) webview.getParent();
            if (parent != null) {
                parent.removeView(webview);
            }
            webview.removeAllViews();
            webview.destroy();
            webview = null;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri[] results = null;
        switch (requestCode) {
            case REQ_FILE_UPLOAD_5:

                if (mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
            case REQ_CAMERA:

                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                String imagePaths = sharedPreferences.getString("sobot_imagePaths", "");
                File cameraFile = new File(imagePaths);
                if (cameraFile.exists()) {
                    Bitmap bitmapBefore = compressBitmap(cameraFile.getAbsolutePath(), getApplicationContext());
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(cameraFile.getAbsolutePath());
                        bitmapBefore.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        if (mUploadMessage != null) {
                            mUploadMessage.onReceiveValue(cameraUri);
                        }
                        if (mFilePathCallback != null) {
                            mFilePathCallback.onReceiveValue(new Uri[]{cameraUri});
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(ServiceOnlineActivity.this,
                            "请重新选择或拍摄", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 10);
                    toast.show();
                    cancelCallback();
                }
                break;
            case REQ_CHOOSE:

                if (null != data && null != data.getData()) {
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(afterChosePic(data));
                    }
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(new Uri[]{afterChosePic(data)});
                    }
                    mUploadMessage = null;
                } else {
                    cancelCallback();
                }
                break;
        }
    }

    private void cancelCallback() {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(Uri.EMPTY);
        }
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(new Uri[]{});
        }
        mUploadMessage = null;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_online;
    }

    @Override
    protected void initView() {
//        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.tb_online));
        findViewById(R.id.iv_service_online_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backActivity();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public class myWebClient extends WebChromeClient {
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android 3.0+
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            if (mUploadMessage != null)
                return;
            mUploadMessage = uploadMsg;
            selectImage();
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = null;
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android 5.0
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {

            mFilePathCallback = filePathCallback;
            selectImage();
            return true;
        }
    }

    protected final void selectImage() {
        if (!FileUtils.existSDCard())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        String[] selectPicTypeStr = {"拍照", "相册"};
        builder.setItems(selectPicTypeStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // 相机拍摄
                            case 0:
                                openCamera();
                                break;
                            // 手机相册
                            case 1:
                                chosePic();
                                break;
                            default:
                                break;
                        }
                        compressPath = Config.IMAGE_CACHE_DIR;
                        new File(compressPath).mkdirs();
                        compressPath = compressPath + "compress.jpg";
                    }
                });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(Uri.EMPTY);
                }
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(new Uri[]{});
                }
                mUploadMessage = null;
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void openCamera() {
        RxPermissions rxPermissions = new RxPermissions(ServiceOnlineActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String imagePaths = Config.SD_PATH + "temp/" + (System.currentTimeMillis() + ".jpg");
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("sobot_imagePaths", imagePaths).commit();
                    File vFile = new File(imagePaths);
                    vFile.getParentFile().mkdirs();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA, vFile.getAbsolutePath());
                        cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } else {
                        cameraUri = Uri.fromFile(vFile);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                    startActivityForResult(intent, REQ_CAMERA);
                } else {
                    ToastUtil.showToast(ServiceOnlineActivity.this, "请去设置开启照相机权限");
                }

            }
        });
    }

    private void chosePic() {

        RxPermissions rxPermissions = new RxPermissions(ServiceOnlineActivity.this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                FileUtils.deleteFile(compressPath);
                Intent intentPic = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentPic, REQ_CHOOSE);
            }
        });
    }

    /* 把图片插入到系统相册 现在没有调用 */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private Uri afterChosePic(Intent data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),
                data.getData(), proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        if (cursor == null) {
            return null;
        }
        try {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            if (path != null) {
                File newFile = FileUtils.compressFile(path, compressPath);
                return Uri.fromFile(newFile);
            } else {
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private Bitmap compressBitmap(String filePath, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置后decode图片不会返回一个bitmap对象，但是会将图片的信息封装到Options中
        BitmapFactory.decodeFile(filePath, options);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int sampleSize = computeSampleSize(options, width, width * height);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    private int computeSampleSize(BitmapFactory.Options options,
                                  int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
