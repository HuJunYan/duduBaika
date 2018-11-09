package com.dudubaika.model.http

import android.text.TextUtils
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.VerifyHomeDataBean
import com.dudubaika.model.bean.VerifyProductDetailBean
import com.dudubaika.util.GsonUtil
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import java.nio.charset.Charset


/**
 * 伪造测试数据拦截器
 */
class ReviewFakeApiInterceptor : Interceptor {
    private val LOG_TAG = "http"
    //返回的http状态码
    private val HTTP_CODE = 200

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response
        val url = chain.request().url().toString()

        //打印log
        //打印request url
        LogUtil.i(LOG_TAG, url)
        //打印request json
        var charset: Charset? = Charset.forName("UTF-8")
        val requestBody = chain.request().body()
        val contentType = requestBody!!.contentType()
        charset = contentType!!.charset(charset)
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        LogUtil.i(LOG_TAG, "--> start")

        val requestJson = buffer.readString(charset!!)
        LogUtil.json(LOG_TAG, requestJson)
        LogUtil.i(LOG_TAG, "--> end")

        val responseJson = checkURL2FakeJson(url, requestJson)
        if (TextUtils.isEmpty(responseJson)) { //测试数据没有写的情况下走真实接口
            return chain.proceed(chain.request())
        }

        //打印http code
        LogUtil.i(LOG_TAG, "<--http code:$HTTP_CODE")
        LogUtil.i(LOG_TAG, "<-- start")
        //打印response log
        LogUtil.json(LOG_TAG, responseJson)
        LogUtil.i(LOG_TAG, "<-- end")

