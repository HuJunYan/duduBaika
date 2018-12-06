package com.dudubaika.model.http

import com.dudubaika.model.bean.*
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET(ApiSettings.ORGANIZATION_REPOS)
    fun getOrganizationRepos(@Path(ApiSettings.PATH_ORGANIZATION) organizationName: String,
                             @Query(ApiSettings.PARAM_REPOS_TYPE) reposType: String, @Query(ApiSettings.PAGE) page: String, @Query(ApiSettings.PER_PAGE) per_page: String): Flowable<MutableList<Repository>>


    @POST(ApiSettings.GET_BANNER_LIST)
    fun getBannerList(@Body body: RequestBody): Flowable<MyHttpResponse<BannerListBean>>

    @POST(ApiSettings.GET_PRODUCT_LIST)
    fun getProductList(@Body body: RequestBody): Flowable<MyHttpResponse<ProductListBean>>

    @POST(ApiSettings.GET_UESR_AUTH_RESULT)
    fun getUserAuthResult(@Body body: RequestBody): Flowable<MyHttpResponse<UserAuthResultBean>>

    @POST(ApiSettings.SIGNIN)
    fun login(@Body body: RequestBody): Flowable<MyHttpResponse<LoginBean>>

    @POST(ApiSettings.GET_VERIFY_CODE)
    fun getVerifyCode(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.SIGN_UP)
    fun reregist(@Body body: RequestBody): Flowable<MyHttpResponse<RegistBean>>

    @POST(ApiSettings.GET_USER_INFO)
    fun getUserInfo(@Body body: RequestBody): Flowable<MyHttpResponse<UserInfoBean>>

    @POST(ApiSettings.SAVE_USER_INFO)
    fun saveUserInfo(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.GET_COUNTY)
    fun getCounty(@Body body: RequestBody): Flowable<MyHttpResponse<CountyBean>>

    @POST(ApiSettings.GET_PROVINCE)
    fun getProvince(@Body body: RequestBody): Flowable<MyHttpResponse<ProvinceBean>>

    @POST(ApiSettings.GET_CITY)
    fun getCity(@Body body: RequestBody): Flowable<MyHttpResponse<CityBean>>

    @POST(ApiSettings.GET_EXTRO_CONTACTS)
    fun getExtroContacts(@Body body: RequestBody): Flowable<MyHttpResponse<ExtroContactsBean>>

    @POST(ApiSettings.SAVE_EXTRO_CONTACTS)
    fun saveExtroContacts(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.SAVE_CONTACTS)
    fun saveContacts(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.SET_PASSWORD)
    fun setPwd(@Body body: RequestBody): Flowable<MyHttpResponse<SetPwdBean>>

    @POST(ApiSettings.CREDIT_ASSESS)
    fun creditAssess(@Body body: RequestBody): Flowable<MyHttpResponse<CreditAssessBean>>

    @POST(ApiSettings.SAVE_PHONE_INFO)
    fun savePhoneInfo(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.GET_LAST_SMS_TIME)
    fun getLastSmsTime(@Body body: RequestBody): Flowable<MyHttpResponse<LastSmsTimeBean>>

    @POST(ApiSettings.CREDIT_APPLY)
    fun creditApply(@Body body: RequestBody): Flowable<MyHttpResponse<CreidtApplyBean>>

    @POST(ApiSettings.CREDIT_APPLY)
    fun creditApply2(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.RESET_PASSWORD)
    fun resetPwd(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.LOGOUT)
    fun loginOut(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.SUBMIT_FEEDBACK)
    fun upLoadOpion(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.PRODUCT_RECOMMEND)
    fun productRecommend(@Body body: RequestBody): Flowable<MyHttpResponse<ProductRecommentBean>>

    @POST(ApiSettings.PRODUCT_DETAIL)
    fun nowLoan(@Body body: RequestBody): Flowable<MyHttpResponse<ProductInfoBean>>

    @POST(ApiSettings.NOW_APPLY)
    fun nowApply(@Body body: RequestBody): Flowable<MyHttpResponse<NowApplyBean>>

    @POST(ApiSettings.CHECK_UPGRADE)
    fun checkUpgrade(@Body body: RequestBody): Flowable<MyHttpResponse<UpgradeBean>>

    @POST(ApiSettings.GET_BANNER_INFO)
    fun getHomeTopInfo(@Body body: RequestBody): Flowable<MyHttpResponse<HomeTopInfo>>

    @POST(ApiSettings.GET_PRODUCT_LIST)
    fun getProductInfoList(@Body body: RequestBody): Flowable<MyHttpResponse<ProductInfoListBean>>

    @POST(ApiSettings.BURIED_POINT)
    fun builePoint(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

   @POST(ApiSettings.GET_USER_AUTH_STATUS)
    fun getAuthStatus(@Body body: RequestBody): Flowable<MyHttpResponse<AuthStatus>>

    @POST(ApiSettings.GET_USER_LOAN)
    fun getUsersAuthLimit(@Body body: RequestBody): Flowable<MyHttpResponse<UsersAuthLimitBean>>

    @POST(ApiSettings.GET_LOAN_NOTES)
    fun getUsersLoanInfo(@Body body: RequestBody): Flowable<MyHttpResponse<MyLoadDetailBean>>

    @POST(ApiSettings.CHANGE_NOTES_STATUS)
    fun changeLoanStatus(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.GET_NOTES_LIST)
    fun getMineLoan(@Body body: RequestBody): Flowable<MyHttpResponse<MineLoanBean>>

    @POST(ApiSettings.GET_NOTES_DETAIL)
    fun getLoanDetail(@Body body: RequestBody): Flowable<MyHttpResponse<LoanDetailBean>>

    @POST(ApiSettings.SAVENOTESDETAIL)
    fun addOrChangeLoan(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.GET_LOAN_NAME_LIST)
    fun getProductListSimple(@Body body: RequestBody): Flowable<MyHttpResponse<ProductListSimpleBean>>

    @POST(ApiSettings.CHANGE_MEXI_STATUS)
    fun changeMoxieStatus(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.GET_MSG_INFO)
    fun getDetailJpush(@Body body: RequestBody): Flowable<MyHttpResponse<JpushDetailBean>>

    @POST(ApiSettings.READ_MSG)
    fun setMsgRead(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>
    @POST(ApiSettings.GET_MSG_CENTER_LIST)
    fun getMsgCenterList(@Body body: RequestBody): Flowable<MyHttpResponse<MsgCenterListBean>>
    @POST(ApiSettings.MINE)
    fun getIsReadMsg(@Body body: RequestBody): Flowable<MyHttpResponse<IshaveNoReadMsgBean>>
    @POST(ApiSettings.HOME_POPUP)
    fun getHomeButtomDialogData(@Body body: RequestBody): Flowable<MyHttpResponse<HomeButtomDialogBean>>
    @POST(ApiSettings.HELP_CENTER)
    fun helpCenter(@Body body: RequestBody): Flowable<MyHttpResponse<HelpCenterBean>>
    @POST(ApiSettings.HOME_CREDIT_CARD)
    fun getCreditCardData(@Body body: RequestBody): Flowable<MyHttpResponse<HomeCreditCardBean>>
    @POST(ApiSettings.CREDIT_CARD_LIST)
    fun getCreditCardListData(@Body body: RequestBody): Flowable<MyHttpResponse<CreditCardListBean>>
    @POST(ApiSettings.CREDIT_CARD_DETIAL)
    fun getCreditCardDetail(@Body body: RequestBody): Flowable<MyHttpResponse<CreditCardDetailBean>>
   @POST(ApiSettings.PRODUCT_SEARCH)
    fun getSoftData(@Body body: RequestBody): Flowable<MyHttpResponse<UsersAuthLimitBean>>
    @POST(ApiSettings.SCREEN_PAGE)
    fun getInitSoftData(@Body body: RequestBody): Flowable<MyHttpResponse<FindInitBean>>
    @POST(ApiSettings.FIND_PAGE)
    fun getFoundHomeData(@Body body: RequestBody): Flowable<MyHttpResponse<HomeFoundBean>>
    @POST(ApiSettings.GET_DISCUSS_LIST)
    fun getHomeTalkListData(@Body body: RequestBody): Flowable<MyHttpResponse<CardMoneyListBean>>
    @POST(ApiSettings.MY_DISCUSS_LIST)
    fun getMineTalkListData(@Body body: RequestBody): Flowable<MyHttpResponse<CardMoneyListBean>>
    /*@POST(ApiSettings.SAVE_DISCUSS)
    fun sendMyTalk(@Body body: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>*/
    @Multipart
    @POST(ApiSettings.SAVE_DISCUSS)
    fun sendMyTalk(@Part("json") description: RequestBody, @Part files: ArrayList<MultipartBody.Part>): Flowable<MyHttpResponse<Any>>

    @POST(ApiSettings.DISCUSS_SEARCH)
    fun searchTalk(@Body body: RequestBody): Flowable<MyHttpResponse<CardMoneyListBean>>
    @POST(ApiSettings.DISCUSS_DETAIL)
    fun getDetaliTalkData(@Body body: RequestBody): Flowable<MyHttpResponse<TalkDetailBean>>
    @POST(ApiSettings.PUBLISH_COMMENT)
    fun dissTalk(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>
    @POST(ApiSettings.REPORTDISCUSS)
    fun reportTalk(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>
    @POST(ApiSettings.REPORT_PAGE)
    fun getReportInfo(@Body body: RequestBody): Flowable<MyHttpResponse<ReportBean>>
    @POST(ApiSettings.NEWS)
    fun getMsgList(@Body body: RequestBody): Flowable<MyHttpResponse<MsgListBean>>
    @POST(ApiSettings.START_ADVERTISE)
    fun startAdvertise(@Body body: RequestBody): Flowable<MyHttpResponse<AdvertisingBean>>
    @POST(ApiSettings.START_APP)
    fun startApp(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>
    @POST(ApiSettings.STOP_APP)
    fun stopApp(@Body body: RequestBody): Flowable<MyHttpResponse<Any>>
    @POST(ApiSettings.HOME_DIALOG)
    fun getHomeDialogForUser(@Body body: RequestBody): Flowable<MyHttpResponse<HomeDialogForUser>>
    @POST(ApiSettings.FIND_TAGE_SEARCH)
    fun getTagProductList(@Body body: RequestBody): Flowable<MyHttpResponse<ProductInfoListBean>>





}