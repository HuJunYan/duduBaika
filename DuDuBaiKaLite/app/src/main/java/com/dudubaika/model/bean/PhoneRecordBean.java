package com.dudubaika.model.bean;

/**
 * Created by Administrator on 2017/6/27.
 */


/**
 * 通话记录 com.util.administrator.getuserdeviceinfo.bean
 */
public class PhoneRecordBean {
    public String name; //名字
    public String type;  //类型 未接 呼出 呼入
    public String date; // 通话时间
    public String number; //电话
    public String duration; //通话时长

    public PhoneRecordBean(String name, String type, String time, String duration, String number) {
        this.name = name;
        this.type = type;
        this.date = time;
        this.duration = duration;
        this.number = number;
    }

}