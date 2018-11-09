package com.dudubaika.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.dudubaika.R
import com.dudubaika.dagger.componet.DaggerFragmentComponent

import com.dudubaika.dagger.componet.FragmentComponent
import com.dudubaika.dagger.module.FragmentModule
import com.dudubaika.event.DummyEvent
import com.dudubaika.util.StatusBarUtil
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject
import com.tendcloud.tenddata.TCAgent



abstract class BaseFragment<T : BaseContract.BasePresenter<*>> : SupportFragment(), BaseContract.BaseView {

    protected var mContext: Context? = null
    protected var defaultTitle: String = " "

    @Inject
    lateinit var mPresenter: T

    val mFragmentModule: FragmentModule get() = FragmentModule(this)

    val fragmentComponent: FragmentComponent
        get() = DaggerFragmentComponent.builder()
                .appComponent(App.instance.appComponent)
                .fragmentModule(mFragmentModule)
                .build()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayout(), null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = activity?.window?.attributes
            if (localLayoutParams != null) {
                localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInject()
        initPresenter()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        setStatusBar()
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(activity!!.applicationContext, this.defaultTitle)
    }

    override fun onPause() {
//        if (!this.isHidden) {
//
//        }

        super.onPause()
        TCAgent.onPageEnd(activity!!.applicationContext, this.defaultTitle)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (::mPresenter.isInitialized) {
            mPresenter.detachView()
        }
    }

    /**
     * 设置状态栏
     */
    protected open fun setStatusBar() {
        StatusBarUtil.immersive(activity, resources.getColor(R.color.colorPrimary), 0F)
    }

    /**
     * 初始化子类布局
     */
    protected abstract fun getLayout(): Int

    /**
     * 初始化子类View
     */
    protected abstract fun initView()

    /**
     * 初始化子类一些数据
     */
    protected abstract fun initData()

    /**
     * 初始化dagger2
     */
    protected abstract fun initInject()

    /**
     * 初始化Presenter
     */
    protected abstract fun initPresenter()

    /**
     * 该方法不执行，只是让Event编译通过
     */
    @Subscribe
    fun dummy(event: DummyEvent) {
    }
}