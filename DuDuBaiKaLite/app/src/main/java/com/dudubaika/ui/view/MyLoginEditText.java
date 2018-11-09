package com.dudubaika.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudubaika.R;
import com.dudubaika.util.TimeCount;


/**
 * Created by Administrator on 2016/7/26.
 * 登录界面的自定义editText
 */
public class MyLoginEditText extends LinearLayout implements View.OnFocusChangeListener, View.OnClickListener {
    ImageView img_left, img_right;
    EditText edit_content;
    View view_line, rootView;
    TextView tv_verification;
    ChangeInterface changeInterface;
    Context mContext;
    private TimeCount mTimer;
    boolean hide = false;//是否显示密码
    private final int INPUT_TEXT = 1;
    private final int INPUT_NUMBER = 2;
    private final int INPUT_PASSWORD = 3;
    private final int INPUT_PHONE = 4;

    Drawable draw_right;
    Drawable draw_right2;
    //输入框搜索类型
    private  int viewInputType=  0;

    public MyLoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_mylogin_edit_text, this);
        findViews(rootView);
        init(attrs);
    }

    public int getViewInputType() {
        return viewInputType;
    }

    public void setViewInputType(int viewInputType) {
        this.viewInputType = viewInputType;
       if (null !=edit_content) {
           if (INPUT_PASSWORD == viewInputType) {
               edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
               edit_content.setTransformationMethod(PasswordTransformationMethod.getInstance());
           } else {
               edit_content.setInputType(InputType.TYPE_CLASS_NUMBER);
               edit_content.setTransformationMethod(null);
           }
       }


    }

    private MyEditTextListener mListener;

    public void setChangeListener(ChangeInterface changeInterface) {
        this.changeInterface = changeInterface;
        edit_content.addTextChangedListener(new EditChangedListener());
    }

    public void setListener(MyEditTextListener listener) {
        mListener = listener;
    }

    private void findViews(View rootView) {
        edit_content = (EditText) rootView.findViewById(R.id.edit_content);
        edit_content.setOnFocusChangeListener(this);
        img_left = (ImageView) rootView.findViewById(R.id.img_left);
        img_right = (ImageView) rootView.findViewById(R.id.img_right);
        tv_verification = (TextView) rootView.findViewById(R.id.tv_verification);
        view_line = rootView.findViewById(R.id.view_line);
        img_right.setOnClickListener(this);
        tv_verification.setOnClickListener(this);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.MyLoginEditText);
        String hint = array.getString(R.styleable.MyLoginEditText_login_edit_hint);

        Drawable draw_left = array.getDrawable(R.styleable.MyLoginEditText_login_edit_img_left);
        draw_right = array.getDrawable(R.styleable.MyLoginEditText_login_edit_img_right);
        draw_right2 = array.getDrawable(R.styleable.MyLoginEditText_login_edit_img_right2);
        int inputType = array.getInt(R.styleable.MyLoginEditText_login_edit_inputType, 0);
        int textSize = array.getInteger(R.styleable.MyLoginEditText_login_edit_text_size, 15);
        String tv_right = array.getString(R.styleable.MyLoginEditText_login_edit_tv_right);
        String editContent = array.getString(R.styleable.MyLoginEditText_login_edit_content);
        int imgSize = array.getInteger(R.styleable.MyLoginEditText_login_edit_img_size, 20);
        int maxLength = array.getInt(R.styleable.MyLoginEditText_login_edit_maxLength, 0);
        array.recycle();
        if (maxLength != 0) {
            InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
            edit_content.setFilters(filters);
        }
        if (hint != null) {
            edit_content.setHint(hint);
        }
        if (draw_left != null) {

            img_left.setVisibility(View.VISIBLE);
            img_left.setImageDrawable(draw_left);
        }
        if (draw_right != null) {
            img_right.setVisibility(View.VISIBLE);
            img_right.setImageDrawable(draw_right);
        }
        if (textSize != 15) {
            edit_content.setTextSize(textSize);
        }

        if ("".equals(tv_right) || null == tv_right) {
            tv_verification.setVisibility(View.GONE);
        } else {
            tv_verification.setVisibility(View.VISIBLE);
            tv_verification.setText(tv_right);
        }

        if (INPUT_TEXT == inputType) {
            edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (INPUT_NUMBER == inputType) {
            edit_content.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (INPUT_PHONE == inputType) {
            edit_content.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (INPUT_PASSWORD == inputType) {
            edit_content.setInputType(InputType.TYPE_CLASS_TEXT);
            edit_content.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edit_content.setHintTextColor(getResources().getColor(R.color.edit_text_hint_color));
//        edit_content.setFocusable(isEdit);
    }

    public void setText(String content) {
        edit_content.setText(content);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("ret", "hasFocus" + hasFocus);
        if (hasFocus) {
            view_line.setBackgroundResource(R.color.edit_text_baseline_color);
        } else {
            view_line.setBackgroundResource(R.color.edit_text_baseline_color);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_right:
                if (hide) {
                    img_right.setImageDrawable(draw_right2);
                    hide = false;
                    edit_content.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    img_right.setImageDrawable(draw_right);
                    hide = true;
                    edit_content.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_verification:
                if (mListener != null) {
                    if (v.getId() == R.id.tv_verification && mListener.onRightClick(this)) {
                        mTimer = new TimeCount(tv_verification, 60000, 1000, "重新获取");
                        mTimer.start();
                    }
                }
                break;
        }
    }

    public void finishTimer() {
        if (mTimer !=null) {
            mTimer.finish();
        }
    }

    public void startTimer() {
        mTimer = new TimeCount(tv_verification, 60000, 1000, "重新获取");
        mTimer.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface MyEditTextListener {
        boolean onRightClick(View view);
    }

    public void setRightImageViewIcon(int resId) {
        img_right.setImageResource(resId);
    }

    public String getText() {
        return edit_content.getText().toString().trim();
    }

    public class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            changeInterface.changeBefore(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changeInterface.change(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            changeInterface.chageAfter(s);
        }
    }

    public void isShowRightView(Boolean flag){
        if (flag){
            tv_verification.setVisibility(View.VISIBLE);
        }else {
            tv_verification.setVisibility(View.INVISIBLE);
        }
    }

}

