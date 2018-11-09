package com.dudubaika.presenter.impl

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.IDCardBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.SaveBackIdCardDataBean
import com.dudubaika.model.bean.TianShenIdNumInfoBean
import com.dudubaika.model.http.*
import com.dudubaika.presenter.contract.IdentityContract
import com.dudubaika.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


class IdentityPresenter @Inject constructor() :  RxPresenter<IdentityContract.View>(),
        IdentityContract.Presenter<IdentityContract.View> {


    /**
     * 得到用户已经认证过的信息
     */
    override fun getIdNumInfo() {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.USER_ID, UserUtil.getUserId(App.instance))
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(TianShenApiManager.getIdNumInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<TianShenIdNumInfoBean>>())
                .compose(RxUtil.handleResult<TianShenIdNumInfoBean>())
                .subscribeWith(object : CommonSubscriber<TianShenIdNumInfoBean>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: TianShenIdNumInfoBean) {
                        mView?.showIdNumInfo(data)
                    }
                }))
    }

    /**
     * 上传身份证信息
     */
    override fun saveIdNumInfo(saveBackIdCardDataBean: SaveBackIdCardDataBean) {

        val jsonObject = JSONObject()
        jsonObject.put("type", saveBackIdCardDataBean.type)
        jsonObject.put("real_name", saveBackIdCardDataBean.real_name)
        jsonObject.put("id_num", saveBackIdCardDataBean.id_num)
        jsonObject.put("gender", saveBackIdCardDataBean.gender)
        jsonObject.put("nation", saveBackIdCardDataBean.nation)
        jsonObject.put("birthday", saveBackIdCardDataBean.birthday)
        jsonObject.put("birthplace", saveBackIdCardDataBean.birthplace)
        jsonObject.put("sign_organ", saveBackIdCardDataBean.sign_organ)
        jsonObject.put("valid_period", saveBackIdCardDataBean.valid_period)
        jsonObject.put("request_id", saveBackIdCardDataBean.request_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        //身份证照片
        val files = arrayListOf<MultipartBody.Part>()
        val file = File(saveBackIdCardDataBean.id_numPath)
        val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)
        val fileBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
        files.add(fileBody)

        addSubscribe(TianShenApiManager.saveIdNumInfo(body, files)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.SAVE_IDNUM_INFO) {
                    override fun onNext(data: Any) {
                        mView?.onSaveIdNumInfoResult()
                    }
                }))

    }

    /**
     * 校验身份一致性并保存
     */
    override fun checkFace(type: String, real_name: String, id_num: String, delta: String, bestPath: String, envPath: String) {

        val jsonObject = JSONObject()
        jsonObject.put("type", type)
        jsonObject.put("real_name", real_name)
        jsonObject.put("id_num", id_num)
        jsonObject.put("delta", delta)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        //身份证照片
        val files = arrayListOf<MultipartBody.Part>()
        //最佳活体照片
        val bestFile = File(bestPath)
        val bestRequestFile = RequestBody.create(MediaType.parse("application/octet-stream"), bestFile)
        val bestFileBody = MultipartBody.Part.createFormData("image_best", bestFile.name, bestRequestFile)
        //防攻击照片
        val envFile = File(envPath)
        val envRequestFile = RequestBody.create(MediaType.parse("application/octet-stream"), envFile)
        val envFileBody = MultipartBody.Part.createFormData("image_env", envFile.name, envRequestFile)

        files.add(bestFileBody)
        files.add(envFileBody)

        addSubscribe(TianShenApiManager.checkFace(body, files)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.CHECK_FACE) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.onCheckFaceResult()
                    }
                }))
    }


    override fun ding(flag: String, result: String) {
        /*val jsonObject = JSONObject()
        jsonObject.put("flag", flag)
        jsonObject.put("result", result)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(TianShenApiManager.buriedPoint(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult())
                .subscribeWith(object : CommonSubscriber<Any>(mView, false, ApiSettings.BURIED_POINT) {
                    override fun onNext(data: Any?) {
                    }
                }))*/
    }

    /**
     * 调用face++服务器得到身份证信息
     */
    override fun ocrIdCard(idFile: File) {

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("api_key", GlobalParams.FACE_ADD_ADD_APPKEY)
                .addFormDataPart("api_secret", GlobalParams.FACE_ADD_ADD_APPSECRET)
                .addFormDataPart("legality", "1")
                .addFormDataPart("image", idFile.name, RequestBody.create(MediaType.parse("multipart/form-data"), idFile))
                .build()

        addSubscribe(FaceAddAddApiManager.ocrIdCard(requestBody)
                .compose(RxUtil.rxSchedulerHelper<IDCardBean>())
                .subscribeWith(object : CommonSubscriber<IDCardBean>(mView, ApiSettings.OCR_IDCARD) {
                    override fun onNext(data: IDCardBean) {
                        mView?.onOcrIdCardResult(data)
                    }
                }))
    }



