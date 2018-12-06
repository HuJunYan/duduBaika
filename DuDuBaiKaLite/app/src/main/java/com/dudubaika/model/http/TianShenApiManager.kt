package com.dudubaika.model.http

import com.dudubaika.base.App
import com.dudubaika.log.okHttpLog.HttpLoggingInterceptorM
import com.dudubaika.log.okHttpLog.LogInterceptor
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.TianShenIdNumInfoBean
import com.dudubaika.util.Utils
import io.reactivex.Flowable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit


object TianShenApiManager {


    //正式
    private val HOST_PRO: String = "https://www.baidu.com/"
    //预发布
    private val HOST_PRE: String = "http://pre.dudueasy.dudujr.com/"
    //开发&测试
    private val HOST_DEV: String = "http://dev.duduapi.huaxick.com/"

    var mHost = ""

    val HTTP_LOG_TAG: String = "http"

    private lateinit var mApiService: TianShenApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    private fun initRetrofit(): Retrofit {

        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptorM(LogInterceptor(HTTP_LOG_TAG))
        interceptor.level = HttpLoggingInterceptorM.Level.BODY
        builder.addInterceptor(interceptor)

        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!Utils.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            // 有网络时, 不缓存, 最大保存时长为0
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=0")
                    .removeHeader("Pragma")
                    .build()
        }
        builder.addNetworkInterceptor(cacheInterceptor)
        builder.addInterceptor(cacheInterceptor)
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        //错误重连
        builder.retryOnConnectionFailure(true)
        val okHttpClient = builder.build()

        mHost = when (App.instance.mCurrentHost) {
            App.HOST.DEV -> HOST_DEV
            App.HOST.PRE -> HOST_PRE
            App.HOST.PRO -> HOST_PRO
        }

        return Retrofit.Builder().baseUrl(mHost)
                .client(okHttpClient)
                .addConverterFactory(CheckGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initServices(retrofit: Retrofit) {
        mApiService = retrofit.create(TianShenApiService::class.java)
    }

    //得到用户认证的信息
    fun getIdNumInfo(body: RequestBody): Flowable<MyHttpResponse<TianShenIdNumInfoBean>>
            = mApiService.getIdNumInfo(body)

    //保存用户认证的信息
    fun saveBackIdCardData(body: RequestBody, file: MultipartBody.Part): Flowable<MyHttpResponse<Any>>
            = mApiService.saveBackIdCardData(body, file)

    //图片上传
    fun uploadImage(body: RequestBody, file: MultipartBody.Part): Flowable<MyHttpResponse<Any>>
            = mApiService.uploadImage(body, file)

    //图片多张上传
    fun uploadBatchImage(body: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>
            = mApiService.uploadBatchImage(body, files)


    //保存用户身份认证信息
    fun saveIdNumInfo(body: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>> = mApiService.saveIdNumInfo(body, files)


    //校验身份一致性并保存
    fun checkFace(body: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>> = mApiService.checkFace(body, files)

 //测试
    fun testURL(): Flowable<MyHttpResponse<Any>> = mApiService.testURL()

}