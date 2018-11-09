package com.dudubaika.util;

import android.text.TextUtils;

import com.dudubaika.base.App;
import com.dudubaika.model.http.ApiManager;
import org.json.JSONObject;

import okhttp3.RequestBody;

/**
 * 埋点统计
 */
public class MaiDianUtils {
    private static final MaiDianUtils ourInstance = new MaiDianUtils();

    public static MaiDianUtils getInstance() {
        return ourInstance;
    }

    private MaiDianUtils() {
    }

    private void setTag(String tag,String product_id) throws Exception{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("flag",tag);
        String mobile = UserUtil.INSTANCE.getMobile(App.instance);
        if (TextUtils.isEmpty(mobile)){
            mobile="";
        }
        jsonObject.put("mobile",mobile);
        jsonObject.put("product_id",product_id);
        JSONObject jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject);
        RequestBody body = RequsetUtil.INSTANCE.getRequestBody(jsonObjectSigned);


        /*val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("type",type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getVerifyCode(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!) {
            override fun onNext(data: Any) {
                mView?.getVCodeComplete()
            }

        }))*/

    }
}
