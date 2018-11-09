package com.dudubaika.ui.activity

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.PrductNameEvent
import com.dudubaika.model.bean.MsgListBean
import com.dudubaika.model.bean.ProductListSimpleBean
import com.dudubaika.model.bean.SimpleProductBean
import com.dudubaika.presenter.contract.ProductListSimpleContract
import com.dudubaika.presenter.impl.ProductListSimplePresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter
import com.mcxtzhang.commonadapter.lvgv.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.dialog_add_product.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity

/**
 * 搜索界面
 */
class SearchActivity : BaseActivity<ProductListSimplePresenter>(), ProductListSimpleContract.View {
    override fun showMsgList(data: MsgListBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //总集合
    private var mList:ArrayList<SimpleProductBean>?=null
    //搜索出来的集合
    private var mSearchList:ArrayList<SimpleProductBean>?=ArrayList()
    private var mBean:ProductListSimpleBean?=null
    //是否来自首页
    private var isFromHome:Boolean=false

    companion object {

        var CURRENTTAG:String="currentTag"
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() = Unit

    override fun hideProgress() = Unit

    override fun showError(url: String, msg: String) {
    }

    override fun getLayout(): Int= R.layout.activity_search

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb)
        defaultTitle="下款口子搜索"
        tv_cancle.setOnClickListener {
            backActivity()
        }
        add.setOnClickListener {
            //添加一个下款入口
            if (!isFromHome) {
                showDialogAdd()
            }
        }

        p_name_key.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 输入的内容变化的监听
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 输入前的监听

            }

            override fun afterTextChanged(s: Editable) {
                // 输入后的监听
                mSearchList?.clear()
                if (TextUtils.isEmpty(s.toString().replace("/n","").trim())){
                    listview.visibility  =View.GONE
                    add.visibility = View.GONE
                    no2.visibility = View.GONE
                    return
                }
                if(!TextUtils.isEmpty(s.toString())) {
                    for (item in mList!!){
                        if (item.productName.contains(s.toString().trim())){
                            mSearchList?.add(item)
                        }
                    }

                }
                if (mSearchList!!.size>0) {
                    listview.visibility  =View.VISIBLE
                    add.visibility = View.GONE
                    showListData()
                }else{
                    listview.visibility  =View.GONE
                    if (!isFromHome) {
                        add.visibility = View.VISIBLE
                    }else{
                        add.visibility = View.VISIBLE
                        no2.visibility = View.GONE
                        no.text="没找到哦"
                    }

                }
            }
        })
    }

    override fun initData() {
        mList = ArrayList()
        isFromHome = intent.getBooleanExtra(CURRENTTAG,false)
        mPresenter.getData()
    }

    override fun showData(data: ProductListSimpleBean?) {

        if (null==data){
            return
        }
        mBean= data

        if (mBean!!.name_list.size>0) {
            for (item in mBean!!.name_list) {
                for (it in item.list){
                    mList?.add(SimpleProductBean(it.product_id, it.product_name, it.product_logo))
                }
            }
        }

        val imm:InputMethodManager = p_name_key.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)

    }


    //展示列表数据
    private fun showListData() {
        //我的记账条目数据
        listview.adapter = object : CommonAdapter<SimpleProductBean>(mActivity, mSearchList, R.layout.item_simple_product) {
            override fun convert(holer: ViewHolder?, item: SimpleProductBean?, posotion: Int) {

                holer?.setOnClickListener(R.id.content, {
                    //机构名称
                    if (!isFromHome) {
                        EventBus.getDefault().post(PrductNameEvent(item!!.productName, "2", item!!.productId))
                    }else{
                        startActivity<ProductInfoActivity>(ProductInfoActivity.TILTLE to item!!.productName,ProductInfoActivity.PRODUCT_ID to item!!.productId)
                    }
                    if (!isFromHome) {
                        backActivity()
                    }
                })

                ImageUtil.loadWithCache(mActivity,holer!!.getView<CircleImageView>(R.id.p_logo),item!!.productLogo,R.drawable.product_logo_default)
                holer.setText(R.id.p_name, item?.productName)

            }
        }

    }


    private fun showDialogAdd() {
        val mDialog = Dialog(this,R.style.MyDialog)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        contentView.dialog_confirm.setOnClickListener {
            //确定

            val productname = contentView.p_name.text.toString().trim()
            if (TextUtils.isEmpty(productname)) {
                ToastUtil.showToast(mActivity,"请输入下款口子")
                return@setOnClickListener
            }
            //机构名称
            EventBus.getDefault().post(PrductNameEvent(productname,"2",""))
            mDialog.dismiss()
            backActivity()
        }
        contentView.dialog_cancle.setOnClickListener {
            //确定
            mDialog.dismiss()
        }

        mDialog?.setContentView(contentView)
        mDialog?.setCancelable(true)
        mDialog?.show()

    }

}
