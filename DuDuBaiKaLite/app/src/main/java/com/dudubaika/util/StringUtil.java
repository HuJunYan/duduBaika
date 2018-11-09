package com.dudubaika.util;


public class StringUtil {


    public static String getTianShenCardNum(String cardNum) {
        int content = 4;
        StringBuilder sb = new StringBuilder();
        char[] cs = cardNum.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            System.out.println(cs[i]);
            if (i != 0 && i % content == 0) {
                sb.append(" ");
            }
            sb.append(cs[i]);
        }
        return sb.toString();
    }


    /**
     * 给手机号显示
     */
    public static String encryptPhoneNum(String phoneNum) {
        char[] array = phoneNum.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i == 3 || i == 4 || i == 5 || i == 6) {
                sb.append("*");
            } else {
                sb.append(array[i]);
            }
        }
        return sb.toString();
    }

    public static String getEndBankCard(String cardNum) {
        char[] array = cardNum.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = array.length - 4; i < array.length; i++) {
            sb.append(array[i]);
        }
        return sb.toString();
    }


}
