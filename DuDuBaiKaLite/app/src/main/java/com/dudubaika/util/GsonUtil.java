package com.dudubaika.util;


import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GsonUtil {

    public static <T> T json2bean(String result, Class<T> clazz) {
        Gson gson = new Gson();
        T t = gson.fromJson(result, clazz);
        return t;
    }

    public static String bean2json(Object object) {
        String s = "";
        try {
            Gson gson = new Gson();
            s = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 读取读取assets资源下的数据
     *
     * @param context
     * @param fileName 文件名
     * @return
     */
    public static String getInfo(Context context, String fileName) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            InputStream in = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }
}
