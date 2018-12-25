package com.read.chajian.base;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.read.chajian.common.CommConfig;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

public class BaseApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        initVendor();
    }


    public static Activity getTopActivity() {
        return ActivityUtils.getTopActivity();
    }


    /**
     * 初始化一些三方的工具
     */
    private void initVendor() {
        // 工具初始化
        Utils.init(this);

        // http模块初始化
        initHttpModule();

;

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }


    /**
     * 初始化HTTP模块
     */
    private void initHttpModule() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 打印日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("PROXY");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        // 全局读取超时时间
        builder.readTimeout(CommConfig.DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(CommConfig.DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.connectTimeout(CommConfig.DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        // https配置
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE);

    }





    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }


}
