package com.dudubaika.ui.fragment

import android.view.View
import com.dudubaika.R
import com.dudubaika.base.BaseFragment
import com.dudubaika.event.MyLoanEvent
import com.dudubaika.event.RefreshDataWhitParams
import com.dudubaika.model.bean.LoanMoney
import com.dudubaika.model.bean.MineLoanBean
import com.dudubaika.presenter.contract.MineLoanContract
import com.dudubaika.presenter.impl.MineLoanPresenter
import com.dudubaika.ui.adapter.LoanAdapter
import kotlinx.android.synthetic.main.fragment_dh.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 已还的fragment
 *
 */
class YhFragment : BaseFragment<MineLoanPresenter>() ,MineLoanContract.View{


    var mAdapter: LoanAdapter?=null
   private var FLAG="5"
    override fun initInject() {
        fragmentComponent.inject(this)

    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
        refresh.isEnableRefresh = false
    }

    override fun getLayout(): Int =R.layout.fragment_dh
    override fun initView() {
        defaultTitle="已还界面"
        refresh.isEnableLoadMore = false
        refresh.isEnableRefresh = true
        //是否在加载的时候禁止列表的操作
        refresh.setDisableContentWhenLoading(true)
        refresh.setEnableOverScrollBounce(true);//是否启用越界回弹

        refresh.setOnRefreshListener( {
            mPresenter.getData(FLAG)
        })
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        listview.visibility  = View.GONE
        mPresenter.getData(FLAG)
    }


    override fun showData(data: MineLoanBean) {
        refresh.isEnableRefresh = false
        if (null==data){
            return
        }
        listview.visibility  = View.VISIBLE
        total_money.text = "已还款总额"+data?.total_money+"元"
        total_num.text = "("+ data?.total_count+")笔"
        mAdapter = LoanAdapter(_mActivity, data.paynote_list, R.layout.item_daihuan)
        listview.adapter = mAdapter
        EventBus.getDefault().post(LoanMoney(data?.total_money,data?.total_count.toString(),1))
    }

    override fun changeComplete() {
        mPresenter.getData(FLAG)
    }

    @Subscribe
    public fun onMyLoanEvent(event : MyLoanEvent){
        if ("2".equals(event.falg)){
            val product_id = event.product_id
            if (null!=product_id) {
                mPresenter.changeNoteStatus(product_id)
            }
        }
    }

    @Subscribe
    public fun OnRefreshDataWhitParams(event: RefreshDataWhitParams){

        if (event!=null && "2".equals(event.tag)) {
            listview.visibility  = View.GONE
            mPresenter.getData(FLAG)
        }
    }

}
