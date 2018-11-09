package com.dudubaika.ui.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.presenter.contract.OpionContract
import com.dudubaika.presenter.impl.UpLoadOpionPresenter
import com.dudubaika.util.RegexUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_opinion_up.*

/**
 * 意见反馈
 */
class OpinionUpActivity : BaseActivity<UpLoadOpionPresenter>(), OpionContract.View {
    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    private var maxLength = 200

    override fun getLayout(): Int = R.layout.activity_opinion_up


    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_opinion_up)
        defaultTitle="意见反馈"
        iv_up_opinion_return.setOnClickListener({ backActivity() })
        confirm.setOnClickListener({ upload() })
        opion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length > maxLength) {
                        val substring = s.substring(0, maxLength)
                        opion.setText(substring)
                        opion.setSelection(maxLength)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }


    override fun initData() {

        opion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.i("sss", "s:" + s.length + "start=" + start + "before=" + before + "count" + count)
                txt_num.text = s.length.toString() + "/200"
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    override fun upLoadComplete() {
        ToastUtil.showToast(mActivity, "意见反馈成功 我们会及时处理！")
        TCAgent.onEvent(mActivity, TalkingDataParams.FEED_BACK)
        finish()
    }

    /**
     * 上传反馈意见
     */
    private fun upload() {
        if (TextUtils.isEmpty(opion.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "请填写您要反馈的内容")
            return
        }

        if (opion.text.length < 5 || opion.text.length > 200) {
            ToastUtil.showToast(mActivity, "您反馈的内容需大于五个字")
            return
        }

        if (TextUtils.isEmpty(phone.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "请输入您的联系方式")
            return
        }
        if (!RegexUtil.IsTelephone(phone.text.toString().trim())) {
            ToastUtil.showToast(mActivity, "该手机号码不合规，请重新输入")
            return
        }

        mPresenter.upLoadOpion(opion.text.toString().trim(), phone.text.toString().trim())

    }

}
