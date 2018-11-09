package com.dudubaika.model.http

import com.dudubaika.model.bean.*
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewApiService {

    @POST(ReviewApiSettings.MINE)
    fun getMine(@Body body: RequestBody): Flowable<MyHttpResponse<MineBean>>

    @POST(ReviewApiSettings.SIGNIN)
    fun login(@Body body: RequestBody): Flowable<MyHttpResponse<LoginBean>>

    @POST(ApiSettings.GET_VERIFY_CODE)
    fun getVeryCode(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ReviewApiSettings.LOGIN_OUT_URL)
    fun logOut(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ReviewApiSettings.HOME_ARTICLE)
    fun getHomeArticle(@Body body: RequestBody): Flowable<MyHttpResponse<VerifyHomeDataBean>>

    @POST(ReviewApiSettings.MY_COLLECT)
    fun getMycollection(@Body body: RequestBody): Flowable<MyHttpResponse<VerifyHomeDataBean>>

    @POST(ReviewApiSettings.GO_COLLECT)
    fun collection(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ReviewApiSettings.DEL_COLLECT)
    fun unCollection(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ReviewApiSettings.GET_SEARCH_VIEW)
    fun getVerifyView(@Body body: RequestBody): Flowable<MyHttpResponse<VerifyHomeDataBean>>

    @POST(ReviewApiSettings.GET_REVIEW_PRODUCT_DETAIL)
    fun getTranficCard(@Body body: RequestBody) : Flowable<MyHttpResponse<VerifyProductDetailBean>>
}