package com.dudubaika.ui.activity

import android.app.Dialog
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.widget.Toast
import com.dudubaika.R
import com.dudubaika.base.*
import com.dudubaika.event.*
import com.dudubaika.ui.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.Subscribe
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

class MainActivity : SimpleActivity() {

    var mFragments: MutableList<SupportFragment>? = null
    var mTabPosition: Int = 0
    var mExitTime: Long = 0

    private var mHomeFragment2: HomeFragment2? = null
    private var mCardFragment: CardFragment? = null
    private var mCardTest: CreditCardFragment? = null
    private var mFoundFragment: FoundFragment? = null
    var mMeFragment: MeFragment? = null
    var mVerifyHomeFragment: VerifyHomeFragment? = null
    var mVerifyMeFragment: VerifyMeFragment? = null
    //推送弹窗
    private var mDialog :Dialog?=null

    override fun getLayout(): Int = R.layout.activity_main

    override fun initData() {
//        gotoActivity(mActivity,TalkDetailActivity::class.java,null)
//        startActivity<WebActivity>(WebActivity.WEB_URL_KEY to "http://waptwo.51jiuqi.com/")
    }

    override fun initView() {
        defaultTitle="main"
        initBottomNavigationView()
        initFragment()
    }

    /**
     * 初始化Tab个Fragment
     */
    private fun initFragment() {

        mFragments = mutableListOf()

        if (App.instance.mIsVerify) {
            mVerifyHomeFragment = VerifyHomeFragment()
            mVerifyMeFragment = VerifyMeFragment()
            mFragments?.add(mVerifyHomeFragment!!)
            mFragments?.add(mVerifyMeFragment!!)

            // 修改首页底部 tab 文字
            bnve.menu.getItem(0).title = getString(R.string.home_verify_tab)

        } else {
//            mHomeFragment = HomeFragment()
            mHomeFragment2 = HomeFragment2()
            mCardFragment = CardFragment()
            mCardTest = CreditCardFragment()
            mFoundFragment = FoundFragment()
            mMeFragment = MeFragment()
            mFragments?.add(mHomeFragment2!!)
//            mFragments?.add(mCardFragment!!)
//           mFragments?.add(mCardTest!!)
//            mFragments?.add(mFoundFragment!!)
            mFragments?.add(mMeFragment!!)
        }

        loadMultipleRootFragment(R.id.fl_container, 0,
                mFragments?.get(0),
                mFragments?.get(1))
             /*   mFragments?.get(2),
                mFragments?.get(3))*/

    }

    /**
     * 初始化底部导航栏
     */
    private fun initBottomNavigationView() {

        bnve.enableAnimation(false)
        bnve.enableShiftingMode(false)
        bnve.enableItemShiftingMode(false)

        bnve.setOnNavigationItemSelectedListener { item ->
            val position = bnve.getMenuItemPosition(item)

            showHideFragment(mFragments?.get(position), mFragments?.get(mTabPosition))
            mTabPosition = position
            true
        }

    }

    /**
     * 给底部导航栏增加气泡
     */
    private fun addBadgeAt(position: Int, number: Int): Badge {
        // add badge
        return QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12F, 2F, true)
                .bindTarget(bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener { dragState, badge, targetView ->
                    if (Badge.OnDragStateChangedListener.STATE_SUCCEED === dragState) {
                        //清除气泡的回调
                    }
                }
    }






    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(mActivity, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
            } else {
                App.instance.exitApp()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Subscribe
    fun onLogoutEvent(event: LogOutEvent) {
        mVerifyHomeFragment?.refreshData()
//        mHomeFragment?.refreshData()
        mMeFragment?.refreshUI()
    }


    @Subscribe
    fun onLoginEvent(event: LoginSuccesfulEvent) {
//        mHomeFragment?.refreshData()
        mMeFragment?.refreshUI()
        mVerifyHomeFragment?.refreshData()
    }

    @Subscribe
    fun onShowHomeTabEvent(event: ShowHomeTabEvent) {
        bnve.currentItem = 0
    }

    @Subscribe
    fun onCheckFoundFragmentEvent(event: CheckFoundFragmentEvent) {
        bnve.currentItem = 2
    }
}
