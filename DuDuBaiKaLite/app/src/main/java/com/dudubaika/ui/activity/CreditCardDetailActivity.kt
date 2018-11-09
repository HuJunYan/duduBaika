package com.dudubaika.ui.activity

import android.text.TextUtils
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.onekeyshare.OnekeyShare
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.CreditCardDetailBean
import com.dudubaika.presenter.contract.CreditCardDetailContract
import com.dudubaika.presenter.impl.CreditCardDetailPresenter
import com.dudubaika.util.ImageUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.UserUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_credit_card_detail.*
import org.jetbrains.anko.startActivity
import java.util.HashMap

class CreditCardDetailActivity : BaseActivity<CreditCardDetailPresenter>(), CreditCardDetailContract.View {

    private var cardId:String=""
    private var mBean:CreditCardDetailBean?=null

    companion object {

        var CARDID="cardId"
    }

    override fun getLayout(): Int = R.layout.activity_credit_card_detail

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun initView() {


        StatusBarUtil.setPaddingSmart(mActivity,tb_crad_detail)
        defaultTitle=  "信用卡详情"
        iv_regist_return.setOnClickListener {
            backActivity()
        }
        now_pay.setOnClickListener {
            if (!TextUtils.isEmpty(mBean?.credit_apply_url)) {
                if (UserUtil.isLogin(mActivity)) {
                    startActivity<WebVerifyActivity>(WebVerifyActivity.WEB_URL_TITLE to mBean!!.credit_name, WebVerifyActivity.WEB_URL_KEY to mBean!!.credit_apply_url)
                    mPresenter.dian(GlobalParams.FALG_EIGHTTEEN,cardId)
                }else{
                    startActivity<LoginActivity>(LoginActivity.ISTOGODETAIL to true)
                }
            }
        }
        tv_share.setOnClickListener {
            showShare()
        }
    }
    private fun  showShare() {
        val oks =  OnekeyShare()
        //关闭sso授权
        oks.disableSSOWhenAuthorize()
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(mBean?.share_title)
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(mBean?.share_url)
        // text是分享文本，所有平台都需要这个字段
        oks.text = mBean?.share_des
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(mBean?.share_img)//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(mBean?.share_url)


        oks.callback= object : PlatformActionListener {

            override fun onComplete(p0: Platform?, p1: Int, p2: HashMap<String, Any>?) {
            }

            override fun onCancel(p0: Platform?, p1: Int) {
            }

            override fun onError(p0: Platform?, p1: Int, p2: Throwable?) {
                LogUtil.i("shareshare",p2?.message)
            }
        }

        // 启动分享GUI
        oks.show(this)
    }

    override fun initData() {
        cardId =  intent.getStringExtra(CARDID)
        if (!TextUtils.isEmpty(cardId)){
            mPresenter.getCreditCardDetail(cardId)
        }

        val kv =  hashMapOf<String,String>()
        kv.put("creditCardId", cardId)
        TCAgent.onEvent(mActivity, TalkingDataParams.CREDITCARD_DETAIL, "", kv)

    }


    override fun getCreditCardDetailComplete(data: CreditCardDetailBean) {

        if (null==data){
            return
        }
        mBean = data

        showView()
    }

    private fun showView() {
        ImageUtil.loadWithCache(mActivity,card_logo,mBean!!.credit_logo,R.drawable.default_banner_icon)

        credit_card_bank_title.text = mBean?.credit_name
        credit_card_bank_desc.text = mBean?.credit_des
        car_vip_title.text = mBean?.credit_privilege_title
        sqtj_value.text = mBean?.credit_privilege
        card_sqtj.text = mBean?.credit_application_title
        card_sqtj_value.text = mBean?.credit_application

        if (TextUtils.isEmpty(mBean?.credit_apply_title)){
            now_pay.text = "立即申请"
        }else {
            now_pay.text = mBean?.credit_apply_title
        }

    }


}
