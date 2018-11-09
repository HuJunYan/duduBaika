package com.dudubaika.ui.activity

import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.AuthInfoContract
import com.dudubaika.presenter.impl.AuthInfoPresenter
import com.dudubaika.util.AutherUtils
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import kotlinx.android.synthetic.main.activity_auth_info.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.EventBus

/**
 * 个人信息认证 页面
 */
class AuthInfoActivity : BaseActivity<AuthInfoPresenter>(), AuthInfoContract.View {

    private var mCountyBean: CountyBean? = null
    private var mProvince: String? = null //省份
    private var mCity: String? = null //省份
    private var mProvincePosition: Int = 0
    private var mCityId: String? = null
    private var mCityPosition: Int = 0
    private var mCountyPosition: Int = 0
    override fun getLayout(): Int = R.layout.activity_auth_info
    private var mMProvinceData: ArrayList<String> = ArrayList<String>()
    private var mProvinceBean: ProvinceBean? = null
    private var mCityData: ArrayList<String> = ArrayList<String>()
    private var mCountyData: ArrayList<String> = ArrayList<String>()
    private var mCityBean: CityBean? = null
    private var mProvinceMomentPosition: Int = 0
    private var mCityMomentPosition: Int = 0
    private var mProvinceId: String? = null // 省份id
    private var mCounty: String? = null
    private var mCountyId: String? = null
    private var mType: Int = 1  // 1 设置常住地址  2  设置单位地址
    private var mAddressMap = HashMap<Int, AddressBean>()
    private var mData: UserInfoBean? = null

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun saveUserInfoComplete() {
        ToastUtil.showToast(mActivity, "认证成功")
        //发送event 到homeFragment2 查询认证
        EventBus.getDefault().post(getUserAuthStatus())
        if (!mActivity.isFinishing){
            finish()
        }
    }


    override fun showProgress() {
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE

    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showError(url: String, msg: String) {
        if (url == ApiSettings.GET_USER_INFO){
            progress.ll_loading.visibility = View.GONE
            progress.ll_error.visibility = View.VISIBLE
            progress.ll_error.setOnClickListener {
                initData()
            }
        }

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_auth_info)
        defaultTitle="个人信息认证"
        iv_auth_back.setOnClickListener({
            backActivity()
        })
        tv_auth_info_home_address.setOnClickListener {
            mType = 1
            mPresenter.getProvinceData()
        }
        tv_auth_info_work_address.setOnClickListener {
            mType = 2
            mPresenter.getProvinceData()
        }
        tv_auth_business.setOnClickListener { chooseOccupation() }
        tv_auth_info_confirm.setOnClickListener { saveUserInfo() }
    }

    private fun saveUserInfo() {
        var qq_num = et_auth_info_qq.text.toString().trim()
        if (TextUtils.isEmpty(qq_num)) {
            ToastUtil.showToast(mActivity, "请输入您的QQ号码")
            return
        }
        var user_add = tv_auth_info_home_address.text.toString().trim()
        if (TextUtils.isEmpty(user_add)) {
            ToastUtil.showToast(mActivity, "请选择常住地址")
            return
        }
        var user_detail_address = et_auth_info_address_details.text.toString().trim()
        if (TextUtils.isEmpty(user_detail_address)) {
            ToastUtil.showToast(mActivity, "请输入常住地址街道路及门牌号")
            return
        }
        var company_name = et_auth_info_work_name.text.toString().trim()
        if (TextUtils.isEmpty(company_name)) {
            ToastUtil.showToast(mActivity, "请输入您的单位名称")
            return
        }
        var occupation = tv_auth_business.text.toString().trim()
        if (TextUtils.isEmpty(occupation)) {
            ToastUtil.showToast(mActivity, "请选择您所属的行业")
            return
        }
        var company_phone = et_auth_info_work_num.text.toString().trim()
        if (TextUtils.isEmpty(company_phone)) {
            ToastUtil.showToast(mActivity, "请输入您的单位电话")
            return
        }
        var company_address = tv_auth_info_work_address.text.toString().trim()
        if (TextUtils.isEmpty(company_address)) {
            ToastUtil.showToast(mActivity, "请选择单位地址")
            return
        }
        var company_detail_address = et_auth_info_work_address_details.text.toString().trim()
        if (TextUtils.isEmpty(company_detail_address)) {
            ToastUtil.showToast(mActivity, "请输入单位地址街道路及门牌号")
            return
        }
        mPresenter.saveUserInfo(mAddressMap, user_detail_address, company_name, company_phone, company_detail_address, qq_num, occupation)
    }

