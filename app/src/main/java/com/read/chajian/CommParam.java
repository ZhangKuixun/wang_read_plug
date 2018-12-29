package com.read.chajian;

import android.os.Build;


import com.read.chajian.manager.AppMgr;
import com.read.chajian.util.ZMCommUtil;

import java.util.HashMap;
import java.util.Map;


public class CommParam {


    // 组织公共参数
    public static Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>();
        params.put("_prj_name", "Read");
        params.put("_device_os", "android");
        params.put("_sn", AppMgr.getInstance().getDeviceId());
        params.put("_version", BuildConfig.VERSION_CODE + "");
        params.put("_oem", "turtle_pro");
        params.put("_timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("_language", "zh_cn");
        params.put("_salt", ZMCommUtil.getRandomString(11));
        params.put("_os_version", Build.VERSION.RELEASE);
        return params;
    }

    public static Map<String, String> getCommonParamsTemp() {
        Map<String, String> params = new HashMap<>();
        params.put("_prj_name", "Read");
        params.put("_device_os", "android");
        params.put("_sn", AppMgr.getInstance().getDeviceId());
        params.put("_version", BuildConfig.VERSION_CODE + "");
        params.put("_oem", "turtle_pro");
        params.put("_timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("_language", "zh_cn");
        params.put("_os_version", Build.VERSION.RELEASE);
        return params;
    }


}
