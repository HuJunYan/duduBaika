package com.dudubaika.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.ext.toast
import com.dudubaika.idcard.IDCardScanActivity
import com.dudubaika.liveness.LivenessActivity
import com.dudubaika.liveness.util.ConUtil
import com.dudubaika.liveness.util.LegalityUtil
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.*
import com.dudubaika.presenter.contract.IdentityContract
import com.dudubaika.presenter.impl.IdentityPresenter
import com.dudubaika.util.*
import com.megvii.idcardquality.IDCardQualityLicenseManager
import com.megvii.licensemanager.Manager
import com.megvii.livenessdetection.LivenessLicenseManager
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_identity.*
import kotlinx.android.synthetic.main.dialog_select.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.File

class IdentityActivity : BaseActivity<IdentityPresenter>(), IdentityContract.View {
    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showError(url: String, msg: String) {

    }

    private val TAG = "abc"

    override fun getLayout(): Int = R.layout.activity_identity


    private var mMediaPlayer: MediaPlayer? = null
    private var isEditIdentity: String? = ""
    private var isCanEditIdentity: Boolean = true

    //天神服务器定义的bean
    private var mTianShenIdNumInfoBean: TianShenIdNumInfoBean? = null
    //face++ ocr返回的身份证的bean
    private var mIDCardBean: IDCardBean = IDCardBean(name = "", time_used = "", gender = "", id_card_number = "", request_id = "",
            side = "", race = "", address = "", issued_by = "", valid_date = "",
            birthday = IDCardBean.Birthday(month = "", day = "", year = ""),
            legality = IDCardBean.Legality(0.0, 0.0, 0.0, 0.0, 0.0))

    //身份证正面，身份证反面，人脸照片本地路径
    private val mImageFullPath = arrayOfNulls<String>(4)

    var delta = "" //face++防攻击校验会用到

    private var frontImg = byteArrayOf() //身份证正面二进制流
    private var backImg = byteArrayOf() //身份证反面二进制流


    val INTO_IDCARDSCAN_FRONT_PAGE = 50 // 身份证正面
    val INTO_IDCARDSCAN_BACK_PAGE = 49 // 身份证反面
    val PAGE_INTO_LIVENESS = 51 //活体检测
    val INTO_SELECT_GALLERY_FRONT = 52 // 用户现在图库（身份证正面）
    val INTO_SELECT_GALLERY_BACK = 53 // 用户现在图库（身份证反面）

    private val IMAGE_TYPE_ID_CARD_FRONT = 20 //上传图片 type  身份证正面
    private val IMAGE_TYPE_ID_CARD_BACK = 21 //上传图片 type  身份证反面
    private val IMAGE_TYPE_SCAN_FACE = 25 //上传图片 最佳人脸照片
    private val IMAGE_TYPE_ENV = 26 //上传图片 防攻击照片
    private val CLICK_TYPE_IDENTITY: Int = 0
    private val CLICK_TYPE_IDENTITY2: Int = 1
    private val CLICK_TYPE_FACE: Int = 2
    private var mIsClickPosition: Int = 0

    private val DING_ID_CARD_FRONT_START = "21" //点击身份证正面
    private val DING_ID_CARD_FRONT_END = "22" //身份证正面扫描退出时
    private val DING_ID_CARD_BACK_START = "23" //点击身份证反面
    private val DING_ID_CARD_BACK_END = "24" //身份证反面扫描退出时
    private val DING_FACE_START = "25" //点击人脸检测
    private val DING_ID_CARD_FRONT_START_CAMERA = "26" //身份证正面启动扫描时_摄像头
    private val DING_ID_CARD_FRONT_START_GALLERY = "27" //身份证正面启动扫描时_相册

    private val DING_RESULT_DEFAULT = "-1" //默认值
    private val DING_RESULT_SUCCESS = "1" //成功
    private val DING_RESULT_FAILURE = "2" //成功