    //选择职业
    private fun chooseOccupation() {
        if (mData == null) {
            return
        }
        showOccupationDialog()
    }

    /**
     * 行业选择的弹窗
     */
    private fun showOccupationDialog() {
        val dialogView = View.inflate(this, R.layout.dialog_listview, null)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        if (mData!!.occupation.size > 0) {
            for (i in mData!!.occupation.indices) {
                arrayAdapter.add(mData!!.occupation[i].occupation_name)
            }
        }

        val builder = AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .setCancelable(true)
        val alertDialog = builder.create()

        alertDialog.setTitle("请选择所属行业")
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        var mListView = dialogView.findViewById<ListView>(R.id.listview)
        mListView.setDividerHeight(0)
        mListView.setAdapter(arrayAdapter)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            alertDialog.dismiss()
            showSubOccupation(mData!!.occupation[i].sub_occupation)
        }
        alertDialog.show()
    }

    private fun showSubOccupation(sub_occupation: ArrayList<UserInfoBean.SubOccupation>) {
        val dialogView = View.inflate(this, R.layout.dialog_listview, null)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        if (sub_occupation.size > 0) {
            for (i in sub_occupation.indices) {
                arrayAdapter.add(sub_occupation[i].sub_name)
            }
        }

        val builder = AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .setCancelable(true)
        val alertDialog = builder.create()

        alertDialog.setTitle("请选择您的职业")
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        var mListView = dialogView.findViewById<ListView>(R.id.listview)
        mListView.setDividerHeight(0)
        mListView.setAdapter(arrayAdapter)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            alertDialog.dismiss()
            tv_auth_business.text = sub_occupation[i].sub_name

        }
        alertDialog.show()
    }

    override fun initData() {
        mPresenter.getUserInfo()
    }

    override fun getUserInfoResult(data: UserInfoBean?) {
        mData = data
//        refreshUIAndData()
    }

    //刷新个人信息UI
    private fun refreshUIAndData() {
        if (mData == null) {
            return
        }
        et_auth_info_qq.setText(mData?.qq_num)
        if (!TextUtils.isEmpty(mData?.user_address_provice)) {
            tv_auth_info_home_address.text = mData?.user_address_provice + "-" + mData?.user_address_city + "-" + mData?.user_address_county
        }
        et_auth_info_address_details.setText(mData?.user_address_detail)
        et_auth_info_work_name.setText(mData?.company_name)
        tv_auth_business.text = mData?.selected_occupation_name
        et_auth_info_work_num.setText(mData?.company_phone)
        if (!TextUtils.isEmpty(mData?.company_address_provice)) {
            tv_auth_info_work_address.text = mData?.company_address_provice + "-" + mData?.company_address_city + "-" + mData?.company_address_county
        }
        et_auth_info_work_address_details.setText(mData?.company_address_detail)
        //保存信息到集合中
        mAddressMap.put(1, AddressBean(mData!!.user_address_provice, mData!!.user_address_city, mData!!.user_address_county))
        mAddressMap.put(2, AddressBean(mData!!.company_address_provice, mData!!.company_address_city, mData!!.company_address_county))
    }


    override fun getProviceResult(data: ProvinceBean?) {
        if (data == null) {
            ToastUtil.showToast(this, "数据错误")
            return
        }
        mProvinceBean = data
        parserProvinceListData(data)
    }

    override fun getCityResult(data: CityBean?) {
        if (data == null) {
            ToastUtil.showToast(this, "数据错误")
            return
        }
        mCityBean = data
        parserCityListData(mCityBean!!)
        showCityDialog(mCityData)
    }


    override fun getCountyResult(data: CountyBean?) {
        if (data == null) {
            ToastUtil.showToast(this, "数据错误")
            return
        }
        mCountyBean = data
        parserCountyListData(mCountyBean!!)
        showCountyDialog(mCountyData)
    }

    fun showCountyDialog(list: List<String>) {
        val dialogView = View.inflate(this, R.layout.dialog_listview, null)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        if (list.size > 0) {
            for (i in list.indices) {
                arrayAdapter.add(list[i])
            }
        }

        val builder = AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .setCancelable(true)
        val alertDialog = builder.create()

        alertDialog.setTitle("请选择地区")
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        var mListView = dialogView.findViewById<ListView>(R.id.listview)
        mListView.setDividerHeight(0)
        mListView.setAdapter(arrayAdapter)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            alertDialog.dismiss()
            //确认此次选择的省的信息
            mProvincePosition = mProvinceMomentPosition
            mProvince = mMProvinceData[mProvincePosition]
            mProvinceId = mProvinceBean!!.provinceList[mProvincePosition].province_id
            //确认此次选择的市的信息
            mCityPosition = mCityMomentPosition
            mCity = mCityData[mCityPosition]
            mCityId = mCityBean!!.cityList[mCityPosition].city_id
            //确认此次选择的区县的信息
            mCountyPosition = i;
            mCounty = mCountyData[mCountyPosition]
            mCountyId = mCountyBean!!.countyList[mCountyPosition].county_id
            //保存到集合中
            mAddressMap.put(mType, AddressBean(mProvince!!, mCity!!, mCounty!!))
            //设置到对应控件上
            when (mType) {
                1 -> tv_auth_info_home_address.text = ("$mProvince-$mCity-$mCounty")
                2 -> tv_auth_info_work_address.text = ("$mProvince-$mCity-$mCounty")
            }

        }
        alertDialog.show()


    }

    fun parserCountyListData(mCountyBean: CountyBean) {
        val datas = mCountyBean.countyList
        mCountyData.clear()
        for (i in datas.indices) {
            val data = datas[i]
            val city_name = data.county_name
            mCountyData.add(city_name)
        }
    }


    fun showCityDialog(list: List<String>) {
        val dialogView = View.inflate(this, R.layout.dialog_listview, null)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        if (list.size > 0) {
            for (i in list.indices) {
                arrayAdapter.add(list[i])
            }
        }

        val builder = AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .setCancelable(true)
        val alertDialog = builder.create()

        alertDialog.setTitle("请选择城市")
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        var mListView = dialogView.findViewById<ListView>(R.id.listview)
        mListView.setDividerHeight(0)
        mListView.setAdapter(arrayAdapter)
        mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            alertDialog.dismiss()
            mProvincePosition = mProvinceMomentPosition
            mProvince = mMProvinceData[mProvincePosition]
            mProvinceId = mProvinceBean!!.provinceList[mProvincePosition].province_id

            mCityMomentPosition = i;
            var mCityMomentPosition = mCityBean!!.cityList[mCityMomentPosition].city_id
            mPresenter.getCountyData(mCityMomentPosition)
        }
        alertDialog.show()


    }

    /**
     * 解析城市数据
     *
     * @param mCityBean 城市数据源
     */
    private fun parserCityListData(mCityBean: CityBean) {
        val datas = mCityBean.cityList
        mCityData.clear()
        for (i in datas.indices) {
            val data = datas[i]
            val city_name = data.city_name
            mCityData.add(city_name)
        }
    }


    /**
     * 解析省份数据
     *
     * @param mProvinceBean 省份数据源
     */
    private fun parserProvinceListData(mProvinceBean: ProvinceBean) {
        if (mProvinceBean == null || mProvinceBean.provinceList == null) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        val datas = mProvinceBean.provinceList
        mMProvinceData.clear()
        for (i in datas.indices) {
            val data = datas[i]
            val provice_name = data.province_name
            mMProvinceData.add(provice_name)
        }
        showProvineDialog(mMProvinceData)
    }

    fun showProvineDialog(list: List<String>) {
        val dialogView = View.inflate(this, R.layout.dialog_listview, null)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        if (list.size > 0) {
            for (i in list.indices) {
                arrayAdapter.add(list[i])
            }
        }
        var mListView = dialogView.findViewById<ListView>(R.id.listview)
        mListView.setDividerHeight(0)
        mListView.setAdapter(arrayAdapter)

        val builder = AlertDialog.Builder(mActivity)
                .setView(dialogView)
                .setCancelable(true)
        val alertDialog = builder.create()

        alertDialog.setTitle("请选择省份")
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(true)
        mListView.setOnItemClickListener({ _, _, i, l ->
            alertDialog.dismiss()
            mProvinceMomentPosition = i
            var mProvinceMomentPosition = mProvinceBean!!.provinceList[mProvinceMomentPosition].province_id
            mPresenter.getCityData(mProvinceMomentPosition)
        })
        alertDialog.show()
    }







}
