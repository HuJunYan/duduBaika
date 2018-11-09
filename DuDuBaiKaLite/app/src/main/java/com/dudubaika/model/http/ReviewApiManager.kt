package com.dudubaika.model.http

import com.dudubaika.log.okHttpLog.HttpLoggingInterceptorM
import com.dudubaika.log.okHttpLog.LogInterceptor
import com.dudubaika.model.bean.*
import com.dudubaika.util.Utils
import io.reactivex.Flowable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ReviewApiManager {

    val HOST: String = "http://dev.xgcard.tianshenjr.com/"

    private val HTTP_LOG_TAG: String = "http"

    private lateinit var mApiService: ReviewApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    private fun initRetrofit(): Retrofit {

        val builder = OkHttpClient.Builder()
        //打印日志 不区分是否是debug模式
        val interceptor = HttpLoggingInterceptorM(LogInterceptor(HTTP_LOG_TAG))
        interceptor.level = HttpLoggingInterceptorM.Level.BODY
//        builder.addInterceptor(interceptor)

        builder.addInterceptor(ReviewFakeApiInterceptor())

        val cachePath = Utils.getCachePath()
        val cacheFile = File(cachePath)
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
//        val apikey = Interceptor { chain ->
//            var request = chain.request()
//            request = request.newBuilder()
//                    .addHeader("apikey", "header")
//                    .build()
//            chain.proceed(request)
//        }
//        //设置统一的请求头部参数
//        builder.addInterceptor(apikey)
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

//        return Retrofit.Builder().baseUrl(HOST)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()

//        统一校验code码
        return Retrofit.Builder().baseUrl(HOST)
                .client(okHttpClient)
                .addConverterFactory(CheckGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initServices(retrofit: Retrofit) {
        mApiService = retrofit.create(ReviewApiService::class.java)
    }

    //我的
    fun getMine(body: RequestBody): Flowable<MyHttpResponse<MineBean>> = mApiService.getMine(body)

    //登录
    fun login(body: RequestBody): Flowable<MyHttpResponse<LoginBean>> = mApiService.login(body)

    //退出登录
    fun logOut(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.logOut(body)

    //得到验证码
    fun getVeryCode(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.getVeryCode(body)

    //得到首页文章数据(审核)
    fun getHomeArticle(body: RequestBody): Flowable<MyHttpResponse<VerifyHomeDataBean>> = mApiService.getHomeArticle(body)

    //得到我的收藏数据
    fun getMycollection(body: RequestBody): Flowable<MyHttpResponse<VerifyHomeDataBean>> = mApiService.getMycollection(body)

    //收藏文章
    fun collection(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.collection(body)

    //取消收藏文章
    fun unCollection(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.unCollection(body)



    fun getVerifyView(body: RequestBody) : Flowable<MyHttpResponse<VerifyHomeDataBean>> = mApiService.getVerifyView(body)

    fun getTransficCard(body: RequestBody) :Flowable<MyHttpResponse<VerifyProductDetailBean>> = mApiService.getTranficCard(body)
}