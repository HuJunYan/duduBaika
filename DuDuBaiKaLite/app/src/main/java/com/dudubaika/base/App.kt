package com.dudubaika.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import cn.jpush.android.api.JPushInterface
import com.balsikandar.crashreporter.CrashReporter
import com.dudubaika.R
import com.dudubaika.dagger.componet.AppComponent
import com.dudubaika.dagger.componet.DaggerAppComponent
import com.dudubaika.dagger.module.AppModule
import com.dudubaika.log.LogUtil
import com.dudubaika.util.Utils
import com.facebook.stetho.Stetho
import com.liulishuo.filedownloader.FileDownloader
import com.mob.MobSDK
import com.moxie.client.manager.MoxieSDK
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import android.os.StrictMode
import com.dudubaika.event.RecordStopAppEvent
import com.tendcloud.tenddata.TCAgent
import org.greenrobot.eventbus.EventBus

class App : Application(), Application.ActivityLifecycleCallbacks  {

    private var resumeCount: Int = 0
    private var appCount: Int = 0
    //当前APP是否是审核视图
    var mIsVerify: Boolean = false

    //当前环境
    var mCurrentHost = HOST.PRO

    // DEV开发,PRE预发布,PRO上线
    enum class HOST {
        DEV, PRE, PRO
    }

    private val mAllActivities: MutableSet<Activity> = mutableSetOf<Activity>()

    companion object {
        lateinit var instance: App
        var isOnResume: Boolean = false
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
//        android.os.Debug.startMethodTracing()
        //判断程序是否重复启动
        val isApplicationRepeat = Utils.isApplicationRepeat(this)
        if (isApplicationRepeat) {
            return
        }
        MoxieSDK.init(this)
        instance = this
        //初始化Logger的TAG
        LogUtil.init(true)
        // dex突破65535的限制
        MultiDex.install(this)
        //初始化下拉刷新View
        initSmartRefreshLayout()
        //初始化下载器
        FileDownloader.setupOnApplicationOnCreate(instance)
        //初始化Stetho
        Stetho.initializeWithDefaults(this)
        //APP Crash后保存log日志 log日志在/Android/data/package-name/files/crashLog
        CrashReporter.initialize(this, Utils.getCrashLogPath())
        //腾讯buggly
        CrashReport.initCrashReport(instance, GlobalParams.BUGGLY_ID, false)
        //"全部加载完成"
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = getString(R.string.srl_footer_nothing)
        //初始化极光
        initJPush()
        //数据统计初始化
        initCollectionData()
        //注册声明周期监听
        registerActivityLifecycleCallbacks(this)

        MobSDK.init(this)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
    }

    private fun initCollectionData() {

        TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this, "9F8D6B39041D4C23B0101655ADF1BDBE", Utils.getChannelId())
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true)
    }

    private fun initChoseImg() {

        TCAgent.LOG_ON=true
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this, GlobalParams.TALK_DATA_ID, Utils.getChannelId())
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true)
    }

    private fun initJPush() {
        JPushInterface.init(instance)
        JPushInterface.setDebugMode(mCurrentHost==HOST.DEV)
    }

    /**
     * 保存Activity的引用
     */
    fun addActivity(act: Activity) {
        mAllActivities.add(act)
    }

    /**
     * 清除Activity的引用
     */
    fun removeActivity(act: Activity) {
        mAllActivities.remove(act)
    }

    fun getActivityList(): MutableSet<Activity> {
        return mAllActivities
    }

    /**
     * 退出App
     */
    fun exitApp() {

        synchronized(mAllActivities) {
            for (act in mAllActivities) {
                act.finish()
            }
        }
//        android.os.Process.killProcess(android.os.Process.myPid())
//        System.exit(0)
    }

    fun initSmartRefreshLayout() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SmartRefreshLayout.setDefaultRefreshHeaderCreator({ context, _ ->
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        })
        SmartRefreshLayout.setDefaultRefreshFooterCreator({ context, _ ->
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
        })
    }

    override fun onActivityResumed(activity: Activity) {
        isOnResume = true
        resumeCount++
    }

    override fun onActivityPaused(activity: Activity) {
        resumeCount--
        if (resumeCount == 0) {
            isOnResume = false
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        appCount++
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        appCount--
        if (appCount==0){
//            应用进入后台
            EventBus.getDefault().post(RecordStopAppEvent())
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }



}