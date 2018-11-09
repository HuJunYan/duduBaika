package com.dudubaika.ui.activity

import android.Manifest
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.TalkingDataParams
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.model.bean.AuthStatus
import com.dudubaika.model.bean.ContactsBean
import com.dudubaika.model.bean.ExtroContactsBean
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.AuthExtroContactsContract
import com.dudubaika.presenter.impl.AuthExtroContactsPresenter
import com.dudubaika.util.*
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import com.tendcloud.tenddata.TCAgent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth_extro_contacts.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList
import kotlin.collections.HashMap

class AuthExtroContactsActivity : BaseActivity<AuthExtroContactsPresenter>(), AuthExtroContactsContract.View {
    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    private var mData: ExtroContactsBean? = null
    private var mMap = HashMap<String, ExtroContactsBean.EmeContacts>()
    private var currentType1 = ""
    private var currentType2 = ""
    override fun getLayout(): Int = R.layout.activity_auth_extro_contacts
    private var isGetContactsing = false
    private var mContactsDialogDada: ArrayList<String>? = null
    var alertDialog: AlertDialog? = null
    private var mContacts = ArrayList<java.util.HashMap<String, String>>()
    private var mContactsPoistion = 0
    private var mIsClickContacts1 = true
    private var clickPosition = 0
    override fun showProgress() {
        progress.visibility = View.VISIBLE
        progress.ll_loading.visibility = View.VISIBLE
        progress.ll_error.visibility = View.GONE

    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }


