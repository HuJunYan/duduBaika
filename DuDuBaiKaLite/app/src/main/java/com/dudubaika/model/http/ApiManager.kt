package com.dudubaika.model.http

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.dudubaika.base.App
import com.dudubaika.log.okHttpLog.HttpLoggingInterceptorM
import com.dudubaika.log.okHttpLog.LogInterceptor
import com.dudubaika.model.bean.*
import com.dudubaika.util.Utils
import io.reactivex.Flowable
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Part
import java.io.File
import java.net.Proxy
import java.util.concurrent.TimeUnit

object ApiManager {

    //正式
    private val HOST_PRO: String = "http://dudueasy.dudujr.com/"
//    private val HOST_PRO: String = "http://linshidudu.dudujr.com/"
    //预发布
    private val HOST_PRE: String = "http://pre.api.51jiuqi.com/"
//    private val HOST_PRE: String = "http://pre.dudueasy.dudujr.com/"
    //开发&测试
//    private val HOST_DEV: String = " http://dev.dudueasy.dudujr.com"
//    private val HOST_DEV: String = "http://dev.duduapi.huaxick.com"
    private val HOST_DEV: String = "http://dev.api.51jiuqi.com/"

    var mHost = ""

    val HTTP_LOG_TAG: String = "http"

    private lateinit var mApiService: ApiService

    init {
        val retrofit = initRetrofit()
        initServices(retrofit)
    }

    private fun initRetrofit(): Retrofit {

        val builder = OkHttpClient.Builder()
        //打印日志 不区分是否是debug模式
        val interceptor = HttpLoggingInterceptorM(LogInterceptor(HTTP_LOG_TAG))
        interceptor.level = HttpLoggingInterceptorM.Level.BODY
        builder.addInterceptor(interceptor)
        builder.addNetworkInterceptor(StethoInterceptor())

//        builder.addInterceptor(FakeApiInterceptor())

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
        builder.connectTimeout(600, TimeUnit.SECONDS)
        builder.readTimeout(600, TimeUnit.SECONDS)
        builder.writeTimeout(600, TimeUnit.SECONDS)
        //无代理模式
        builder.proxy(Proxy.NO_PROXY)
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
        mApiService = retrofit.create(ApiService::class.java)
    }

    fun loadOrganizationRepos(organizationName: String, reposType: String, page: String, per_page: String):
            Flowable<MutableList<Repository>> = mApiService.getOrganizationRepos(organizationName, reposType, page, per_page)

    //得到广告条
    fun getBannerList(body: RequestBody): Flowable<MyHttpResponse<BannerListBean>>
            = mApiService.getBannerList(body)

    //得到广告条
    fun getProductList(body: RequestBody): Flowable<MyHttpResponse<ProductListBean>>
            = mApiService.getProductList(body)

    //得到用户的状态
    fun getUserAuthResult(body: RequestBody): Flowable<MyHttpResponse<UserAuthResultBean>>
            = mApiService.getUserAuthResult(body)

    //登录
    fun login(body: RequestBody): Flowable<MyHttpResponse<LoginBean>>
            = mApiService.login(body)

    //获取验证码
    fun getVerifyCode(body: RequestBody): Flowable<MyHttpResponse<Any>>
            = mApiService.getVerifyCode(body)

    //注册
    fun reregist(body: RequestBody): Flowable<MyHttpResponse<RegistBean>>
            = mApiService.reregist(body)

    //获取用户个人信息中的职业列表
    fun getUserInfo(body: RequestBody): Flowable<MyHttpResponse<UserInfoBean>> = mApiService.getUserInfo(body)

