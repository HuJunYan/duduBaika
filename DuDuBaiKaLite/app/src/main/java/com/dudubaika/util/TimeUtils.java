package com.dudubaika.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bao on 2016/3/27.
 */
public class TimeUtils {
    /**
     * 时间戳转为时间(年月日，时分秒)
     *
     * @param cc_time 时间戳
     * @return
     */
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     * @return
     */
    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param format  yyyy-MM-dd
     * @param date
     * @return 2018-06-12
     */
    public static String getData2String(String format,Date date){

        SimpleDateFormat sdf = new SimpleDateFormat(format);
         return sdf.format(date);
    }

    /**
     *把date转为long
     * @param date
     * @return 1536125646
     */
    public static long getData2Long(Date date){

       return date.getTime();
    }



    /**
     * 获取当前时间
     * @return 2018年06月06日
     *
     */
    public static  String getCurrentTime(){

        //获取当前时间到毫秒值
        Date d = new Date();
        //创建日期格式化对象(把日期转成字符串)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(d);
    }


    /**
     * 把天数转为Date
     * @param i
     * @return
     */
    public static Date getDays2Date( String i){
        if (!TextUtils.isEmpty(i)) {
            int day = Integer.parseInt(i);
            return new Date(day * 60 * 60 * 24);
        }else {
            return new Date();
        }
    }


    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date string2Date(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }



 /**
 * 指定日期加上天数后的日期
 * @param num 为增加的天数
 * @param newDate 创建时间
 * @return
 * @throws ParseException
 */
    public static String plusDay(int num,String newDate) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date  currdate = format.parse(newDate);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        return enddate;
    }


    /**
     *
     * @param dateTime 待处理的日期
     * @param n 加减天数
     * @return
     */
    public static String addAndSubtractDaysByGetTime(Date dateTime,int n){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(df.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //System.out.println(dd.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //注意这里一定要转换成Long类型，要不n超过25时会出现范围溢出，从而得不到想要的日期值
        return getData2String("yyyy年MM月dd日",new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L));
    }


    /**
     *@param  date1 被减数
     *@param date2 减数
     *@return day 天数
     * eg: date2-date1
     */

    public static long getDaysFromTwoData(Date date1, Date date2){

        long between=(date2.getTime()-date1.getTime())/1000L;//除以1000是为了转换成秒
        long day=between/(24*3600);
        return day;

    }




}



