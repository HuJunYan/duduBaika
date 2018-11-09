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
import com.dudubaika.ui.adapter.LoanAdapter2
import kotlinx.android.synthetic.main.fragment_dh.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 待还的fragment
 *
 */
class DhFragment : BaseFragment<MineLoanPresenter>() ,MineLoanContract.View{


    var mAdapter: LoanAdapter2?=null
   private var FLAG="4"
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
        defaultTitle="待还界面"
      /*  refresh.isEnableLoadMore = false
        refresh.isEnableRefresh = true
        //是否在加载的时候禁止列表的操作
        refresh.setDisableContentWhenLoading(true)*/
        refresh.isEnableOverScrollBounce = true//是否启用越界回弹

        refresh.setOnRefreshListener( {
            mPresenter.getData(FLAG)
        }
        )
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
        total_money.text = "应还款总额"+data?.total_money+"元"
        total_num.text = "("+ data?.total_count+")笔"
        mAdapter = LoanAdapter2(_mActivity, data.paynote_list, R.layout.item_daihuan)
        listview.adapter = mAdapter
        EventBus.getDefault().post(LoanMoney(data?.total_money,data?.paynote_list.size.toString(),0))
    }

    override fun changeComplete() {
        //标记为已还 重新更新下数据
        mPresenter.getData(FLAG)
    }

    @Subscribe
    public fun onMyLoanEvent(event : MyLoanEvent){
        //标记已还 或者删除
        if ("1".equals(event.falg)){
            val product_id = event.product_id
            if (null!=product_id) {
                mPresenter.changeNoteStatus(product_id)
            }
        }


    }

    @Subscribe
   public fun OnRefreshDataWhitParams(event: RefreshDataWhitParams){

        if (event!=null && "1".equals(event.tag)) {
            listview.visibility  = View.GONE
            mPresenter.getData(FLAG)
        }
   }



}
