package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.IDCardBean
import com.dudubaika.model.bean.SaveBackIdCardDataBean
import com.dudubaika.model.bean.TianShenIdNumInfoBean
import java.io.File

object IdentityContract {

    interface View : BaseContract.BaseView {
        fun showIdNumInfo(data: TianShenIdNumInfoBean)
        fun onSaveIdNumInfoResult()
        fun onCheckFaceResult()
        fun onOcrIdCardResult(data: IDCardBean)
    }

    interface Presenter<T> : BaseContract.BasePresenter<View> {
        fun getIdNumInfo()
        fun saveIdNumInfo(saveBackIdCardDataBean: SaveBackIdCardDataBean)
        fun checkFace(type: String, real_name: String, id_num: String, delta: String, bestPath: String, envPath: String)
        fun ocrIdCard(idFile: File)
        fun ding(flag: String, result: String)
    }

    /* interface View : BaseContract.BaseView {
      fun showIdNumInfo(data: TianShenIdNumInfoBean)
      fun onSaveBackIdCardData()
      fun onUpLoadImageResult()
      fun onBatchUpLoadImageResult()
      fun onUpLoadImage2FaceAddAddResult(data: IDCardBean)
      fun onCheckFaceResult()
      fun onOcrIdCardResult(data: IDCardBean)

  }

  interface Presenter<in T> : BaseContract.BasePresenter<T> {
      fun getIdNumInfo()
      fun saveBackIdCardData(saveBackIdCardDataBean: SaveBackIdCardDataBean)
      fun upLoadImage(type: String, path: String)
      fun upBatchLoadImage(type: String, paths: ArrayList<String>)
      fun upLoadImage2FaceAddAdd(image: ByteArray)
      fun checkFace(type: String, real_name: String, id_num: String, delta: String, bestPath: String, envPath: String)
      fun ocrIdCard(idFile: File)
  }*/

}