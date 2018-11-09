package com.dudubaika.ui.activity

import android.content.Context
import android.text.TextUtils
import android.text.TextWatcher
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.dudubaika.R
import com.dudubaika.base.BaseActivity
import com.dudubaika.model.bean.LoanDetailBean
import com.dudubaika.presenter.contract.LoadDetailContract
import com.dudubaika.presenter.impl.LoadDetailPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_add_loan_info.*
import java.text.SimpleDateFormat
import android.text.Editable
import com.dudubaika.event.PrductNameEvent
import com.dudubaika.util.TimeUtils
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
import java.util.*
import android.view.inputmethod.InputMethodManager
import android.text.Spanned
import android.text.InputFilter
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.util.EditInputFilter
import com.tendcloud.tenddata.TCAgent

class AddLoanInfoActivity : BaseActivity<LoadDetailPresenter>(), LoadDetailContract.View {

    private var product_id:String?=null
    private var note_id:String?=null
    //下款时间 xxxxxxxx
    private var start_time:String?=null
    //下款时间 date
    private var oldDate:Date?=null
    //下款时间 String xxxx年xx月xx日
    private var oldTime:String?=null
    //下款周期
    private var term:Int?=null

    //下款时间 被减数
    private var startData:Date?=null
    //还款时间 减数
    private var endtData:Date?=null
    //借款周期
    private var term_money:Int?=null

    companion object {
        var PRODUCT_ID="product_id"
        var NOTE_ID="note_id"
    }

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

    override fun getLayout(): Int = R.layout.activity_add_loan_info
    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity,tb_title)

        //默认下款时间为当天
        p_start_time.text=TimeUtils.getCurrentTime()
        oldDate = Date()
        startData = Date()
        oldTime= TimeUtils.getData2String("yyyy年MM月dd日",oldDate)
        start_time = TimeUtils.getData2String("yyyyMMdd",oldDate)

        iv_return.setOnClickListener({
            backActivity()
        })

        add_loan.setOnClickListener {
            addLoan()
        }
        start_time_view.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(imm.isActive){
                //强制隐藏键盘
                imm.hideSoftInputFromWindow(start_time_view.windowToken, 0)
            }
            showTimeDialog(1)
        }
        rl_my_info.setOnClickListener {
            startActivity<PrductListSimpleActivity>()
        }

       //限制 只能输入正整数
        p_term.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 输入的内容变化的监听
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 输入前的监听

            }

            override fun afterTextChanged(s: Editable) {
                // 输入后的监听
                // 这部分是处理如果输入框内小数点后有俩位，那么舍弃最后一位赋值，光标移动到最后
                if (s.toString().contains(".")) {

                    if (s.length - 1 - s.toString().indexOf(".") >= 0) {
                        p_term.setText( s.toString().subSequence(0, s.toString().indexOf(".")))
                        p_term.setSelection(s.toString().subSequence(0, s.toString().indexOf(".")).length)
                    }
                }
                // 这部分是处理如果用户输入以.开头，在前面加上0
                if (s.toString().trim().substring(0).equals(".")) {
                    p_term.setText( "0")
                    p_term.setSelection(1)
                }
                // 这里处理用户 多次输入.的处理
                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        p_term.setText(s.subSequence(0,1))
                        p_term.setSelection(1)
                        return
                    }
                }
                if(!TextUtils.isEmpty(s.toString())) {
                    term_money = s.toString().trim().toInt()
                    val time =TimeUtils.addAndSubtractDaysByGetTime(TimeUtils.string2Date(oldTime,"yyyy年MM月dd日"),p_term.text.toString().trim().toInt())
                    p_repay_date.text = time
                }else{
                    p_repay_date.text = ""
                }
            }
        })


        val filters = arrayOf<InputFilter>(EditInputFilter())

        //钱的输入
