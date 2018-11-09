package com.dudubaika.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.base.GlobalParams
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.idcard.IDCardScanActivity
import com.dudubaika.liveness.LivenessActivity
import com.dudubaika.liveness.util.ConUtil
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.presenter.contract.IdentityContract
import com.dudubaika.presenter.impl.IdentityPresenter
import com.dudubaika.util.*
import com.megvii.idcardquality.IDCardQualityLicenseManager
import com.megvii.licensemanager.Manager
import com.megvii.livenessdetection.LivenessLicenseManager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_identity2.*
import kotlinx.android.synthetic.main.view_progress.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException

/*class IdentityActivity2 : BaseActivity<IdentityPresenter>(), IdentityContract.View {

    private val TAG = "abc"

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun getLayout(): Int = R.layout.activity_identity2

    override fun showStatus(data: AuthStatus) {
    }

    private var mMediaPlayer: MediaPlayer? = null

    private var mTianShenIdNumInfoBean: TianShenIdNumInfoBean? = null
    private var mIDCardBean: IDCardBean? = null
    private val mImageFullPath = arrayOfNulls<String>(7)
    private val imageFullPatyArray = arrayListOf<String>()
    private var mFacePath: String? = null //活体检测图片地址

    private var frontImg = byteArrayOf() //身份证正面
    private var backImg = byteArrayOf() //身份证反面
    private var mSaveIDCardFront: Boolean = false //是否上传身份证正面
    private var mCanScanFace: Boolean = false
    private var isCanPressBack = true

    private val IMAGE_TYPE_ID_CARD_FRONT = 20 //上传图片 type  身份证正面
    private val IMAGE_TYPE_ID_CARD_BACK = 21 //上传图片 type  身份证反面
    private val IMAGE_TYPE_SCAN_FACE = 25 //上传图片 type  活体检测图组
    private val CLICK_TYPE_IDENTITY: Int = 0
    private val CLICK_TYPE_IDENTITY2: Int = 1
    private val CLICK_TYPE_FACE: Int = 2
    private var mIsClickPosition: Int = 0

    override fun setStatusBar() {
        super.setStatusBar()
        StatusBarUtil.setPaddingSmart(mActivity, tb_identity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE -> {
                frontImg = data.getByteArrayExtra("idcardImg")
                mImageFullPath[0] = saveJPGFile(mActivity, frontImg, IMAGE_TYPE_ID_CARD_FRONT)
                upLoadImage()
            }
            GlobalParams.INTO_IDCARDSCAN_BACK_PAGE -> {
                backImg = data.getByteArrayExtra("idcardImg")
                mImageFullPath[1] = saveJPGFile(mActivity, backImg, IMAGE_TYPE_ID_CARD_BACK)
                upLoadImage()
            }
            GlobalParams.PAGE_INTO_LIVENESS -> {
                livenessResult(data.extras)
            }
        }

    }


    override fun initView() {
        //点击返回键
        iv_identity_back.setOnClickListener {
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
            saveIdentity()
        }
        mPresenter.getAuthStatush(true)
    }

    override fun initData() {
        mPresenter.getIdNumInfo()
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
        if (url == ApiSettings.GET_ID_NUM_INFO) {
            progress.ll_loading.visibility = View.GONE
            progress.ll_error.visibility = View.VISIBLE
            progress.ll_error.setOnClickListener {
                initData()
            }
        }
    }

    *//**
     * 得到身份认证信息
     *//*
    override fun showIdNumInfo(data: TianShenIdNumInfoBean) {
        mTianShenIdNumInfoBean = data
        val name = data.real_name
        val num = data.id_num
        tv_identity_name.text = name
        tv_identity_id_num.text = num

        ImageUtil.loadNoCache(applicationContext, iv_identity_auth_pic, data.front_idCard_url, R.drawable.ic_identity1)
        ImageUtil.loadNoCache(applicationContext, iv_identity_auth_pic2, data.back_idCard_url, R.drawable.ic_identity2)
        ImageUtil.loadNoCache(applicationContext, iv_identity_auth_face, data.face_url, R.drawable.ic_face)

        LogUtil.d(TAG, "设置--身份证正面--->" + data.front_idCard_url)
        LogUtil.d(TAG, "设置--身份证反面--->" + data.back_idCard_url)
        LogUtil.d(TAG, "设置--人脸照片--->" + data.face_url)

    }

    *//**
     * 保存身份认证信息回调
     *//*
    override fun onSaveBackIdCardData() {
        EventBus.getDefault().post(RefreshCreditStatusEvent())
        backActivity()
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
            val mParameters = mCamera!!.getParameters()
            mCamera!!.setParameters(mParameters)
        } catch (e: Exception) {
            canUse = false
        }

        if (mCamera != null) {
            mCamera!!.release()
        }
        return canUse
    }

    *//**
     * 请求相机权限 并根据结果 决定是否进行跳转
     *//*
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

    *//**
     * 点击了身份证正面
     *//*
    private fun onClickIdentity() {
        mIsClickPosition = 0
        idCardNetWorkWarranty()
    }

    *//**
     * 点击了身份证反面
     *//*
    private fun onClickIdentityBack() {
        mIsClickPosition = 1
        //先判断没有上传身份证正面
        if (!mSaveIDCardFront) {
            ToastUtil.showToast(mActivity, "先上传身份证正面")
            return
        }
        idCardNetWorkWarranty()
    }

    *//**
     * 点击了人脸识别
     *//*
    private fun onClickFace() {
        mIsClickPosition = 2
        //判断没有上传身份证背面
        if (!mCanScanFace) {
            if (TextUtils.isEmpty(mTianShenIdNumInfoBean?.back_idCard_url)) {
                ToastUtil.showToast(mActivity, "先上传身份证")
                return
            }
        }
        livenessNetWorkWarranty()
    }

    *//**
     * 身份证联网授权
     *//*
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

    *//**
     * 活体联网授权
     *//*
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


    *//**
     * 保存身份证信息
     *//*
    private fun saveIdentity() {

        var real_name = ""
        var id_num = ""
        var gender = ""
        var nation = ""
        var birthday = ""
        var birthplace = ""
        var sign_organ = ""
        var valid_period = ""

        if (mTianShenIdNumInfoBean != null) {
            real_name = mTianShenIdNumInfoBean!!.real_name
            id_num = mTianShenIdNumInfoBean!!.id_num
            gender = mTianShenIdNumInfoBean!!.gender
            nation = mTianShenIdNumInfoBean!!.nation
            birthday = mTianShenIdNumInfoBean!!.birthday
            birthplace = mTianShenIdNumInfoBean!!.birthplace
            sign_organ = mTianShenIdNumInfoBean!!.sign_organ
            valid_period = mTianShenIdNumInfoBean!!.valid_period
        }

        if (mIDCardBean != null) {
            real_name = mIDCardBean!!.name
            id_num = mIDCardBean!!.id_card_number
            gender = mIDCardBean!!.gender
            nation = mIDCardBean!!.race
            val birthdayBean = mIDCardBean!!.birthday
            birthday = birthdayBean.year + "年" + birthdayBean.month + "月" + birthdayBean.day + "日" //出生日期
            birthplace = mIDCardBean!!.address
            sign_organ = mIDCardBean!!.issued_by
            valid_period = mIDCardBean!!.valid_date
        }

        if (TextUtils.isEmpty(real_name)) {
            ToastUtil.showToast(mActivity, "请认证身份证正面")
            return
        }

        if (TextUtils.isEmpty(sign_organ)) {
            ToastUtil.showToast(mActivity, "请认证身份证反面")
            return
        }

        if (TextUtils.isEmpty(mFacePath)) {
            ToastUtil.showToast(mActivity, "请认证人脸识别")
            return
        }

        val mobile = UserUtil.getMobile(mActivity)
        val device_id = UserUtil.getDeviceId(mActivity)
        val saveBackIdCardDataBean = SaveBackIdCardDataBean(real_name, id_num, gender, nation,
                birthday, birthplace, sign_organ, valid_period, mFacePath!!, mobile, device_id)

        mPresenter.saveBackIdCardData(saveBackIdCardDataBean)
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
                val delta = bundle.getString("delta");
                val images = bundle.getSerializable("images") as Map<String, ByteArray>
                val image_best = images.get("image_best") as ByteArray
                val image_env = images.get("image_env") as ByteArray
                val myMap = MyMap()
                val myImages = mutableMapOf<String, ByteArray>()
                myImages.put("image_best", image_best)
                myImages.put("image_env", image_env)
                myMap.images = myImages
                saveLivenessImage(myMap)
                upLoadLivenessImage(myMap)
            } else {
                ToastUtil.showToast(mActivity, "检测失败，请重新检测")
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

    private fun saveLivenessImage(map: MyMap) {
        var i = 0
        for (entry in map.images.entries) {
            mImageFullPath[i + 2] = saveJPGFile(mActivity, entry.value, i + 25)
            i++
        }
    }

    *//**
     * 跳转到face++的页面
     *//*
    private fun gotoFaceAddAddActivity() {
        when (mIsClickPosition) {
        //正面
            0 -> {
                val idCardScanIntent = Intent(mActivity, IDCardScanActivity::class.java)
                idCardScanIntent.putExtra("isvertical", true)
                idCardScanIntent.putExtra("side", 0)
                startActivityForResult(idCardScanIntent, GlobalParams.INTO_IDCARDSCAN_FRONT_PAGE)
                ToastUtil.showToast(mActivity, "请拍摄身份证正面")
            }
        //反面
            1 -> {
                val idCardScanBackIntent = Intent(mActivity, IDCardScanActivity::class.java)
                idCardScanBackIntent.putExtra("isvertical", true)
                idCardScanBackIntent.putExtra("side", 1)
                startActivityForResult(idCardScanBackIntent, GlobalParams.INTO_IDCARDSCAN_BACK_PAGE)
                ToastUtil.showToast(mActivity, "请拍摄身份证反面")
            }
        //人脸检测
            2 -> {
                LogUtil.d("http", "跳转到人脸检测")
                isCanPressBack = false
                startActivityForResult(Intent(mActivity, LivenessActivity::class.java), GlobalParams.PAGE_INTO_LIVENESS)
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

    fun saveJPGFile(mContext: Context, data: ByteArray?, type: Int): String? {
        if (data == null)
            return null
        val mediaStorageDir = mContext.getExternalFilesDir("idCardAndLiveness") ?: return null
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        var bos: BufferedOutputStream? = null
        var fos: FileOutputStream? = null
        try {
            val jpgFileName = type.toString() + ".jpg"
            fos = FileOutputStream(mediaStorageDir.toString() + "/" + jpgFileName)
            bos = BufferedOutputStream(fos)
            bos.write(data)
            return mediaStorageDir.absolutePath + "/" + jpgFileName
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
        return null
    }

    *//**
     * 上传身份证图片
     *//*
    private fun upLoadImage() {
        var path = ""
        var type = ""
        when (mIsClickPosition) {
            0 -> {
                type = IMAGE_TYPE_ID_CARD_FRONT.toString()
                path = mImageFullPath[0]!!
            }
            1 -> {
                type = IMAGE_TYPE_ID_CARD_BACK.toString()
                path = mImageFullPath[1]!!
            }
            2 -> {
            }
        }
        mPresenter.upLoadImage(type, path)
    }

    *//**
     * 上传扫脸照片
     *//*
    private fun upLoadLivenessImage(map: MyMap) {
        try {
            val count = map.images.size
            for (i in 0..count - 1) {
                imageFullPatyArray.add(mImageFullPath[i + 2]!!)
                LogUtil.d("abc", "上传扫脸照片--->" + mImageFullPath[i + 2]!!)
            }
            mPresenter.upBatchLoadImage(IMAGE_TYPE_SCAN_FACE.toString(), imageFullPatyArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    *//**
     * 上传身份证图片回调
     *//*
    override fun onUpLoadImageResult() {
        when (mIsClickPosition) {
            0 -> {
                getIdcardInfo(frontImg)
            }
            1 -> {
                getIdcardInfo(backImg)
            }
        }
    }

    *//**
     * 上传活体检测图片回调
     *//*
    override fun onBatchUpLoadImageResult() {
        mFacePath = imageFullPatyArray[1]
        ImageUtil.loadNoCache(applicationContext, iv_identity_auth_face, mFacePath!!, R.drawable.ic_item_placeholder)

        LogUtil.d(TAG, "设置人脸照片-->" + mFacePath!!)

    }


    *//**
     * 上传图片到face++回调
     *//*
    override fun onUpLoadImage2FaceAddAddResult(data: IDCardBean) {

        when (mIsClickPosition) {
            0 -> {
                mIDCardBean = data
                if (mIDCardBean == null) {
                    ToastUtil.showToast(mActivity, "身份证信息读取失败，请重新扫描身份证正面！")
                    return
                }
                refreshNameAndNumUI()
            }
            1 -> {
                mIDCardBean?.issued_by = data.issued_by
                mIDCardBean?.valid_date = data.valid_date
                setImageSource(backImg)
                mCanScanFace = true

                LogUtil.d(TAG, "得到身份证反面信息---issued_by-->" + mIDCardBean?.issued_by)
                LogUtil.d(TAG, "得到身份证反面信息---valid_date-->" + mIDCardBean?.valid_date)
            }
        }

    }

    *//**
     * 得到身份证正面信息
     *//*
    private fun getIdcardInfo(data: ByteArray) {
        mPresenter.upLoadImage2FaceAddAdd(data)
    }

    *//**
     * 刷新名字和身份证号UI
     *//*
    private fun refreshNameAndNumUI() {
        val name = mIDCardBean?.name
        val num = mIDCardBean?.id_card_number
        tv_identity_name.text = name
        tv_identity_id_num.text = num
        setImageSource(frontImg)
        mSaveIDCardFront = true

        LogUtil.d(TAG, "得到身份证正面信息---name-->$name")
        LogUtil.d(TAG, "得到身份证正面信息---num-->$num")

    }


    private fun setImageSource(imageSource: ByteArray) {
        val idcardBmp = BitmapFactory.decodeByteArray(imageSource, 0, imageSource.size)
        val drawable = BitmapDrawable(idcardBmp)
        when (mIsClickPosition) {
            0 -> {
                iv_identity_auth_pic.setImageDrawable(drawable)
                LogUtil.d(TAG, "设置--身份证正面---drawable>")
            }
            1 -> {
                iv_identity_auth_pic2.setImageDrawable(drawable)
                LogUtil.d(TAG, "设置--身份证反面---drawable>")
            }
        }
    }
}*/
