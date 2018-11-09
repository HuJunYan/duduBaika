package com.dudubaika.ui.activity

import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.SimpleActivity
import com.dudubaika.event.RefreshDataWhitParams
import com.dudubaika.model.bean.LoanMoney
import com.dudubaika.presenter.impl.MineLoanPresenter
import com.dudubaika.ui.adapter.FragAdapter
import com.dudubaika.ui.fragment.DhFragment
import com.dudubaika.ui.fragment.YhFragment
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_mine_loan_record.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.ArrayList

/**
 * 待还记录界面
 */
class MineLoanRecordActivity : SimpleActivity() {

    //当前界面选中的位置
    private var mCurrentPosotion:Int?=0
    private var pagerAdapter:FragAdapter?=null
    private var listFragment: ArrayList<Fragment>? =null
    override fun getLayout(): Int = R.layout.activity_mine_loan_record

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,mine_loan_record)
        defaultTitle="待还记录"
        iv_return.setOnClickListener({
            backActivity()
        })

        b1.setOnClickListener {
            selectLeft()
            view_pager.currentItem = 0
            money_total.text ="0"
            money_count.text = "0"
            EventBus.getDefault().post(RefreshDataWhitParams("1"))

        }
        b2.setOnClickListener {
            selectRight()
            view_pager.currentItem = 1
            money_total.text ="0"
            money_count.text = "0"
            EventBus.getDefault().post(RefreshDataWhitParams("2"))

        }
    }

    private fun selectRight() {
        b1.setTextColor(resources.getColor(R.color.me_text_color))
        left.visibility = View.INVISIBLE

        b2.setTextColor(resources.getColor(R.color.red_home))
        right.visibility = View.VISIBLE
        money_key.text="已还款总额  (元)"
    }

    private fun selectLeft() {
        b1.setTextColor(resources.getColor(R.color.red_home))
        left.visibility = View.VISIBLE


        b2.setTextColor(resources.getColor(R.color.me_text_color))
        right.visibility = View.INVISIBLE
        money_key.text="应还款总额  (元)"

    }

    override fun initData() {
        listFragment = ArrayList()
        listFragment?.add(DhFragment())
        listFragment?.add(YhFragment())
        pagerAdapter = FragAdapter(supportFragmentManager,listFragment)
        view_pager.adapter  =pagerAdapter
        view_pager.currentItem = 0
        selectLeft()
        view_pager.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position==0){
                    selectLeft()
                }else{
                    selectRight()
                }
                mCurrentPosotion = position
            }

        })
    }

    @Subscribe
    public fun OnLoanMoney(event: LoanMoney){
        if (event.posotion==mCurrentPosotion) {
            money_total.text = event.money
            money_count.text = event.count
        }

    }

}
