package com.read.chajian.util;

import android.util.Base64;

import com.blankj.utilcode.util.EncryptUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @Description:
 * @Author: librabin
 * @Time: 2018/8/31 17:45
 */
public class ZMCommUtil {


    private final static String KEY = "linglong.com";

    public static String mdEncrypt1(String str) {
        String str1 = "1111111111111";
        String str2 = "aaaaaaaaaaaaa";
        String str3 = KEY;
        String str4 = EncryptUtils.encryptMD5ToString(str1 + str2 + str3).toLowerCase();
        //待加密内容base64转码为String
        String str5 = new String(Base64.encode(str.getBytes(), Base64.NO_WRAP));

        // 5用4异或
        String str6_1 = doEor(str5, str4);

        String str6_2 = doEor(str1, str2);

        String str6_3 = doEor(str2, str3);

        String finalStr = new String(Base64.encode((str6_1 + str6_2 + str6_3).getBytes(), Base64.NO_WRAP));

        return finalStr;
    }


    /**
     * 加密
     *
     * @param str
     * @param encodeKey
     * @return
     */
    public static String mdEncrypt(String str, String encodeKey) {
        String str1 = System.currentTimeMillis() + 120000 + "";
        String str2 = getRandomString(13);
        String str3 = encodeKey;
        String str4 = EncryptUtils.encryptMD5ToString(str1 + str2 + str3).toLowerCase();
        //待加密内容base64转码为String
        String str5 = new String(Base64.encode(str.getBytes(), Base64.NO_WRAP));

        // 5用4异或
        String str6_1 = doEor(str5, str4);

        String str6_2 = doEor(str1, str2);

        String str6_3 = doEor(str2, str3);

        String finalStr = new String(Base64.encode((str6_1 + str6_2 + str6_3).getBytes(), Base64.NO_WRAP));

        return finalStr;


    }


    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static String mdDecryptIcon(String str, String decodeKey) {
        String finalStr = new String(Base64.decode(str.getBytes(), Base64.NO_WRAP));
        String str3_1 = doEor(finalStr.substring(finalStr.length() - 13), decodeKey);
        String str3_2 = doEor(finalStr.substring(finalStr.length() - 26, finalStr.length() - 13), str3_1);
        String str3_3 = EncryptUtils.encryptMD5ToString(str3_2 + str3_1 + decodeKey).toLowerCase();
        String testStr = doEor(finalStr.substring(0, finalStr.length() - 26), str3_3);
        String testStr2 = new String(Base64.decode(testStr.getBytes(), Base64.NO_WRAP));

        return testStr2;
    }


    private static String doEor(String str1, String str2) {
        char[] array1 = str1.toCharArray();
        char[] array2 = str2.toCharArray();
        char[] array3 = new char[array1.length];

        for (int i = 0; i < array1.length; i++) {
            array3[i] = (char) (array1[i] ^ array2[i % str2.length()]);
        }

        return new String(array3);

    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }


    public static Map<String, String> sortMapByKey(Map<String, String> oriMap, final boolean isRise) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (isRise) {
                    // 升序排序
                    return o1.compareTo(o2);
                } else {
                    // 降序排序
                    return o2.compareTo(o1);
                }
            }
        });
        sortMap.putAll(oriMap);
        return sortMap;
    }

}
