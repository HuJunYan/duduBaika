package com.dudubaika.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dudubaika.R;
import com.dudubaika.base.App;
import com.dudubaika.base.GlobalParams;
import com.dudubaika.event.RefreshCreditStatusEvent;
import com.dudubaika.model.bean.CreditAssessBean;
import com.dudubaika.ui.activity.AuthExtroContactsActivity;
import com.dudubaika.ui.activity.AuthInfoActivity;
import com.dudubaika.ui.activity.IdentityActivity;
import com.dudubaika.ui.activity.MainActivity;
import com.dudubaika.ui.activity.WebVerifyActivity;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;

import org.greenrobot.eventbus.EventBus;

public class AutherUtils {

    //身份认证 - 个人信息 -紧急联系人 - 运营商- 淘宝
    private Class<IdentityActivity>[] intents= new Class[]{IdentityActivity.class, AuthInfoActivity.class, AuthExtroContactsActivity.class, WebVerifyActivity.class};

    private Context context;
    private Activity activity;

    private AutherUtils() {
    }
    public static AutherUtils getInstance() {
        return holder.INSTANCE;
    }

    public static class holder{
        public static final AutherUtils INSTANCE = new AutherUtils();
    }
    private AutherUtils getContext(Activity context){
        this.context =context;
        this.activity =context;
        return this;
    }

    public AutherUtils StartActivity(Activity context,String tag,String url){

        Class c=null;
        Bundle bundle = null;

        if ("4".equals(tag)){
            Intent intent = new Intent(context,intents[3]);
            intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_KEY(),url);
            intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_TITLE(), "手机运营商");
            context.startActivity(intent);

        }else {
            switch (tag) {
                case "1":
                    c = intents[0];
                    break;
                case "2":
                    c = intents[1];
                    break;
                case "3":
                    c = intents[2];
                    break;
                default:
                    c = MainActivity.class;
                    break;

            }
            gotoActivity(context, c, bundle);

        }
        return this;
    }





    /**
     * 跳转到某个Activity
     */
    protected void gotoActivity(Activity mContext, Class toActivityClass, Bundle bundle) {
        Intent intent = new Intent(mContext, toActivityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    /**
     * 到魔蝎认证界面
     * @param activity
     */
    private void gotoMoXieActivity(final Activity mActivity) {

        String apiKey = "";
        switch (App.instance.getMCurrentHost()) {
            //测试key
            case DEV:
                apiKey = GlobalParams.MOXIE_DEV_KEY;
                break;
            //测试key
            case PRE:
                apiKey = GlobalParams.MOXIE_DEV_KEY;
                break;
            //正式key
            case PRO:
                apiKey = GlobalParams.MOXIE_PRO_KEY;
                break;
            default:
                apiKey = GlobalParams.MOXIE_DEV_KEY;
        }

        final MxParam mxParam = new MxParam();
        mxParam.setUserId(UserUtil.INSTANCE.getUserId(mActivity) + "-" + GlobalParams.PLATFORM_FLAG);
        mxParam.setApiKey(apiKey);

        mxParam.setTaskType(MxParam.PARAM_TASK_TAOBAO);
        MoxieSDK.getInstance().start(mActivity, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        case MxParam.ResultCode.IMPORTING:
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                        case MxParam.ResultCode.IMPORT_FAIL:
                            ToastUtil.showToast(mActivity, "认证失败!");
                            moxieContext.finish();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //刷新接口
                            moxieContext.finish();
                            break;
                        default:
                    }

                }
                return false;
            }
        });

    }




}