//        p_money.filters = filters
        //限制 只能输入正整数
        p_money.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 输入的内容变化的监听
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // 输入前的监听

            }

            override fun afterTextChanged(s: Editable) {
                // 输入后的监听
                // 这部分是处理如果输入框内小数点后有俩位，那么舍弃最后一位赋值，光标移动到最后
                if (s.toString().contains(".")) {

                    if (s.length - 1 - s.toString().indexOf(".") >= 0) {
                        p_money.setText( s.toString().subSequence(0, s.toString().indexOf(".")))
                        p_money.setSelection(s.toString().subSequence(0, s.toString().indexOf(".")).length)
                    }
                }
                // 这部分是处理如果用户输入以.开头，在前面加上0
                if (s.toString().trim().substring(0).equals(".")) {
                    p_money.setText( "0")
                    p_money.setSelection(1)
                }
                // 这里处理用户 多次输入.的处理
                if (s.toString().startsWith("0") && s.toString().trim().length > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        p_money.setText(s.subSequence(0,1))
                        p_money.setSelection(1)
                        return
                    }
                }

            }
        })

        rl_setting.setOnClickListener {

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(imm.isActive){
                //强制隐藏键盘
                imm.hideSoftInputFromWindow(start_time_view.windowToken, 0)
            }
            showTimeDialog(2)
        }


    }

    private fun addLoan() {
         if (TextUtils.isEmpty(p_name.text.toString().trim())){
            ToastUtil.showToast(mActivity,"请选择下款口子")
            return
         }
         if (TextUtils.isEmpty(p_money.text.toString().trim())){
            ToastUtil.showToast(mActivity,"请输入下款金额")
            return
         }

        if ("0".equals(p_money.text.toString().trim())){
            ToastUtil.showToast(mActivity,"下款金额需大于0")
            return
        }
         if (TextUtils.isEmpty(start_time)){
            ToastUtil.showToast(mActivity,"请选择下款日期")
            return
         }
        if (TextUtils.isEmpty(p_term.text.toString().trim())){
            ToastUtil.showToast(mActivity,"请输入下款周期")
            return
        }

        if (TextUtils.isEmpty(p_repay_date.text.toString().trim())){
            ToastUtil.showToast(mActivity,"请选择还款日期")
            return
        }
        val s = p_repay_date.text.toString().trim().replace("年", "").replace("月", "").replace("日", "")
       if (TextUtils.isEmpty(note_id)){
           note_id=""
       }
        mPresenter.addLoanInfo(note_id,product_id,p_name.text.toString().trim(),p_money.text.toString().trim(),start_time!!,p_term.text.toString().trim(),s)

    }

    override fun initData() {
        note_id =  intent.getStringExtra(PRODUCT_ID)
        p_start_time.setTextColor(resources.getColor(R.color.global_txt_gray))
        defaultTitle = "增加账单记录"
        if (!TextUtils.isEmpty(note_id)){
            tv_home_title.text="账单详情"
            add_loan.text="保存修改"
            defaultTitle = "修改账单"
            p_start_time.setTextColor(resources.getColor(R.color.global_txt_black4))
            mPresenter.getLoanDetailData(note_id!!)
        }

    }

    override fun showData(data: LoanDetailBean) {
        if (null==data){
            return
        }
        if (!TextUtils.isEmpty(data.loan_date)){
            oldTime= data.loan_date
        }
        p_name.text= data.product_name
        p_money.setText( data.loan_money)
        p_money.hint
        //将光标移至文字末尾
        p_money.setSelection( data.loan_money.toString().length)

        p_start_time.text = data.loan_date
        p_term.setText(data.loan_term)
        p_repay_date.text= data.repay_date

        start_time = data.loan_date.replace("年","").replace("月","").replace("日","")
    }

    override fun addLoanComplete() {
        if (TextUtils.isEmpty(note_id)) {
            TCAgent.onEvent(mActivity, TalkingDataParams.SAVE_NOTE,"newNote")
        }else{
            TCAgent.onEvent(mActivity, TalkingDataParams.SAVE_NOTE,"changeNote")
        }
        ToastUtil.showToast(mActivity,"添加账单成功")
    }

    override fun finishActivity() {
        backActivity()
    }


    private fun showTimeDialog(type :Int){

        //时间选择器
        val pvTime = TimePickerBuilder(mActivity!!, OnTimeSelectListener { date, _ ->

            if (type==1) {
                //开始
                oldDate = date
                startData = date
                val sdf = SimpleDateFormat("yyyy年MM月dd日")
                val sdf2 = SimpleDateFormat("yyyyMMdd")
                val format = sdf.format(date)
                //这个是传给服务器用的
                start_time = sdf2.format(date)
                p_start_time.text = format.toString().trim()
                p_start_time.setTextColor(resources.getColor(R.color.global_txt_black4))
                oldTime = format.toString().trim()

                if (!TextUtils.isEmpty(p_term.text.toString().trim())) {
                    val time2 = TimeUtils.addAndSubtractDaysByGetTime(TimeUtils.string2Date(oldTime, "yyyy年MM月dd日"), p_term.text.toString().trim().toInt())
                    p_repay_date.text = time2
                }
            }else{
                //还款时间
                endtData  = date
                if (!TextUtils.isEmpty(startData.toString())){
                    //开始日期不为空
                    term_money = TimeUtils.getDaysFromTwoData(startData,endtData).toString().trim().toInt()
                    if (term_money!! >0) {
                        p_term.setText(term_money.toString())
                    }else{
                        ToastUtil.showToast(mActivity,"还款日期要大于借款日期")
                        return@OnTimeSelectListener
                    }
                }

//                ToastUtil.showToast(mActivity,TimeUtils.getDaysFromTwoData(startData,endtData).toString())


            }

        }).build()
        pvTime.show()

    }

    @Subscribe
    public fun OnPrductNameEvent(event: PrductNameEvent){
        p_name.text = event.p_name
        product_id= event.p_id

    }

}