//    override fun getIdNumInfo() {
//
//        val jsonObject = JSONObject()
//        jsonObject.put(GlobalParams.USER_ID, UserUtil.getUserId(App.instance))
//        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
//        val body = RequsetUtil.getUcenterRequestBody(jsonObjectSigned)
//
//        addSubscribe(TianShenApiManager.getIdNumInfo(body)
//                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<TianShenIdNumInfoBean>>())
//                .compose(RxUtil.handleResult<TianShenIdNumInfoBean>())
//                .subscribeWith(object : CommonSubscriber<TianShenIdNumInfoBean>(mView!!, false, true, ApiSettings.GET_ID_NUM_INFO) {
//                    override fun onNext(t: TianShenIdNumInfoBean) {
//                        super.onNext(t)
//                        mView?.showIdNumInfo(t)
//                    }
//                }))
//    }
//
//    override fun saveBackIdCardData(saveBackIdCardDataBean: SaveBackIdCardDataBean) {
//        val jsonObject = JSONObject()
//        jsonObject.put(GlobalParams.USER_ID, UserUtil.getUserId(App.instance))
//        jsonObject.put("device_id", saveBackIdCardDataBean.device_id)
//        jsonObject.put("mobile", UserUtil.getMobile(App.instance))
//        jsonObject.put("real_name", saveBackIdCardDataBean.real_name)
//        jsonObject.put("id_num", saveBackIdCardDataBean.id_num)
//        jsonObject.put("gender", saveBackIdCardDataBean.gender)
//        jsonObject.put("nation", saveBackIdCardDataBean.nation)
//        jsonObject.put("birthday", saveBackIdCardDataBean.birthday)
//        jsonObject.put("birthplace", saveBackIdCardDataBean.birthplace)
//        jsonObject.put("sign_organ", saveBackIdCardDataBean.sign_organ)
//        jsonObject.put("valid_period", saveBackIdCardDataBean.valid_period)
//
//        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
//        val body = RequsetUtil.getUcenterRequestBody(jsonObjectSigned)
//        val file = File(saveBackIdCardDataBean.mFacePath)
//        val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)
//        val fileBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        addSubscribe(TianShenApiManager.saveBackIdCardData(body, fileBody)
//                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
//                .compose(RxUtil.handleResult<Any>())
//                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
//                    override fun onNext(data: Any) {
//                        super.onNext(data)
//                        mView?.onSaveBackIdCardData()
//                    }
//                }))
//    }
//
//
//    /**
//     * 上传图片到天神贷服务器
//     */
//    override fun upLoadImage(type: String, path: String) {
//
//        val jsonObject = JSONObject()
//        jsonObject.put(GlobalParams.USER_ID, UserUtil.getUserId(App.instance))
//        jsonObject.put("type", type)
//        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
//        val body = RequsetUtil.getUcenterRequestBody(jsonObjectSigned)
//        val file = File(path)
//        val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)
//        val fileBody = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        addSubscribe(TianShenApiManager.uploadImage(body, fileBody)
//                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
//                .compose(RxUtil.handleResult<Any>())
//                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
//                    override fun onNext(data: Any) {
//                        mView?.onUpLoadImageResult()
//                    }
//                }))
//    }
//
//    /**
//     * 上传多张照片到天神贷服务器
//     */
//    override fun upBatchLoadImage(type: String, paths: ArrayList<String>) {
//
//        val jsonObject = JSONObject()
//        jsonObject.put(GlobalParams.USER_ID, UserUtil.getUserId(App.instance))
//        jsonObject.put("type", type)
//        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
//        val body = RequsetUtil.getUcenterRequestBody(jsonObjectSigned)
//
//        val files = arrayListOf<MultipartBody.Part>()
//        var index = 0
//        for (path in paths) {
//            val file = File(path)
//            val requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file)
//            val fileBody = MultipartBody.Part.createFormData("file$index", file.name, requestFile)
//            files.add(fileBody)
//            index++
//        }
//
//        addSubscribe(TianShenApiManager.uploadBatchImage(body, files)
//                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
//                .compose(RxUtil.handleResult<Any>())
//                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
//                    override fun onNext(t: Any) {
//                        mView?.onBatchUpLoadImageResult()
//                    }
//                }))
//
//    }
//
//
//    /**
//     * 上传图片到face++服务器
//     * 检测和识别中华人民共和国第二代身份证。
//     */
//    override fun upLoadImage2FaceAddAdd(image: ByteArray) {
//
//        val imagePath = FileUtils.saveJPGFile(App.instance, image, "idcard_img")
//        val file = File(imagePath)
//
//        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("api_key", GlobalParams.FACE_ADD_ADD_APPKEY)
//                .addFormDataPart("api_secret", GlobalParams.FACE_ADD_ADD_APPSECRET)
//                .addFormDataPart("legality", "1")
//                .addFormDataPart("image", file.name, RequestBody.create(MediaType.parse("image/*"), file))
//                .build()
//
//        addSubscribe(FaceAddAddApiManager.uploadImage(requestBody)
//                .compose(RxUtil.rxSchedulerHelper<IDCardBean>())
//                .subscribeWith(object : CommonSubscriber<IDCardBean>(mView!!, true) {
//                    override fun onNext(data: IDCardBean) {
//                        mView?.onUpLoadImage2FaceAddAddResult(data)
//                    }
//                }))
//    }
//
//
//    /**
//     * 上传一段音频到服务器
//     */
//     fun upVideoToServices(file: File) {
//
////        val imagePath = FileUtils.saveJPGFile(App.instance, image, "idcard_img")
//        val file = File(file.toString())
//
//        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("api_key", GlobalParams.FACE_ADD_ADD_APPKEY)
//                .addFormDataPart("api_secret", GlobalParams.FACE_ADD_ADD_APPSECRET)
//                .addFormDataPart("legality", "1")
//                .addFormDataPart("mp4", file.name, RequestBody.create(MediaType.parse("mp4/*"), file))
//                .build()
//
//        addSubscribe(FaceAddAddApiManager.uploadImage(requestBody)
//                .compose(RxUtil.rxSchedulerHelper<IDCardBean>())
//                .subscribeWith(object : CommonSubscriber<IDCardBean>(mView!!, true) {
//                    override fun onNext(data: IDCardBean) {
//                        mView?.onUpLoadImage2FaceAddAddResult(data)
//                    }
//                }))
//    }

}