package com.dudubaika.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;

import com.dudubaika.ui.activity.WebActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static StringUtils instance;
    private static Context mContext;
    //利用正则识别

    private StringUtils (){

    }

    public static StringUtils getInstance(Context context){
        mContext = context;
       synchronized (StringUtils.class) {
           if (null == instance) {
               instance = new StringUtils();
           }
       }
        return instance;
    }

    public CharSequence checkAutoLink(String content) {

        String url = "百度 https://www.baidu.com腾讯 http://www.qq.com/，淘宝 www.taobao.com/，淘宝2www.taobao.com/淘宝3www.taobao.com/啦啦";//此处测试，就不用参数了

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

//        Pattern pattern = Pattern.compile("([\\w]+?://[\\w\\\\x80-\\\\xff\\#$%&~/.\\-;:=,?@\\[\\]]*)");
//        Pattern pattern = Pattern.compile("((www|http|https|WWW|HTTP|HTTPS)://|.)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{3,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
//        Pattern pattern = Pattern.compile("((http|https|HTTP|HTTPS)://|.)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
        Pattern pattern = Pattern.compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)|([a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)");

        Matcher matcher = pattern.matcher(spannableStringBuilder);

        while (matcher.find()) {

            setClickableSpan(spannableStringBuilder, matcher);

        }
//        Pattern pattern2 = Pattern.compile("((www|ftp)\\.[\\w\\\\x80-\\\\xff\\#$%&~/.\\-;:=,?@\\[\\]+]*)");
//        Pattern pattern2 = Pattern.compile("((www|http|https|WWW|HTTP|HTTPS)://|.)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{3,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
//        Pattern pattern2 = Pattern.compile("((http|https|HTTP|HTTPS)://|.)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?");
          Pattern pattern2 =  Pattern.compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)|([a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>,]*)?)");

        matcher.reset();

        matcher = pattern2.matcher(spannableStringBuilder);

        while (matcher.find()) {

            setClickableSpan(spannableStringBuilder, matcher);

        }

        return spannableStringBuilder;

    }


//给符合的设置自定义点击事件

    private void setClickableSpan (final SpannableStringBuilder clickableHtmlBuilder, final Matcher matcher) {

        int start = matcher.start();

        int end = matcher.end();

        final String url = matcher.group();

        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View view) {
                try {
                    //需要在子线程中处理的逻辑
                    Uri content_url;
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    if(url.startsWith("www.")){
                        content_url = Uri.parse("http://"+url);
                    }else {
                        content_url = Uri.parse(url);
                    }
                    //splitflowurl为分流地址
                    intent.setData(content_url);
                    if (!hasPreferredApplication(mContext,intent)){
                        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    }
                    mContext.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }

            }

        };

        clickableHtmlBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

    }


//    在TextView处不必设置autolink，只设置

    //如保持系统风
   /* setAutoLinkTextColor(getResources().getColor(R.color.link_color));

    setMovementMethod(LinkMovementMethod.getInstance()); */
//判断系统是否设置了默认浏览器
 public boolean hasPreferredApplication(Context context, Intent intent) {
     PackageManager pm = context.getPackageManager();
     ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
     return !"android".equals(info.activityInfo.packageName); }







}
