package com.dudubaika.model.bean;

/**
 * Created by Administrator on 2017/6/27.
 */

/**
 * 短信 com.util.administrator.getuserdeviceinfo.bean
 */
public class SmsInfoBean {
    public String name;
    public int type;
    public String date;
    public String number;
    public String smsBody;

    public SmsInfoBean(String name, int type, String number, String smsBody, String date) {
        this.name = name;
        this.type = type;
        this.number = number;
        this.smsBody = smsBody;
        this.date = date;
    }

}