    //保存用户个人信息
    fun saveUserInfo(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.saveUserInfo(body)

    //获取区域
    fun getCounty(body: RequestBody): Flowable<MyHttpResponse<CountyBean>> = mApiService.getCounty(body)

    //得到省
    fun getProvince(body: RequestBody): Flowable<MyHttpResponse<ProvinceBean>> = mApiService.getProvince(body)

    //得到城市
    fun getCity(body: RequestBody): Flowable<MyHttpResponse<CityBean>> = mApiService.getCity(body)

    //获取紧急联系人信息
    fun getExtroContacts(body: RequestBody): Flowable<MyHttpResponse<ExtroContactsBean>> = mApiService.getExtroContacts(body)

    //保存紧急联系人信息
    fun saveExtroContacts(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.saveExtroContacts(body)

    //保存所有联系人
    fun saveContacts(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.saveContacts(body)

    //设置密码
    fun setPwd(body: RequestBody): Flowable<MyHttpResponse<SetPwdBean>> = mApiService.setPwd(body)

    //保存手机信息 上传短信等
    fun savePhoneInfo(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.savePhoneInfo(body)


    //获取上次短信上传时间
    fun getLastSmsTime(body: RequestBody): Flowable<MyHttpResponse<LastSmsTimeBean>> = mApiService.getLastSmsTime(body)

    //信用提交
    fun creditApply(body: RequestBody): Flowable<MyHttpResponse<CreidtApplyBean>> = mApiService.creditApply(body)

    //信用提交2
    fun creditApply2(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.creditApply2(body)

    //信用评估
    fun creditAssess(body: RequestBody): Flowable<MyHttpResponse<CreditAssessBean>> = mApiService.creditAssess(body)

    //重置密码
    fun resetPwd(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.resetPwd(body)

    //退出登录
    fun loginOut(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.loginOut(body)

    //退出登录
    fun upLoadOpion(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.upLoadOpion(body)

    //贷款推荐
    fun productRecommend(body: RequestBody): Flowable<MyHttpResponse<ProductRecommentBean>> = mApiService.productRecommend(body)

    //贷款推荐--（产品详情）
    fun nowLoan(body: RequestBody): Flowable<MyHttpResponse<ProductInfoBean>> = mApiService.nowLoan(body)

    //点击立即申请
    fun nowApply(body: RequestBody): Flowable<MyHttpResponse<NowApplyBean>> = mApiService.nowApply(body)

    //检查更新
    fun checkUpgrade(body: RequestBody): Flowable<MyHttpResponse<UpgradeBean>> = mApiService.checkUpgrade(body)

    //检查更新
    fun getHomeTopInfo(body: RequestBody): Flowable<MyHttpResponse<HomeTopInfo>> = mApiService.getHomeTopInfo(body)

    //得到产品列表
    fun getProductInfoList(body: RequestBody): Flowable<MyHttpResponse<ProductInfoListBean>> = mApiService.getProductInfoList(body)

    //埋点
    fun builePoint(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.builePoint(body)

    //用户认证状态
    fun getAuthStatus(body: RequestBody): Flowable<MyHttpResponse<AuthStatus>> = mApiService.getAuthStatus(body)




    //更改魔蝎状态
    fun changeMoxieStatus(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.changeMoxieStatus(body)
    //设置极光消息为已读
    fun setMsgRead(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.setMsgRead(body)

    //是否有未读消息
    fun getIsReadMsg(body: RequestBody): Flowable<MyHttpResponse<IshaveNoReadMsgBean>> = mApiService.getIsReadMsg(body)
   //获取弹窗底部数据
    fun getHomeButtomDialogData(body: RequestBody): Flowable<MyHttpResponse<HomeButtomDialogBean>> = mApiService.getHomeButtomDialogData(body)
    //获取信用卡首页数据
    fun getCreditCardData(body: RequestBody): Flowable<MyHttpResponse<HomeCreditCardBean>> = mApiService.getCreditCardData(body)
   //获取筛选初始化的数据
    fun getInitSoftData(body: RequestBody): Flowable<MyHttpResponse<FindInitBean>> = mApiService.getInitSoftData(body)
   //获取筛选后的数据
    fun getSoftData(body: RequestBody): Flowable<MyHttpResponse<UsersAuthLimitBean>> = mApiService.getSoftData(body)

    //获取发现首页数据
    fun getFoundHomeData(body: RequestBody): Flowable<MyHttpResponse<HomeFoundBean>> = mApiService.getFoundHomeData(body)
   //启动广告图
    fun startAdvertise(body: RequestBody): Flowable<MyHttpResponse<AdvertisingBean>> = mApiService.startAdvertise(body)
    //app 启动时间
    fun startApp(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.startApp(body)
    //app 结束时间
    fun stopApp(body: RequestBody): Flowable<MyHttpResponse<Any>> = mApiService.stopApp(body)
    //获取首页弹窗信息
    fun getHomeDialogForUser(body: RequestBody): Flowable<MyHttpResponse<HomeDialogForUser>> = mApiService.getHomeDialogForUser(body)
    //根据标签获取的产品列表
    fun getTagProductList(body: RequestBody): Flowable<MyHttpResponse<ProductInfoListBean>> = mApiService.getTagProductList(body)



}