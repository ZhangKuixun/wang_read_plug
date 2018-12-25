package com.read.chajian.common;

public class CommConfig {

    // 默认连接超时时间
    public static final int DEFAULT_CONNECT_TIMEOUT = 7000;
    // 默认读取超时时间
    public static final int DEFAULT_READ_TIMEOUT = 15000;
    // 默认写入超时时间
    public static final int DEFAULT_WRITE_TIMEOUT = 15000;


//    // 域名图片（主）
//    public static final String DOMAIN_MAIN = "http://img.dns.bike/bg.gif";
//    // 备用
//    public static final String DOMAIN_BACK = "http://img.mddns.net/bg.gif";
    // 域名图片解密key
    public static final String URL_DECODE_KEY = "L@xc[7Y{<lu+1^K*";

     // 域名图片（主）
    public static final String DOMAIN_MAIN = "http://hk-com.oss-cn-hongkong.aliyuncs.com/nodel/haigui/bg.gif ";
    // 备用
    public static final String DOMAIN_BACK = "http://nodel.oss-cn-shanghai.aliyuncs.com/haigui/bg.gif";


//    // 域名图片解密key
//    public static final String URL_DECODE_KEY = "CpRAuu~O!o+a@28k";


    public static final String RESPONSE_DATA_ANNOTATION = "ret";

    // 账户登录
    public static final int LOGIN_TYPE_ACCOUNT = 1;
    // 密码登录
    public static final int LOGIN_TYPE_DEVICE = 2;



    public static final int SPEED_TYPE_GLOBAL = 0;
    public static final int SPEED_TYPE_SMART = 1;


    public static final int LIST_PAGE_SIZE = 20;
}
