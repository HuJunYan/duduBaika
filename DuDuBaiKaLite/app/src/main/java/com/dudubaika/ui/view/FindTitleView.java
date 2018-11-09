package com.dudubaika.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dudubaika.R;
import com.dudubaika.util.ToastUtil;

public class FindTitleView extends RelativeLayout {

    private View view;
    private Context mContext;
    private RelativeLayout view_layout,view_main_layout;
    private TextView tv;
    private ImageView iv;
    //设置当前的控件状态（用于点击）
    private String mCurrentStatus="default";
    //取值当前控件的真实状态
    private String mStatus;
    private String text;

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmCurrentStatus() {
        return mCurrentStatus;
    }

    public void setmCurrentStatus(String mCurrentStatus) {
        this.mCurrentStatus = mCurrentStatus;
    }



    public FindTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_find_title, this);
        findViews();
        init(attrs);
    }

    private void findViews() {
        view_layout  =  view.findViewById(R.id.view_layout);
        view_main_layout  =  view.findViewById(R.id.view_main_layout);
        tv  =  view.findViewById(R.id.tv);
        iv  =  view.findViewById(R.id.iv);
        setDefaultStatus();
        view_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                mListener.onMyClick(v);
                switch (getmCurrentStatus()){

                    case "up":
                        setmCurrentStatus("down");
                        setUpStatus();
                        setmStatus("up");
                        break;

                    case "down":
                        setmCurrentStatus("up");
                        setDownStatus();
                        setmStatus("down");
                        break;

                    default:
                        setDefaultStatus();

                        break;
                }
            }
        });
    }


    private void init(AttributeSet attrs) {


        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.MySoftView);
        String left_text = array.getString(R.styleable.MySoftView_my_findview_left_text);

        if (!TextUtils.isEmpty(left_text)){
         tv.setText(left_text);
        }
    }

    //设置文字
    public void setText(String text){
        tv.setText(text);
    }




    //设置降序状态
    public void setDownStatus() {
        tv.setTextColor(Color.parseColor("#CCA967"));
        iv.setImageResource(R.drawable.soft_down);
    }

    //设置升序状态
    public void setUpStatus() {
        tv.setTextColor(Color.parseColor("#CCA967"));
        iv.setImageResource(R.drawable.soft_up);
    }

    //设置为默认状态
    public void setDefaultStatus() {
        tv.setTextColor(Color.parseColor("#262626"));
        iv.setImageResource(R.drawable.soft_default);
//        setmCurrentStatus("default");

    }

    private FindTitleViewListener mListener;
    public void setMyListener(FindTitleViewListener listener) {
        mListener = listener;
    }
    public interface FindTitleViewListener {
        void onMyClick(View view);
    }

}
