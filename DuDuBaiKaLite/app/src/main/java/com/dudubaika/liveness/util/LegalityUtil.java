package com.dudubaika.liveness.util;


import com.dudubaika.model.bean.IDCardBean;

/**
 * LegalityUtil
 *
 * @author liuwei
 * @date 2018/6/13
 */
public class LegalityUtil {
    private static double MIN_VALUE = 0.8;

    /**
     * @return 如果是复印件返回true
     */
    public static boolean isIDPhoto(IDCardBean.Legality legality) {
        if (legality.getID_Photo() >= MIN_VALUE) {
            return true;
        }
        return false;
    }
}
