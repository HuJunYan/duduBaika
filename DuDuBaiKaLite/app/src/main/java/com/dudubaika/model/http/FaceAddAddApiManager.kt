package com.dudubaika.model.http

import com.google.gson.GsonBuilder
import com.dudubaika.log.okHttpLog.HttpLoggingInterceptorM
import com.dudubaika.log.okHttpLog.LogInterceptor
import com.dudubaika.model.bean.IDCardBean
import com.dudubaika.util.Utils
import io.reactivex.Flowable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object FaceAddAddApiManager {


//    val HOST: String = "https://api.faceid.com/faceid/v1/"
    val HOST: String = "https://api.faceid.com/faceid/"

    val HTTP_LOG_TAG: String = "http"

    private lateinit var mApiService: FaceAddAddApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    private fun initRetrofit(): Retrofit {


        var builder = OkHttpClient.Builder()
        //打印日志 不区分是否是debug模式
        val interceptor = HttpLoggingInterceptorM(LogInterceptor(HTTP_LOG_TAG))
        interceptor.level = HttpLoggingInterceptorM.Level.BODY
        builder.addInterceptor(interceptor)
        val cachePath = Utils.getCachePath()
        var cacheFile = File(cachePath)
        val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!Utils.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (Utils.isNetworkConnected()) {
                val maxAge = 0
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build()
            } else {
                // 无网络时，设置超时为4周
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build()
            }
        }
        builder.addNetworkInterceptor(cacheInterceptor)
        builder.addInterceptor(cacheInterceptor)
        //设置缓存
        builder.cache(cache)
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        //错误重连
        builder.retryOnConnectionFailure(true)
        val okHttpClient = builder.build()
        return Retrofit.Builder().baseUrl(HOST)
                .client(okHttpClient)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initServices(retrofit: Retrofit) {
        mApiService = retrofit.create(FaceAddAddApiService::class.java)
    }

    private fun createGsonConverter(): GsonConverterFactory {
        val builder = GsonBuilder().serializeNulls()
        return GsonConverterFactory.create(builder.create())
    }

    //图片上传
    fun uploadImage(body: RequestBody): Flowable<IDCardBean> = mApiService.uploadImage(body)

    //图片上传得到身份证信息ocridcard
    fun ocrIdCard(body: RequestBody): Flowable<IDCardBean> = mApiService.ocrIdCard(body)

}