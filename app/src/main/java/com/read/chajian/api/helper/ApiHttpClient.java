package com.read.chajian.api.helper;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.read.chajian.CommParam;
import com.read.chajian.api.callback.BaseCallback;
import com.read.chajian.common.CommConfig;
import com.read.chajian.manager.DomainMgr;
import com.read.chajian.util.ZMCommUtil;


import java.util.Map;

/**
 * Created by bin on 17/6/4.
 */

public class ApiHttpClient {


    public static void post(String partUrl, Map<String, String> params, BaseCallback callback,
                            Object tag) {

        PostRequest postRequest = OkGo.post(getAbsoulteApiUrl(partUrl));
        postRequest.tag(tag);
        Map<String, String> postParams = getPostData(params);
        for (Map.Entry<String, String> entry : postParams.entrySet()) {
            postRequest.params(entry.getKey(), entry.getValue());
        }
        postRequest.execute(callback);
    }

    public static void post(String partUrl, BaseCallback callback,
                            Object tag) {
        post(partUrl, null, callback, tag);
    }


    private static Map<String, String> getPostData(Map<String, String> params) {
        Map<String, String> postParams = CommParam.getCommonParams();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                postParams.put(entry.getKey(), entry.getValue());
            }
        }

//        if (UserMgr.getInstance().isLogin()) {
//            postParams.put("_session", UserMgr.getInstance().getSessionId());
//            postParams.put("_uid", UserMgr.getInstance().getUid() + "");
//            postParams.put("_username", UserMgr.getInstance().getUsername());
//        }


        StringBuilder sb = new StringBuilder();
        Map<String, String> resultMap = ZMCommUtil.sortMapByKey(postParams, true);
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            if (!TextUtils.isEmpty(entry.getValue())) {
                sb.append(entry.getKey() + "=" + EncodeUtils.urlEncode(entry.getValue()) + "&");
            }
        }
        String paramStr = sb.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);


//        paramStr = "_device_os=iphone&_language=zh&_oem=default&_os_version=11.4&_prj_name=ferrari&_salt=2D9030CC57668B506581D2FF33795F5B&_session=9cb0e0c120528182073b88dd9ae7ef09&_sn=e75564c8acf2f7a4ab37d2d0f4443f01&_timestamp=1535959803&_uid=29&_username=e9cfcf618f1e5c79&_version=1&page=1&pre_page=20";


        String signStr = EncryptUtils.encryptMD5ToString(paramStr + CommConfig.URL_DECODE_KEY).toLowerCase();
//        LogUtils.e("signStr: " + signStr);
//        postParams.put("_sign", signStr);
//        String parmData = new Gson().toJson(postParams);
//        LogUtils.e("param_data: " + parmData);
//        String postData = ZMCommUtil.mdEncrypt(parmData, CommConfig.URL_DECODE_KEY);
//        LogUtils.e("postData: " + postData);
        postParams.put("_sign", signStr);
        return postParams;
    }


    /**
     * 根据相对URL获取绝对url
     *
     * @param partUrl
     * @return
     */
    private static String getAbsoulteApiUrl(String partUrl) {
        String allUrl = partUrl.startsWith("http") ? partUrl : DomainMgr.getInstance().getBaseUrl() + partUrl;
//        LogUtils.e(allUrl);
        return allUrl;
    }


}