    val IMAGE_UNSPECIFIED = "image/*"
    private var mWithdraw_money: String? = ""

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.setPaddingSmart(mActivity, tb_identity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
        //摄像头拍摄正面返回
            INTO_IDCARDSCAN_FRONT_PAGE -> {
                frontImg = data.getByteArrayExtra("idcardImg")
                mImageFullPath[0] = FileUtils.authIdentitySaveJPGFile(mActivity, frontImg, IMAGE_TYPE_ID_CARD_FRONT)
                getIdcardInfo(mImageFullPath[0]!!)
            }
        //摄像头拍摄反面返回
            INTO_IDCARDSCAN_BACK_PAGE -> {
                backImg = data.getByteArrayExtra("idcardImg")
                mImageFullPath[1] = FileUtils.authIdentitySaveJPGFile(mActivity, backImg, IMAGE_TYPE_ID_CARD_BACK)
                getIdcardInfo(mImageFullPath[1]!!)
            }
        //图库选择身份证正面返回
            INTO_SELECT_GALLERY_FRONT -> {
                val uri = data.data
                val bmp = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                val bitmap = BitmapUtils.resizeBitmap(bmp)
                frontImg = BitmapUtils.Bitmap2Bytes(bitmap)
                mImageFullPath[0] = FileUtils.authIdentitySaveJPGFile(mActivity, frontImg, IMAGE_TYPE_ID_CARD_FRONT)
                getIdcardInfo(mImageFullPath[0]!!)
            }
        //图库选择身份证反面返回
            INTO_SELECT_GALLERY_BACK -> {
                val uri = data.data
                val bmp = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                val bitmap = BitmapUtils.resizeBitmap(bmp)
                backImg = BitmapUtils.Bitmap2Bytes(bitmap)
                mImageFullPath[1] = FileUtils.authIdentitySaveJPGFile(mActivity, backImg, IMAGE_TYPE_ID_CARD_BACK)
                getIdcardInfo(mImageFullPath[1]!!)
            }
        //扫脸返回
            PAGE_INTO_LIVENESS -> {
                livenessResult(data.extras)
            }
        }
    }

    override fun initView() {
        defaultTitle="身份认证"
        //点击返回键
        tv_identity_back.setOnClickListener {
            backActivity()
        }
        //点击了身份证正面
        iv_identity_auth_pic.setOnClickListener {
            requestPermissionsToNextActivity(CLICK_TYPE_IDENTITY)
        }
        //点击了身份证反面
        iv_identity_auth_pic2.setOnClickListener {
            requestPermissionsToNextActivity(CLICK_TYPE_IDENTITY2)
        }
        //点击了人脸识别
        iv_identity_auth_face.setOnClickListener {
            requestPermissionsToNextActivity(CLICK_TYPE_FACE)
        }
        //点击了保存
        tv_identity_save.setOnClickListener {
            checkFace()
        }
        //判断身份证按钮是否可自己编辑
//        isEditIdentity = UserUtil.getIsEditIdentity(mActivity)

       /* if (TextUtils.isEmpty(isEditIdentity)) {
            isCanEditIdentity = false
        } else {
            isCanEditIdentity = "1" == isEditIdentity
        }*/
        isCanEditIdentity = true

        if (isCanEditIdentity) {
            iv_identity_name_edit.visibility = View.VISIBLE
            et_identity_name.isEnabled = true
            iv_identity_id_edit.visibility = View.VISIBLE
            et_identity_id_num.isEnabled = true
            tv_identity_tip.text = resources.getString(R.string.identity_tip3)
        } else {
            iv_identity_name_edit.visibility = View.GONE
            et_identity_name.isEnabled = false
            iv_identity_id_edit.visibility = View.GONE
            et_identity_id_num.isEnabled = false
            tv_identity_tip.text = resources.getString(R.string.identity_tip2)
        }

    }

    override fun initData() {
        /*这里不获取数据
        val mIntent = intent
        if (mIntent != null) {
            mWithdraw_money = mIntent.getStringExtra("withdraw_money")
        }
        LogUtil.e("传过来的钱是", mWithdraw_money)
        mPresenter.getIdNumInfo()*/
    }

    override fun showProgress() =Unit

    override fun hideProgress() =Unit


    /**
     * 从天神贷服务器得到身份认证信息
     */
    override fun showIdNumInfo(data: TianShenIdNumInfoBean) {

        mTianShenIdNumInfoBean = data
        val name = data.real_name
        val id_num = data.id_num
        val issued_by = data.sign_organ
        val valid_date = data.valid_period

        mIDCardBean.name = name
        mIDCardBean.id_card_number = id_num
        mIDCardBean.issued_by = issued_by
        mIDCardBean.valid_date = valid_date

        et_identity_name.setText(name)
        et_identity_id_num.setText(id_num)

        if (TextUtils.isEmpty(mIDCardBean.name)) {
            ll_identity_name_num.visibility = View.GONE
        } else {
            ll_identity_name_num.visibility = View.VISIBLE
        }

        ImageUtil.load(applicationContext, data.front_idCard_url, R.drawable.ic_identity1, iv_identity_auth_pic)
        ImageUtil.load(applicationContext, data.back_idCard_url, R.drawable.ic_identity2, iv_identity_auth_pic2)
        ImageUtil.load(applicationContext, data.face_url, R.drawable.ic_face, iv_identity_auth_face)

    }

    //检查是否是魅族Flyme系统 如果是 则判断是否有相机权限 不是则不判断
    private fun checkPermissionForFlyme(): Boolean {
        val isFlyme = RomUtils.FlymeSetStatusBarLightMode() //是否是魅族
        val isReallyHasPermission: Boolean
        if (isFlyme) {
            isReallyHasPermission = isCameraCanUse()
        } else {
            isReallyHasPermission = true
        }
        return isReallyHasPermission
    }

    //针对魅族Flyme系统判断是否有相机权限
    private fun isCameraCanUse(): Boolean {
        var canUse = true
        var mCamera: Camera? = null
        try {
            mCamera = Camera.open()
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            val mParameters = mCamera.getParameters()
            mCamera.setParameters(mParameters)
        } catch (e: Exception) {
            canUse = false
        }

        if (mCamera != null) {
            mCamera.release()
        }
        return canUse
    }

    /**
     * 请求相机权限 并根据结果 决定是否进行跳转
     */
    private fun requestPermissionsToNextActivity(type: Int) {
        val isReallyHasPermission = checkPermissionForFlyme()
        if (!isReallyHasPermission) {
            ToastUtil.showToast(this, "请去设置开启照相机权限")
            return
        }
        val rxPermissions = RxPermissions(mActivity)
        rxPermissions.request(android.Manifest.permission.CAMERA).subscribe {
            if (it) {
                when (type) {
                    CLICK_TYPE_IDENTITY -> onClickIdentity()
                    CLICK_TYPE_IDENTITY2 -> onClickIdentityBack()
                    CLICK_TYPE_FACE -> onClickFace()
                }
            } else {
                ToastUtil.showToast(mActivity, "请去设置开启照相机权限")
            }
        }
    }

    /**
     * 点击了身份证正面
     */
    private fun onClickIdentity() {
        mIsClickPosition = 0
        if ("1" == mTianShenIdNumInfoBean?.is_show_gallery) {
            showSelectDialog()
        } else {
//            mPresenter.ding(DING_ID_CARD_FRONT_START_CAMERA, DING_RESULT_DEFAULT)
            idCardNetWorkWarranty()
        }
    }

    /**
     * 点击了身份证反面
     */
    private fun onClickIdentityBack() {
        mIsClickPosition = 1

        if (TextUtils.isEmpty(mIDCardBean.name)) {
            ToastUtil.showToast(mActivity, "请先认证身份证正面", Toast.LENGTH_SHORT)
            return
        }

        if ("1" == mTianShenIdNumInfoBean?.is_show_gallery) {
            showSelectDialog()
        } else {
            idCardNetWorkWarranty()
        }
    }

    /**
     * 点击了人脸识别
     */
    private fun onClickFace() {
        mIsClickPosition = 2

        if (TextUtils.isEmpty(mIDCardBean.name)) {
            ToastUtil.showToast(mActivity, "请先认证身份证正面", Toast.LENGTH_SHORT)
            return
        }

        if (TextUtils.isEmpty(mIDCardBean.valid_date)) {
            ToastUtil.showToast(mActivity, "请先认证身份反面", Toast.LENGTH_SHORT)
            return
        }

        livenessNetWorkWarranty()
    }

    /**
     * 显示选择本地图库，还是使用摄像头的对话框
     */
    private fun showSelectDialog() {

        val mDialog = Dialog(this, R.style.MyDialog)
        val mLayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mLayoutInflater.inflate(R.layout.dialog_select, null, false)
        mDialog.setContentView(view)
        val layoutParams = view.getLayoutParams()
        layoutParams.width = (300 * resources.displayMetrics.density).toInt()
        view.layoutParams = layoutParams
        mDialog.setCancelable(true)
        view.tv_dialog_select_gallery.setOnClickListener {
            if (mIsClickPosition == 0) {
//                mPresenter.ding(DING_ID_CARD_FRONT_START_GALLERY, DING_RESULT_DEFAULT)
            }
            showSelectGallery()
            mDialog.dismiss()
        }
        view.tv_dialog_select_camera.setOnClickListener {
            if (mIsClickPosition == 0) {
//                mPresenter.ding(DING_ID_CARD_FRONT_START_CAMERA, DING_RESULT_DEFAULT)
            }
            idCardNetWorkWarranty()
            mDialog.dismiss()
        }
        mDialog.show()
    }

    /**
     * 跳转到系统图库选择照片
     */
    private fun showSelectGallery() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED)

        var requestCode = 0
        if (mIsClickPosition == 0) {
            requestCode = INTO_SELECT_GALLERY_FRONT
        } else {
            requestCode = INTO_SELECT_GALLERY_BACK
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 校验身份一致性并保存
     */
    private fun checkFace() {


        if (TextUtils.isEmpty(mIDCardBean.name)) {
            ToastUtil.showToast(mActivity,"请先认证身份证正面")
            return
        }

        if (TextUtils.isEmpty(mIDCardBean.id_card_number) || !RegexUtil.isIDCardNumber(mIDCardBean.id_card_number)) {
            ToastUtil.showToast(mActivity,"身份证号码不合法，请重新认证")
            return
        }

        if (TextUtils.isEmpty(mIDCardBean.issued_by)) {
            toast("请先认证身份反面")
            return
        }

        if (TextUtils.isEmpty(mImageFullPath[2])) {
            toast("请先认证人脸识别")
            return
        }

        if (TextUtils.isEmpty(mImageFullPath[3])) {
            toast("请先认证人脸识别")
            return
        }

        val type = "25"
//        val real_name = mIDCardBean.name
//        val id_num = mIDCardBean.id_card_number
        val real_name = et_identity_name.text.toString()
        val id_num = et_identity_id_num.text.toString()
        val bestPath = mImageFullPath[2]!!
        val envPath = mImageFullPath[3]!!
        if (TextUtils.isEmpty(real_name)) {
            toast("请填写您的姓名")
            return
        }
        if (TextUtils.isEmpty(id_num)) {
            toast("请填写您的身份证号")
            return
        }

        mPresenter.checkFace(type, real_name, id_num, delta, bestPath, envPath)
    }

    /**
     * 身份证联网授权
     */
    private fun idCardNetWorkWarranty() {
        doAsync {
            val manager = Manager(App.instance)
            val idCardLicenseManager = IDCardQualityLicenseManager(mActivity)
            manager.registerLicenseManager(idCardLicenseManager)
            manager.takeLicenseFromNetwork(FaceUtil.getUUIDString(mActivity))
            uiThread {
                if (idCardLicenseManager.checkCachedLicense() > 0) {
                    gotoFaceAddAddActivity()
                } else {
                    ToastUtil.showToast(mActivity, "联网授权失败，请重新认证")
                }
            }
        }
    }

    /**
     * 活体联网授权
     */
    private fun livenessNetWorkWarranty() {
        doAsync {
            val manager = Manager(mActivity)
            val licenseManager = LivenessLicenseManager(mActivity)
            manager.registerLicenseManager(licenseManager)
            manager.takeLicenseFromNetwork(ConUtil.getUUIDString(mActivity))
            uiThread {
                if (licenseManager.checkCachedLicense() > 0) {
                    gotoFaceAddAddActivity()
                } else {
                    ToastUtil.showToast(mActivity, "联网授权失败，请重新认证")
                }
            }
        }
    }


    private fun livenessResult(bundle: Bundle) {

        val resultOBJ = bundle.getString("result")
        try {
            val result = JSONObject(resultOBJ)
            val resID = result.getInt("resultcode")
            checkID(resID);
            val isSuccess = result.getString("result").equals(
                    resources.getString(R.string.verify_success))
            if (isSuccess) {
                delta = bundle.getString("delta");
                val images = bundle.getSerializable("images") as Map<String, ByteArray>
                //最佳人脸照片
                val image_best = images.get("image_best") as ByteArray
                //防攻击照片
                val image_env = images.get("image_env") as ByteArray

                mImageFullPath[2] = FileUtils.authIdentitySaveJPGFile(mActivity, image_best, IMAGE_TYPE_SCAN_FACE)
                mImageFullPath[3] = FileUtils.authIdentitySaveJPGFile(mActivity, image_env, IMAGE_TYPE_ENV)
                iv_identity_auth_face.setImageBitmap(BitmapFactory.decodeByteArray(image_best, 0, image_best.size))

            } else {
                toast("检测失败，请重新检测")
                gotoFaceAddAddActivity()
            }

        } catch (e: Exception) {
            e.printStackTrace();
        }
    }


    private fun checkID(resID: Int) {
        if (resID == R.string.verify_success) {
            doPlay(R.raw.meglive_success)
        } else if (resID == R.string.liveness_detection_failed_not_video) {
            doPlay(R.raw.meglive_failed)
        } else if (resID == R.string.liveness_detection_failed_timeout) {
            doPlay(R.raw.meglive_failed)
        } else if (resID == R.string.liveness_detection_failed) {
            doPlay(R.raw.meglive_failed)
        } else {
            doPlay(R.raw.meglive_failed)
        }
    }

    /**
     * 跳转到face++的页面
     */
    private fun gotoFaceAddAddActivity() {
        when (mIsClickPosition) {
        //正面
            0 -> {
                val idCardScanIntent = Intent(mActivity, IDCardScanActivity::class.java)
                idCardScanIntent.putExtra("isvertical", true)
                idCardScanIntent.putExtra("side", 0)
                startActivityForResult(idCardScanIntent, INTO_IDCARDSCAN_FRONT_PAGE)
            }
        //反面
            1 -> {
                val idCardScanBackIntent = Intent(mActivity, IDCardScanActivity::class.java)
                idCardScanBackIntent.putExtra("isvertical", true)
                idCardScanBackIntent.putExtra("side", 1)
                startActivityForResult(idCardScanBackIntent, INTO_IDCARDSCAN_BACK_PAGE)
            }
        //人脸检测
            2 -> {
                LogUtil.d(TAG, "跳转到人脸检测")
                startActivityForResult(Intent(mActivity, LivenessActivity::class.java), PAGE_INTO_LIVENESS)
            }
        }
    }

    private fun doPlay(rawId: Int) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
        mMediaPlayer?.reset()
        try {
            val localAssetFileDescriptor = resources
                    .openRawResourceFd(rawId)
            mMediaPlayer?.setDataSource(
                    localAssetFileDescriptor.fileDescriptor,
                    localAssetFileDescriptor.startOffset,
                    localAssetFileDescriptor.length)
            mMediaPlayer?.prepare()
            mMediaPlayer?.start()
        } catch (localIOException: Exception) {
            localIOException.printStackTrace()
        }
    }

    /**
     * 得到身份证信息
     */
    private fun getIdcardInfo(path: String) {
        val file = File(path)
        mPresenter.ocrIdCard(file)
    }

    /**
     * face++OCR回调
     */
    override fun onOcrIdCardResult(data: IDCardBean) {

        if (data?.legality != null) {
            if (!LegalityUtil.isIDPhoto(data.legality)) {
                ToastUtil.showToast(mActivity, "请使用本人真实身份证进行扫描，不要使用复印件和电子照片")
                return
            }
        }

        when (mIsClickPosition) {
            0 -> {
                if (TextUtils.isEmpty(data.name)) {
                    ToastUtil.showToast(mActivity, "身份证正面信息读取失败，请重试")
                    mPresenter.ding(DING_ID_CARD_FRONT_END, DING_RESULT_FAILURE)
                    return
                }

                if (TextUtils.isEmpty(data.id_card_number) || !RegexUtil.isIDCardNumber(data.id_card_number)) {
                    ToastUtil.showToast(mActivity, "身份证号码不合法，请重试")
                    return
                }

                mIDCardBean.race = data.race
                mIDCardBean.name = data.name
                mIDCardBean.time_used = data.time_used
                mIDCardBean.gender = data.gender
                mIDCardBean.id_card_number = data.id_card_number
                mIDCardBean.request_id = data.request_id
                mIDCardBean.birthday = data.birthday
                mIDCardBean.legality = data.legality
                mIDCardBean.address = data.address
                mIDCardBean.side = data.side
//                mPresenter.ding(DING_ID_CARD_FRONT_END, DING_RESULT_SUCCESS)
                saveIdNumInfo()
            }
            1 -> {
                if (TextUtils.isEmpty(data.issued_by)) {
                    ToastUtil.showToast(mActivity, "身份证反面信息读取失败，请重试")
//                    mPresenter.ding(DING_ID_CARD_BACK_END, DING_RESULT_FAILURE)
                    return
                }
                mIDCardBean.issued_by = data.issued_by
                mIDCardBean.valid_date = data.valid_date
//                mPresenter.ding(DING_ID_CARD_BACK_END, DING_RESULT_SUCCESS)
                saveIdNumInfo()
            }
        }
    }

    /**
     * 保存身份证正反面信息到天神贷服务器
     */
    private fun saveIdNumInfo() {

        val type = if (mIsClickPosition == 0) "20" else "21"
        val real_name = mIDCardBean.name
        val id_num = mIDCardBean.id_card_number
        val gender = mIDCardBean.gender
        val nation = mIDCardBean.race
        val birthdayBean = mIDCardBean.birthday
        val birthday = birthdayBean.year + "年" + birthdayBean.month + "月" + birthdayBean.day + "日" //出生日期
        val birthplace = mIDCardBean.address
        val sign_organ = mIDCardBean.issued_by
        val valid_period = mIDCardBean.valid_date
        val request_id = mIDCardBean.request_id
        val id_numPath = if (mIsClickPosition == 0) mImageFullPath[0]!! else mImageFullPath[1]!!

        val saveBackIdCardDataBean = SaveBackIdCardDataBean(type = type, real_name = real_name, id_num = id_num,
                gender = gender, nation = nation, birthday = birthday,
                birthplace = birthplace, sign_organ = sign_organ, valid_period = valid_period,
                request_id = request_id, id_numPath = id_numPath)

        mPresenter.saveIdNumInfo(saveBackIdCardDataBean)
    }

    /**
     * 保存身份认正反面证信息回调
     */
    override fun onSaveIdNumInfoResult() {

        if (mIsClickPosition == 0) {
            //刷新身份证正面信息
            val name = mIDCardBean.name
            val num = mIDCardBean.id_card_number

            if (et_identity_id_num.text.toString() != num) {//如果第二次认证身份证号码不一致就清空身份证反面信息和人脸照片
                iv_identity_auth_pic2.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_identity2))
                iv_identity_auth_face.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_face))
                mIDCardBean.issued_by = ""
                mIDCardBean.valid_date = ""
                mImageFullPath[2] = ""
                mImageFullPath[3] = ""
            }

            if (TextUtils.isEmpty(name)) {
                ll_identity_name_num.visibility = View.GONE
            } else {
                ll_identity_name_num.visibility = View.VISIBLE
                et_identity_name.setText(name)
                et_identity_id_num.setText(num)
                iv_identity_auth_pic.setImageBitmap(BitmapFactory.decodeByteArray(frontImg, 0, frontImg.size))
            }
        } else if (mIsClickPosition == 1) { //刷新身份证反面信息
            iv_identity_auth_pic2.setImageBitmap(BitmapFactory.decodeByteArray(backImg, 0, backImg.size))
        }
    }

    /**
     * 校验身份一致性并保存
     */
    override fun onCheckFaceResult() {
        //发送event 到homeFragment2 查询认证
        EventBus.getDefault().post(getUserAuthStatus())
       if (!mActivity.isFinishing){
            finish()
       }

    }



    override fun onPause() {
        super.onPause()
        if (!TextUtils.isEmpty(mWithdraw_money)) {
            finish()
        }
    }

}