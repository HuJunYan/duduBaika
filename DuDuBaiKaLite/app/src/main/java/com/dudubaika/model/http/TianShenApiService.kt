package com.dudubaika.model.http

import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.TianShenIdNumInfoBean
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TianShenApiService {

    @POST(ApiSettings.GET_ID_NUM_INFO)
    fun getIdNumInfo(@Body body: RequestBody): Flowable<MyHttpResponse<TianShenIdNumInfoBean>>

    @Multipart
    @POST(ApiSettings.SAVE_BACK_ID_CARD_DATA)
    fun saveBackIdCardData(@Part("json") description: RequestBody, @Part file: MultipartBody.Part): Flowable<MyHttpResponse<Any>>

    @Multipart
    @POST(ApiSettings.UPLOAD_IMAGE)
    fun uploadImage(@Part("json") description: RequestBody, @Part file: MultipartBody.Part): Flowable<MyHttpResponse<Any>>

    @Multipart
    @POST(ApiSettings.UPLOAD_IMAGE)
    fun uploadBatchImage(@Part("json") description: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>

    @Multipart
    @POST(ApiSettings.CHECK_FACE)
    fun checkFace(@Part("json") description: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>

    @Multipart
    @POST(ApiSettings.SAVE_IDNUM_INFO)
    fun saveIdNumInfo(@Part("json") description: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>


}