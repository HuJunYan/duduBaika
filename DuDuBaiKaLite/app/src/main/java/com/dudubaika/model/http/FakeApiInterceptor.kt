package com.dudubaika.model.http

import android.text.TextUtils
import com.dudubaika.BuildConfig
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.*
import com.dudubaika.ui.adapter.CreditAdapter
import com.dudubaika.util.GsonUtil
import okhttp3.*
import okio.Buffer
import java.nio.charset.Charset

/**
 * 伪造测试数据拦截器
 */
class FakeApiInterceptor : Interceptor {
    private val LOG_TAG = "http"
    //返回的http状态码
    private val HTTP_CODE = 200

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response
        if (BuildConfig.DEBUG) {
            val url = chain.request().url().toString()
            val responseJson = checkURL2FakeJson(url)
            if (TextUtils.isEmpty(responseJson)) { //测试数据没有写的情况下走真实接口
                return chain.proceed(chain.request())
            }

            //打印log
            //打印request url
            LogUtil.d(LOG_TAG, url)
            //打印request json
            var charset: Charset? = Charset.forName("UTF-8")
            val requestBody = chain.request().body()
            val contentType = requestBody!!.contentType()
            charset = contentType!!.charset(charset)
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            LogUtil.i(LOG_TAG, "--> start")
            LogUtil.json(LOG_TAG, buffer.readString(charset!!))
            LogUtil.i(LOG_TAG, "--> end")

            //打印http code
            LogUtil.i(LOG_TAG, "<--http code:" + HTTP_CODE)
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
        } else {
            response = chain.proceed(chain.request())
        }
        return response
    }

    /**
     * 根据URL生成测试数据
     */
    private fun checkURL2FakeJson(url: String): String {
        var json = ""
        when (url) {
        //首页banner
            ApiManager.mHost + ApiSettings.GET_BANNER_LIST -> {
                val banner_list = ArrayList<BannerListBean.BannerBean>()
                banner_list.add(BannerListBean.BannerBean("http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true",
                        "2", "http://www.baidu.com", "2"))
//                banner_list.add(BannerListBean.BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505470629546&di=194a9a92bfcb7754c5e4d19ff1515355&imgtype=0&src=http%3A%2F%2Fpics.jiancai.com%2Fimgextra%2Fimg01%2F656928666%2Fi1%2FT2_IffXdxaXXXXXXXX_%2521%2521656928666.jpg",
//                        "1", "http://www.sohu.com", "1"))
                val bannerListBean = com.dudubaika.model.bean.BannerListBean(banner_list)
                val myHttpResponse = com.dudubaika.model.bean.MyHttpResponse<BannerListBean>(0, "success", bannerListBean)
                json = GsonUtil.bean2json(myHttpResponse)
            }
        //产品列表
            ApiManager.mHost + ApiSettings.GET_PRODUCT_LIST -> {
                val top_list = ArrayList<ProductListBean.ProductBean>()
                val hot_list = ArrayList<ProductListBean.ProductBean>()
                top_list.add(ProductListBean.ProductBean("1", "http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true",
                        "天神贷top1", "凭信用卡极速放贷", "1", "601-1000", "期限1-12个月", "1234"))
                top_list.add(ProductListBean.ProductBean("1", "http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true",
                        "天神贷top2", "凭信用卡极速放贷", "2", "802-1000", "期限1-12个月", "1234"))
                for (i in 1..3) {
                    hot_list.add(ProductListBean.ProductBean("1", "http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true",
                            "天神贷hot$i", "凭信用卡极速放贷", "$i", "805-1000", "期限1-12个月", "1234"))
                }
                val productListBean = com.dudubaika.model.bean.ProductListBean(top_list, "借款成功率在90%以上的平台", "热门贷款产品", hot_list)
                val myHttpResponse = com.dudubaika.model.bean.MyHttpResponse<ProductListBean>(0, "success", productListBean)
                json = GsonUtil.bean2json(myHttpResponse)
            }
        //获取认证信息
            ApiManager.mHost + ApiSettings.CREDIT_ASSESS -> {
                var mData = ArrayList<CreditAssessBean.CreditAssessItemBean>()
                var mData2 = ArrayList<CreditAssessBean.CreditAssessItemBean>()
                var data = com.dudubaika.model.bean.CreditAssessBean(mData, mData2);
                mData.add(CreditAssessBean.CreditAssessItemBean("1",
                        "http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg", "身份认证", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("2",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg"
                        , "收款银行卡", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("5", "http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg"
                        , "个人信息", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("3",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg"
                        , "运营商认证", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("4",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "紧急联系人", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("6",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "芝麻信用", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData.add(CreditAssessBean.CreditAssessItemBean("7",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "淘宝", "1", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("8",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "京东", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("9",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "信用卡", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("10",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "社保", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("11",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "公积金", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("12",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "学信网", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))
                mData2.add(CreditAssessBean.CreditAssessItemBean("13",
                        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3530122825,3588284255&fm=27&gp=0.jpg", "征信", "2", "1", "http://www.baidu.com", CreditAdapter.ItemType.TYPE_REQUIRED))

                val myHttpResponse = com.dudubaika.model.bean.MyHttpResponse<CreditAssessBean>(0, "success", data)
                json = GsonUtil.bean2json(myHttpResponse)
            }
        //得到用户的状态u
            ApiManager.mHost + ApiSettings.GET_UESR_AUTH_RESULT -> {
                val userAuthResultBean = com.dudubaika.model.bean.UserAuthResultBean("1", "3", "2")
                val myHttpResponse = com.dudubaika.model.bean.MyHttpResponse<UserAuthResultBean>(0, "success", userAuthResultBean)
                json = GsonUtil.bean2json(myHttpResponse)
            }
            ApiManager.mHost + ApiSettings.PRODUCT_RECOMMEND -> {
                val productRecommentBean = com.dudubaika.model.bean.ProductRecommentBean("http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true")
                val myHttpResponse = com.dudubaika.model.bean.MyHttpResponse<ProductRecommentBean>(0, "success", productRecommentBean)
                json = GsonUtil.bean2json(myHttpResponse)
            }
        }

        return json
    }

}