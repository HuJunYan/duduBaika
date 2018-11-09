package com.dudubaika.model.http;

import com.dudubaika.log.LogUtil;
import com.dudubaika.model.bean.MyHttpResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;


public class CheckGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson mGson;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    CheckGsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.mGson = new Gson();
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = "";
        try {
            LogUtil.d("http", "开始解析json");
            response = value.string();
            MyHttpResponse httpStatus = mGson.fromJson(response, MyHttpResponse.class);
            if (httpStatus.getCode() != ApiCode.SUCCESS) {
                value.close();
                LogUtil.d("http", "业务状态码不等于0抛出ApiException");
                throw new ApiException(response);
            }
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);
            JsonReader jsonReader = mGson.newJsonReader(reader);
            LogUtil.d("http", "解析json正常结束");
            return adapter.read(jsonReader);
        } catch (Exception ignored) {
            LogUtil.d("http", "解析json异常"+response.toString());
            value.close();
            throw new ApiException(response);
        } finally {
            value.close();
        }
    }


}