    override fun showError(url: String, msg: String) {
        if (url == ApiSettings.GET_EXTRO_CONTACTS) {
            if (ApiSettings.GET_EXTRO_CONTACTS == url) {
                progress.ll_loading.visibility = View.GONE
                progress.ll_error.visibility = View.VISIBLE
                progress.ll_error.setOnClickListener {
                    initData()
                }
            }
        }

    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_auth_extro)
        defaultTitle="紧急联系人"
        iv_auth_extro_back.setOnClickListener({
            backActivity()
        })
        tv_auth_extro_confirm.setOnClickListener { submitExtroContacts() }
        iv_auth_contacts1.setOnClickListener {
            mIsClickContacts1 = true
            getContacts()
        }
        iv_auth_contacts2.setOnClickListener {
            mIsClickContacts1 = false
            getContacts()
        }
        rl_auth_nexus1.setOnClickListener {
            clickPosition = 0
            showEmeDialog()
        }
        rl_auth_nexus2.setOnClickListener {
            clickPosition = 1
            showEmeDialog()
        }
        SpannableUtils.setEmojiFilter(et_auth_nexus_name1)
        SpannableUtils.setEmojiFilter(et_auth_nexus_name2)
    }

    private fun showEmeDialog() {
        if (mData == null || mData!!.type_list == null || mData!!.type_list.size == 0) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        if (!isFinishing) {
            val dialogView = View.inflate(this, R.layout.dialog_listview, null)
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            var temporaryList = ArrayList<ExtroContactsBean.TypeList>()
            if (mData!!.type_list!!.size > 0) {
                for (i in mData!!.type_list!!.indices) {
                    //这段代码打开可以避免两个联系人选择一样的type
//                    if (clickPosition == 0) {
//                        if (currentType2 == mData!!.type_list[i].type) {
//                            continue
//                        }
//                    }
//                    if (clickPosition == 1) {
//                        if (currentType1 == mData!!.type_list[i].type) {
//                            continue
//                        }
//                    }
                    temporaryList.add(mData!!.type_list[i])
                    arrayAdapter.add(mData!!.type_list!![i].type_name)
                }
            }

            val builder = AlertDialog.Builder(mActivity)
                    .setView(dialogView)
                    .setCancelable(true)
            var alertDialog = builder.create()

            alertDialog.setTitle("请选择关系")
            alertDialog.setView(dialogView)
            alertDialog.setCancelable(true)
            var mListView = dialogView.findViewById<ListView>(R.id.listview)
            mListView.setDividerHeight(0)
            mListView.setAdapter(arrayAdapter)
            mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                alertDialog.dismiss()
                //点击了上面的按钮
                if (clickPosition == 0) {
                    currentType1 = temporaryList[i].type
                    tv_auth_nexus1.text = temporaryList[i].type_name
                } else if (clickPosition == 1) {
                    currentType2 = temporaryList[i].type
                    tv_auth_nexus2.text = temporaryList[i].type_name
                }
            }
            alertDialog.show()
        }
    }

    private fun submitExtroContacts() {
        if (mData == null) {
            ToastUtil.showToast(mActivity, "数据错误")
            return
        }
        var name1 = et_auth_nexus_name1.text.toString().trim()
        var name2 = et_auth_nexus_name2.text.toString().trim()
        var phone1 = et_auth_nexus_phone.text.toString().trim()
        var phone2 = et_auth_nexus_phone2.text.toString().trim()
        if (TextUtils.isEmpty(name1) || TextUtils.isEmpty(name2) || TextUtils.isEmpty(phone1) || TextUtils.isEmpty(phone2)) {
            ToastUtil.showToast(mActivity, "请先完善相关信息")
            return
        }
        val userPhoneNum = UserUtil.getMobile(App.instance)
        if (phone1 == phone2) {
            ToastUtil.showToast(mActivity, "两个联系人的电话号码不能相同")
            return
        }
        if (phone1 == userPhoneNum) {
            ToastUtil.showToast(mActivity, "联系人电话不能和注册手机号一致")
            return
        }
        if (phone2 == userPhoneNum) {
            ToastUtil.showToast(mActivity, "联系人电话不能和注册手机号一致")
            return
        }
        if (phone1.length != 11 || phone2.length != 11 || !RegexUtil.IsTelephone(phone1) || !RegexUtil.IsTelephone(phone2)) {
            ToastUtil.showToast(mActivity, "手机号格式不正确")
            return
        }
        mMap.put(0.toString(), ExtroContactsBean.EmeContacts(currentType1, "", name1, phone1))
        mMap.put(1.toString(), ExtroContactsBean.EmeContacts(currentType2, "", name2, phone2))
        getAllContacts()
    }

    override fun initData() {
        mPresenter.getExtroContacts()
    }

    override fun processExtroData(data: ExtroContactsBean?) {
        if (data == null) {
            return
        }
        mData = data
        refreshUIAndData()
    }

    fun refreshUIAndData() {
        val eme_contacts = mData?.eme_contacts
        val type_list = mData?.type_list
        if (eme_contacts != null && eme_contacts.size != 0) {
            for (index in eme_contacts.indices) {
                val emeContacts = eme_contacts[index]
                if (index == 0) {
                    tv_auth_nexus1.text = emeContacts.type_name
                    et_auth_nexus_name1.setText(emeContacts.contact_name)
                    et_auth_nexus_phone.setText(emeContacts.contact_phone)
                    mMap.put(0.toString(), emeContacts)
                    currentType1 = emeContacts.type
                }
                if (index == 1) {
                    tv_auth_nexus2.text = emeContacts.type_name
                    et_auth_nexus_name2.setText(emeContacts.contact_name)
                    et_auth_nexus_phone2.setText(emeContacts.contact_phone)
                    mMap.put(1.toString(), emeContacts)
                    currentType2 = emeContacts.type
                }
            }
        } else {
            if (type_list != null && type_list.size != 0) {
                for (index in type_list.indices) {
                    val emeContacts = type_list[index]
                    if (index == 0) {
                        tv_auth_nexus1.text = emeContacts.type_name
                        currentType1 = emeContacts.type
                    }
                    if (index == 1) {
                        tv_auth_nexus2.text = emeContacts.type_name
                        currentType2 = emeContacts.type
                    }
                }
            }
        }
    }


    override fun processUploadResult() {
        LoadingUtil.hideLoadingDialog(mActivity)
//        EventBus.getDefault().post(RefreshCreditStatusEvent())
        TCAgent.onEvent(mActivity, TalkingDataParams.SAVE_CONTACTS)
        ToastUtil.showToast(mActivity, "认证成功")
        //发送event 到homeFragment2 查询认证
        EventBus.getDefault().post(getUserAuthStatus())
        if (!mActivity.isFinishing){
            finish()
        }

    }



    /**
     * 得到联系人信息
     */
    private fun getContacts() {

        //判断当前是否执行任务
        if (isGetContactsing) {
            return
        }
        //判断当前是否显示dialog
        if (alertDialog != null && alertDialog!!.isShowing()) {
            return
        }


        val rxPermissions = RxPermissions(this@AuthExtroContactsActivity)
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(object : Consumer<Boolean> {
            override fun accept(t: Boolean) = if (t) {
                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map({ contacts ->
                            mContacts.clear()
                            val contactsDialogDada = ArrayList<String>()
                            for (i in contacts.indices) {
                                val contactMap = contacts[i]
                                val name = contactMap["name"]
                                val phone = contactMap["phone"]
                                if (phone == null || phone.length != 11) {
                                    continue
                                }
                                mContacts.add(contactMap)
                                contactsDialogDada.add(name + "-" + phone)
                            }
                            contactsDialogDada
                        })
                        .subscribe(getObserver())
            } else {
                ToastUtil.showToast(mActivity, "请您设置打开通讯录读取")
            }
        })
    }

    private fun getObserver(): Observer<ArrayList<String>> {
        return object : Observer<ArrayList<String>> {
            //任务执行之前
            override fun onSubscribe(d: Disposable) {
                isGetContactsing = true
                LoadingUtil.showLoadingDialog(mActivity, false)
            }

            //任务执行之后
            override fun onNext(value: ArrayList<String>) {
                mContactsDialogDada = value
                isGetContactsing = false
            }

            //任务执行完毕
            override fun onComplete() {
                LoadingUtil.hideLoadingDialog(mActivity)
                showContactsDialog()
                isGetContactsing = false
                TCAgent.onEvent(mActivity, TalkingDataParams.GET_CONTACTS)
            }

            //任务异常
            override fun onError(e: Throwable) {
                isGetContactsing = false
                LoadingUtil.hideLoadingDialog(mActivity)
                ToastUtil.showToast(mActivity, "没有查找到联系人")
            }

        }
    }

    private fun getObservable(): Observable<List<java.util.HashMap<String, String>>> {
        return Observable.create { e ->
            if (!e.isDisposed) {
                val contacts = PhoneUtils.getAllContactInfo(mActivity)
                e.onNext(contacts)
                e.onComplete()
            }
        }
    }

    /**
     * 显示选择联系人的dialog
     */
    private fun showContactsDialog() {

        if (mContactsDialogDada == null || mContactsDialogDada?.size == 0) {
            ToastUtil.showToast(mActivity, "没有查找到联系人")
            return
        }
        if (!isFinishing) {
            val dialogView = View.inflate(this, R.layout.dialog_listview, null)
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            if (mContactsDialogDada!!.size > 0) {
                for (i in mContactsDialogDada!!.indices) {
                    arrayAdapter.add(mContactsDialogDada!![i])
                }
            }

            val builder = AlertDialog.Builder(mActivity)
                    .setView(dialogView)
                    .setCancelable(true)
            alertDialog = builder.create()

            alertDialog?.setTitle("请选择联系人")
            alertDialog?.setView(dialogView)
            alertDialog?.setCancelable(true)
            var mListView = dialogView.findViewById<ListView>(R.id.listview)
            mListView.setDividerHeight(0)
            mListView.setAdapter(arrayAdapter)
            mListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                alertDialog?.dismiss()
                mContactsPoistion = i
                refreshContactUI()
            }
            alertDialog?.show()
        }
    }

    private fun refreshContactUI() {
        val hashMap = mContacts[mContactsPoistion]
        val name = hashMap["name"]
        val phone = hashMap["phone"]
        if (mIsClickContacts1) {
            et_auth_nexus_name1.setText(name)
            et_auth_nexus_phone.setText(phone)
        } else {
            et_auth_nexus_name2.setText(name)
            et_auth_nexus_phone2.setText(phone)
        }
    }

    /**
     * 用户点击提交得到联系人信息
     */
    private fun getAllContacts() {
        val rxPermissions = RxPermissions(mActivity)
        rxPermissions.request(Manifest.permission.READ_CONTACTS).subscribe(object : Consumer<Boolean> {
            override fun accept(t: Boolean) = if (t) { //获得联系人权限
                getObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map<List<ContactsBean>> { contacts ->
                            val mContactsBeanList = ArrayList<ContactsBean>()
                            for (i in contacts.indices) {
                                val contactMap = contacts[i]
                                val name = contactMap["name"]
                                val phone = contactMap["phone"]
                                val mContactsBean = ContactsBean()
                                mContactsBean.contact_name = name
                                mContactsBean.contact_phone = phone
                                // 设置默认值  否则服务器报错
                                mContactsBean.group = ""
                                mContactsBean.nick = ""
                                mContactsBean.memo = ""
                                mContactsBeanList.add(mContactsBean)
                            }
                            mContactsBeanList
                        }
                        .subscribe(getObserver2())
            } else { //没有获得权限
                ToastUtil.showToast(mActivity, "请您设置打开通信录读取")
            }


        })
    }

    private fun getObserver2(): Observer<List<ContactsBean>> {
        return object : Observer<List<ContactsBean>> {
            //任务执行之前
            override fun onSubscribe(d: Disposable) {
                LoadingUtil.showLoadingDialog(mActivity, false)
            }

            //任务执行之后
            override fun onNext(value: List<ContactsBean>) {
                if (value.size == 0) {
                    ToastUtil.showToast(mActivity, "请您设置打开通信录读取")
                } else {
                    uploadContacts(value)
                }
            }

            //任务执行完毕
            override fun onComplete() {
                LoadingUtil.hideLoadingDialog(mActivity)
            }

            //任务异常
            override fun onError(e: Throwable) {
                //获取联系人失败，提交用户填入的联系人信息继续往下走
                mPresenter.saveExtroContacts(mMap)
            }

        }
    }

    /**
     * 上传联系人
     */
    private fun uploadContacts(list: List<ContactsBean>) {
        mPresenter.uploadContacts(list)

    }

    override fun processUploadContactsResult() {
        mPresenter.saveExtroContacts(mMap)
    }



}
