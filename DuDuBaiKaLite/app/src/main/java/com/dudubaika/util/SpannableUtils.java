package com.dudubaika.util;

import android.graphics.Color;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/2.
 */

public class SpannableUtils {
    private SpannableUtils() {
    }

    private static int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;

    /**
     * 设置去web页面的spannable样式
     *
     * @param textView
     * @param text
     * @param start
     * @param end
     * @param spanList
     * @param spanTextColor
     */
    public static void setWebSpannableString(TextView textView, String text, String start, String end, List<CharacterStyle> spanList, int spanTextColor) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || spanList == null || spanList.size() == 0) {
            return;
        }
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        int startIndex;
        int endIndex = 0;
        int count = 0;
        while ((startIndex = text.indexOf(start, endIndex)) != -1) {
            if (count == spanList.size()) {
                break;
            }
            endIndex = text.indexOf(end, endIndex) + 1;
            ss.setSpan(spanList.get(count), startIndex, endIndex, flag);
            setTransparentBackgoundSpan(ss, startIndex, endIndex);
            setSpanTextColor(ss, startIndex, endIndex, spanTextColor);
            count++;
        }
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static void setTransparentBackgoundSpan(SpannableStringBuilder ss, int start, int end) {
        ss.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end, flag);
    }

    private static void setSpanTextColor(SpannableStringBuilder ss, int start, int end, int color) {
        ss.setSpan(new ForegroundColorSpan(color), start, end, flag);
    }

    /**
     * 设置制定范围内的文字颜色
     *
     * @param textView
     * @param text
     * @param start
     * @param end
     * @param spanTextColor
     */
    public static void setSpannableStringColor(TextView textView, String text, String start, String end, int spanTextColor) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            return;
        }
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        int startIndex = text.indexOf(start);
        int endIndex = text.indexOf(end) + 1;
        setTransparentBackgoundSpan(ss, startIndex, endIndex);
        setSpanTextColor(ss, startIndex, endIndex, spanTextColor);
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    /**
     * 给edittext设置过滤器 过滤emoji
     *
     * @param et
     */
    public static void setEmojiFilter(EditText et) {
        InputFilter emojiFilter = new InputFilter() {
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) {
                    return "";
                }
                return null;
            }
        };
        et.setFilters(new InputFilter[]{emojiFilter});
    }

    public static String filterCharToNormal(String oldString) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = oldString.length();
        for (int i = 0; i < length; i++) {//遍历传入的String的所有字符
            char codePoint = oldString.charAt(i);
            if (!isEmojiCharacter(codePoint)) {//小写字母区间
                stringBuilder.append(codePoint);
            } else {//如果当前字符为非常规字符,则忽略掉该字符
            }
        }
        return stringBuilder.toString();
    }
    private static boolean isEmojiCharacter(int codePoint) {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
                || codePoint == 0x205F //
                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
                /* 标点符号占用区域 */
                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
                || codePoint >= 0x10000; // Plane在第二平面以上的，char都不可以存，全部都转
    }
}
