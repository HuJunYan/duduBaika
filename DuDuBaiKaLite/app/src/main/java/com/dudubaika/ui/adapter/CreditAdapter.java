package com.dudubaika.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.dudubaika.R;
import com.dudubaika.base.App;
import com.dudubaika.base.GlobalParams;
import com.dudubaika.event.PostAssessmentEvent;
import com.dudubaika.event.RefreshCreditStatusEvent;
import com.dudubaika.model.bean.CreditAssessBean;
import com.dudubaika.ui.activity.WebVerifyActivity;
import com.dudubaika.util.ImageUtil;
import com.dudubaika.util.ToastUtil;
import com.dudubaika.util.UserUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2018/1/29.
 */

public class CreditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface ItemType {
        int TYPE_TITLE = 2;
        int TYPE_REQUIRED = 3;
        int TYPE_NOT_REQUIRED = 4;
        int TYPE_COMMIT = 5;
    }

    private boolean isAuthIdentity = false;
    private boolean isCommit = false;
    private boolean isHide = true;
    private List<CreditAssessBean.CreditAssessItemBean> mHideData;
    private Activity mActivity;
    private List<CreditAssessBean.CreditAssessItemBean> mData;

    public CreditAdapter(Activity activity, List<CreditAssessBean.CreditAssessItemBean> data) {
        this.mActivity = activity;
        this.mData = data;
    }

    public void refreshData(boolean isCommit, ArrayList<CreditAssessBean.CreditAssessItemBean> hideData, boolean isAuthIdentity) {
        this.isAuthIdentity = isAuthIdentity;
        this.isCommit = isCommit;
        mHideData = hideData;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.TYPE_TITLE) {
            return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title_holder, parent, false));
        } else if (viewType == ItemType.TYPE_REQUIRED) {
            return new RequiredViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_required_holder, parent, false));
        } else if (viewType == ItemType.TYPE_NOT_REQUIRED) {
            return new NotRequiredViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_not_required_holder, parent, false));
        } else if (viewType == ItemType.TYPE_COMMIT) {
            return new CommitViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_commit_holder, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int local_item_type = mData.get(position).getLocal_item_type();
        if (local_item_type == ItemType.TYPE_TITLE) {
            ((TitleViewHolder) holder).onBindViewHolder(position);
        } else if (local_item_type == ItemType.TYPE_REQUIRED) {
            ((RequiredViewHolder) holder).onBindViewHolder(position);
        } else if (local_item_type == ItemType.TYPE_NOT_REQUIRED) {
            ((NotRequiredViewHolder) holder).onBindViewHolder(position);
        } else if (local_item_type == ItemType.TYPE_COMMIT) {
            ((CommitViewHolder) holder).onBindViewHolder(position);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null) {
            return super.getItemViewType(position);
        } else {
            int local_item_type = mData.get(position).getLocal_item_type();
            if (local_item_type == ItemType.TYPE_NOT_REQUIRED) {
                return ItemType.TYPE_NOT_REQUIRED;
            } else if (local_item_type == ItemType.TYPE_REQUIRED) {
                return ItemType.TYPE_REQUIRED;
            } else if (local_item_type == ItemType.TYPE_TITLE) {
                return ItemType.TYPE_TITLE;
            } else if (local_item_type == ItemType.TYPE_COMMIT) {
                return ItemType.TYPE_COMMIT;
            } else {
                return super.getItemViewType(position);
            }
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_holder_title;
        TextView tv_title_holder_desc;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tv_title_holder_title = (TextView) itemView.findViewById(R.id.tv_title_holder_title);
            tv_title_holder_desc = (TextView) itemView.findViewById(R.id.tv_title_holder_desc);
        }

        void onBindViewHolder(int position) {
            CreditAssessBean.CreditAssessItemBean creditAssessItemBean = mData.get(position);
            tv_title_holder_title.setText(creditAssessItemBean.getLocal_item_title());
            tv_title_holder_desc.setText(creditAssessItemBean.getLocal_item_des());
        }
    }

    class RequiredViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_required_holder_title;
        TextView tv_required_title;
        View view_line;
        View view_line2;
        TextView tv_required_status;

        public RequiredViewHolder(View itemView) {
            super(itemView);
            iv_required_holder_title = (ImageView) itemView.findViewById(R.id.iv_required_holder_title);
            tv_required_title = (TextView) itemView.findViewById(R.id.tv_required_title);
            tv_required_status = (TextView) itemView.findViewById(R.id.tv_required_status);
            view_line = itemView.findViewById(R.id.view_line);
            view_line2 = itemView.findViewById(R.id.view_line2);
        }

        void onBindViewHolder(final int position) {
            final CreditAssessBean.CreditAssessItemBean creditAssessItemBean = mData.get(position);
            if (creditAssessItemBean.getLocal_item_is_required()) {
                view_line.setVisibility(View.VISIBLE);
                view_line2.setVisibility(View.GONE);
            } else {
                view_line.setVisibility(View.GONE);
                view_line2.setVisibility(View.VISIBLE);
            }
            String item_name = creditAssessItemBean.getItem_name();
            tv_required_title.setText(item_name);
            if ("1".equals(creditAssessItemBean.getItem_status())) {
                tv_required_status.setText("已认证");
                tv_required_status.setTextColor(mActivity.getResources().getColor(R.color.edit_text_hint_color));
            } else if ("2".equals(creditAssessItemBean.getItem_status())) {
                tv_required_status.setText("去认证");
                tv_required_status.setTextColor(mActivity.getResources().getColor(R.color.global_red_color));
            } else {
                tv_required_status.setText("");
            }
            ImageUtil.INSTANCE.loadNoCache(mActivity, iv_required_holder_title, creditAssessItemBean.getItem_icon(), 0);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkToJump(creditAssessItemBean);
                }
            });
        }
    }

    class NotRequiredViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_not_required_icon;
        TextView tv_not_required_name;
        TextView tv_not_required_status;


        public NotRequiredViewHolder(View itemView) {
            super(itemView);
            iv_not_required_icon = (ImageView) itemView.findViewById(R.id.iv_not_required_icon);
            tv_not_required_name = (TextView) itemView.findViewById(R.id.tv_not_required_name);
            tv_not_required_status = (TextView) itemView.findViewById(R.id.tv_not_required_status);
        }

        public void onBindViewHolder(final int position) {
            final CreditAssessBean.CreditAssessItemBean creditAssessItemBean = mData.get(position);
            ImageUtil.INSTANCE.loadNoCache(mActivity, iv_not_required_icon, creditAssessItemBean.getItem_icon(), 0);

            tv_not_required_name.setText(creditAssessItemBean.getItem_name());
            String item_status = creditAssessItemBean.getItem_status();
            if ("1".equals(item_status)) {
                tv_not_required_status.setText("已认证");
                tv_not_required_status.setTextColor(mActivity.getResources().getColor(R.color.edit_text_hint_color));
            } else {
                tv_not_required_status.setText("未认证");
                tv_not_required_status.setTextColor(mActivity.getResources().getColor(R.color.global_bg));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkToJump(creditAssessItemBean);

                }
            });
        }
    }


    class CommitViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_agreement;
        TextView tv_item_credit_assessment_submit;

        public CommitViewHolder(View itemView) {
            super(itemView);
            tv_item_agreement = (TextView) itemView.findViewById(R.id.tv_item_agreement);
            tv_item_credit_assessment_submit = (TextView) itemView.findViewById(R.id.tv_item_credit_assessment_submit);
        }

        public void onBindViewHolder(final int position) {
            tv_item_agreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, WebVerifyActivity.class);
                    intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_KEY(), UserUtil.INSTANCE.getAuthorUrl(mActivity));
                    intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_TITLE(), "用户服务协议");
                    gotoActivity(intent, mActivity);
                }
            });
            tv_item_credit_assessment_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new PostAssessmentEvent());
                }
            });
        }

    }

    //判断是否满足跳转条件
    public void checkToJump(CreditAssessBean.CreditAssessItemBean creditAssessItemBean) {
        //判断是否满足条件去认证
        if (!isAuthIdentity && !"1".equals(creditAssessItemBean.getItem_num()) && !TextUtils.isEmpty(creditAssessItemBean.getItem_num())) {
            ToastUtil.showToast(mActivity, "请先进行实名认证");
            return;
        }
        String item_is_click = creditAssessItemBean.getItem_is_click();
        if ("2".equals(item_is_click)) {
            ToastUtil.showToast(mActivity, "已认证，无需认证");
        } else {
            String item_num = creditAssessItemBean.getItem_num();
            switch (item_num) {
                case "1"://1身份认证
                    break;
//                case "2"://2银行卡
//                    gotoActivity(mActivity, BindBankCardActivity.class, null);
//                    break;
                case "3"://3运营商
                    Intent intent = new Intent(mActivity, WebVerifyActivity.class);
                    intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_KEY(), creditAssessItemBean.getJump_url());
                    intent.putExtra(WebVerifyActivity.Companion.getWEB_URL_TITLE(), "手机运营商");
                    gotoActivity(intent, mActivity);
                    break;
                case "4"://4联系人
                    break;
                case "5":// 5个人信息
                    break;
                case "6"://6芝麻信用
                    Intent intent2 = new Intent(mActivity, WebVerifyActivity.class);
                    intent2.putExtra(WebVerifyActivity.Companion.getWEB_URL_KEY(), creditAssessItemBean.getJump_url());
                    intent2.putExtra(WebVerifyActivity.Companion.getWEB_URL_TITLE(), "芝麻信用");
                    gotoActivity(intent2, mActivity);
                    break;
                case "7"://7淘宝
                    gotoMoXieActivity(creditAssessItemBean, true);
                    break;
                case "8"://8京东
                case "9"://9信用卡
                case "10"://10社保
                case "11"://11公积金
                case "12"://12学信网
                case "13"://13央行征信
                    gotoMoXieActivity(creditAssessItemBean, false);
                    break;

                case "14"://14微博
                    break;
                default:
            }
        }
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
     * 跳转到某个Activity
     */
    protected void gotoActivity(Intent intent, Activity activity) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);
    }

    private void gotoMoXieActivity(final CreditAssessBean.CreditAssessItemBean creditAssessItemBean, final boolean isTaobao) {

        String item_num = creditAssessItemBean.getItem_num();
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

        //,7淘宝，8京东，9信用卡 ，10社保 ,11公积金 ,12学信网
        switch (item_num) {
            case "7":
                mxParam.setTaskType(MxParam.PARAM_TASK_TAOBAO);
                break;
            case "8":
                mxParam.setTaskType(MxParam.PARAM_TASK_JINGDONG);
                break;
            case "9":
                mxParam.setTaskType(MxParam.PARAM_TASK_EMAIL);
                break;
            case "10":
                mxParam.setTaskType(MxParam.PARAM_TASK_SECURITY);
                break;
            case "11":
                mxParam.setTaskType(MxParam.PARAM_TASK_FUND);
                break;
            case "12":
                mxParam.setTaskType(MxParam.PARAM_TASK_CHSI);
                break;
            case "13":
                mxParam.setTaskType(MxParam.PARAM_TASK_ZHENGXIN);
                break;
            default:
        }
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
                            notifyDataSetChanged();
                            if (isTaobao) {
                                //如果是淘宝  发送消息
                                EventBus.getDefault().post(new RefreshCreditStatusEvent());
                            } else {
                                //不是淘宝则 不需要自动跳转 发送另一个event 刷新接口 暂时不需要做任何事
                                // do nothing
                                //                            EventBus.getDefault().post(new RefreshCreditStatusEvent());
                            }
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