        response = Response.Builder()
                .code(HTTP_CODE)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), responseJson))
                .message(responseJson)
                .request(chain.request())
                .protocol(Protocol.HTTP_2)
                .build()
        return response
    }

    /**
     * 根据URL生成测试数据
     */
    private fun checkURL2FakeJson(url: String, requestJson: String): String {
        var responseJson = ""
        when (url) {
            ReviewApiManager.HOST + ReviewApiSettings.GET_SEARCH_VIEW -> {
                val mData = ArrayList<VerifyHomeDataBean.VerifyBannerBean>()
                val mData2 = ArrayList<VerifyHomeDataBean.ProductBean>()
                val data = VerifyHomeDataBean(mData, mData2)
                mData.add(VerifyHomeDataBean.VerifyBannerBean("http://cdn.tianshenjr.com/icon_beijing.png",
                        "北京交通卡", "http://www.baidu.com"))
                mData.add(VerifyHomeDataBean.VerifyBannerBean("http://cdn.tianshenjr.com/icon_chongqing.png",
                        "重庆交通卡", "http://www.baidu.com"))
                mData.add(VerifyHomeDataBean.VerifyBannerBean("http://cdn.tianshenjr.com/icon_tianjin.png",
                        "天津交通卡", "http://www.baidu.com"))
                mData.add(VerifyHomeDataBean.VerifyBannerBean("http://cdn.tianshenjr.com/icon_shanghai.png",
                        "上海交通卡", "http://www.baidu.com"))

                mData2.add(VerifyHomeDataBean.ProductBean("1", "http://cdn.tianshenjr.com/icon_beijing.png",
                        "北京交通卡", "普通公交卡，学生公交卡，短期公交卡", "5元", "1000", 102))
                mData2.add(VerifyHomeDataBean.ProductBean("2", "http://cdn.tianshenjr.com/icon_chongqing.png",
                        "重庆交通卡", "普通公交卡，优惠(月票)卡，免费卡", "10元", "185", 102))
                mData2.add(VerifyHomeDataBean.ProductBean("3", "http://cdn.tianshenjr.com/icon_tianjin.png",
                        "天津交通卡", "H卡，月票卡，学生月票卡", "30元", "360", 102))
                mData2.add(VerifyHomeDataBean.ProductBean("4", "http://cdn.tianshenjr.com/icon_shanghai.png",
                        "上海交通卡", "紫色普通交通卡，绿色、黄色和红色普通交通卡", "880元", "100", 102))

                val myHttpResponse = MyHttpResponse<VerifyHomeDataBean>(0, "success", data)
                responseJson = GsonUtil.bean2json(myHttpResponse)
            }
            ReviewApiManager.HOST + ReviewApiSettings.GET_REVIEW_PRODUCT_DETAIL -> {

                val list = ArrayList<VerifyProductDetailBean.ProductDetailBean>()

                val jsonObject = JSONObject(requestJson)
                val productId = jsonObject.optJSONObject("data").optString("product_id")
                when (productId) {
                //北京
                    "1" -> {
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/bj_general.png", "普通公交卡", "2006年05月至2014年12月28日，普通公交卡的折扣为4折。2014年12月28日起，普通公交卡的折扣为5折。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/bj_short_period.png", "学生公交卡", "根据《北京市教育委员会关于印发《北京市中小学学生卡管理暂行办法》的通知》（京教基[2007]17号），普通中小学在校学生，凭北京市中小学学生卡（含证件卡、学籍卡）到公交集团发卡充值网点充值，即可做为学生公交IC卡使用。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/bj_student.png", "短期公家卡", "从2007年02月01日起，一卡通开始发行3日、7日、15日三种限次使用的短期卡，以方便短期到北京旅行的乘客。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/j_j_j.png", "京津冀交通卡", "2015年12月起，北京开始发行京津冀一卡通互通卡，该卡可用于北京、天津、张家口、廊坊、保定和石家庄的部分公交线路。"))
                    }
                //重庆
                    "2" -> {
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/cq_general.png", "普通卡", "持该卡可多人同时刷卡乘车，可跨年、月使用，可乘坐所有已安装车载和手持收费机的公交车，每次充值金额最低10元，每次乘车按实际票价9折扣费。购卡人初次申购，凭身份证登记，交纳25元服务补偿金即可办理(便于挂失和退卡)，办卡后，一个月抵扣1元，扣完为止，此卡不贴照片。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_blue.png", "优惠卡（月卡）", "此卡属于福利卡，分成人与学生两种，优惠幅度较大，但须按月使用。持卡人每月最多限乘90次，过月，卡内剩余次数作废。办卡人需持身份证或户口本原件(学生凭学生证)，交彩色照片1张及IC卡押金25元，仅限本人使用，不使用时，如卡完好可退回押金。成人卡每月购价40元，学生卡每月购价20元。按每月乘车90次计算，成人每次乘车折合人民币0.44元，学生每次乘车折合人民币0.22元。根据重庆市规划局2002年10月组织的交通综合大调查的结果，月票乘客每天平均出行2.57次，除国家规定的法定节假日外，职工上下班出行的月工作日为21天，照此计算，人均上下班乘车次数平均约为54次。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_blue.png", "免费卡", "仅限主城区常住城镇户口的离休干部、革命残废军人、盲人和70岁以上的老年人。"))

                    }
                //天津
                    "3" -> {
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/tianjin.png", "A卡(H卡)", "A卡(H卡)最少充值10元，95折优惠。1.5元的线路-1.43元，1元的线路-0.95元，可以连续打卡，卡内资费没有月份的限制，可以乘坐任何有刷卡机的线路。任何工作时间都可以。"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_blue.png", "B卡(月票卡)", "B卡(月票卡)月充值有效，充值费40/月，可以乘坐90次，约0.45元/次，只限一个月内用完，乘坐路线限制为：1-98，631，662，639，645，609，634，695，693路。办理的时候需要本人身份证、单位工作证明。办理时间：每月25日-次月5日。"))
                    }
                //上海
                    "4" -> {
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_blue.png", "上海蓝色公交卡", "蓝色普通交通卡以及11位卡号以字母“U”开头（CPU卡）的纪念卡、异型卡 可在以下城市使用——太仓（公交）、金华（公交）、宜兴（公交）、宁波（公交）、绍兴（公交）、湖州（公交）、台州（公交）、常熟（公交）、昆山（公交）、江阴（公交）、淮安（公交）、启东无锡（公交、地铁）、南通（公交）、泰州（公交）、长兴（公交）、舟山（公交）、嘉兴（公交）、义乌（公交）、温州（公交、轮渡）"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_purse.png", "上海紫色公交卡", "紫色普通交通卡以及12位卡号以C开头的纪念卡、异型卡"))
                        list.add(VerifyProductDetailBean.ProductDetailBean("http://cdn.tianshenjr.com/sh_red.png", "上海红色公交卡", "绿色、黄色和红色普通交通卡以及11位卡号直接以数字开头（M1卡）的纪念卡、异型可在以下城市使用——无锡、昆山、常熟、阜阳、苏州（公交）、杭州（部分出租汽车）、南宁（海博出租汽车）、大丰（出租汽车）、舟山。"))
                    }
                }


                val verifyProductDetailBean = VerifyProductDetailBean(list)

                val myHttpResponse = MyHttpResponse<VerifyProductDetailBean>(0, "success", verifyProductDetailBean)
                responseJson = GsonUtil.bean2json(myHttpResponse)
            }
        }

        return responseJson
    }

}