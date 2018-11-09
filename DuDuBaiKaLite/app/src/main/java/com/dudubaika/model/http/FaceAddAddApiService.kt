package com.dudubaika.model.http

import com.dudubaika.model.bean.IDCardBean
import com.dudubaika.model.bean.VerifyBean
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceAddAddApiService {

    @POST("ocridcard")
    fun uploadImage(@Body body: RequestBody): Flowable<IDCardBean>
    @POST(ApiSettings.OCR_IDCARD)
    fun ocrIdCard(@Body body: RequestBody): Flowable<IDCardBean>

    @POST(ApiSettings.VERIFY)
    fun idEqualsFace(@Body body: RequestBody): Flowable<VerifyBean>

}