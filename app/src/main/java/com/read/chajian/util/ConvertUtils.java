package com.read.chajian.util;

/**
 * @Description:
 * @Author: librabin
 * @Time: 2018/8/30 08:06
 */
public class ConvertUtils {

    public static String getUseTime(long startTime, long endTime) {
        long second = endTime - startTime;
        if (second < 0) {
            return "";
        }
        long minute = second % 3600;
        long hour = second / 3600;

        return hour + "小时" + minute + "分钟";

    }

}
