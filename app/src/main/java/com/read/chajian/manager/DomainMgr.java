package com.read.chajian.manager;

import com.blankj.utilcode.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.read.chajian.common.CommConfig;
import com.read.chajian.interfac.CommCallBack;
import com.read.chajian.util.ZMCommUtil;

public class DomainMgr {


    private static final String SP_BASE_URL = "base_url";

    private static final String URL_PRE = "http://";

    private String baseUrl;

    private static DomainMgr instance;


    private DomainMgr() {

    }

    public static DomainMgr getInstance() {
        if (instance == null) {
            instance = new DomainMgr();
        }
        return instance;
    }


    /**
     * 获取url
     *
     * @return
     */
    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = SPUtils.getInstance().getString(SP_BASE_URL, "");
//            baseUrl = "http://ddns.zmapi.com:6501/";
//            baseUrl = "https://api.mddns.net/";
        }
        return baseUrl;
    }


    /**
     * 解析url
     *
     * @param str
     * @return
     */
    private boolean parseUrl(String str) {
        int startIndex = str.indexOf("5B2D3E");
        int endIndex = str.indexOf("3C2D5D");
        if (startIndex == -1 || endIndex == -1) { // 未获取到域名关键字段
            return false;
        }
        if (startIndex >= endIndex) { // 这个不应该存在
            return false;
        }

        try {
            String subStr = str.substring(startIndex, endIndex);
            String substrFinal = subStr.substring(6);
            String ascill = ZMCommUtil.convertHexToString(substrFinal);
            String baseUri = ZMCommUtil.mdDecryptIcon(ascill, CommConfig.URL_DECODE_KEY); //异或解密
            String[] urlList = baseUri.split("\\|");
            if (urlList.length > 0) {
                instance.baseUrl = urlList[0];
                SPUtils.getInstance().put(SP_BASE_URL, urlList[0]);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }


    public void fetchURL(CommCallBack callBack) {
//        callBack.complete(0, "域名解析OK");
        getBaseUrl(CommConfig.DOMAIN_MAIN, callBack);
    }


    /**
     * 获取接口url
     *
     * @param imgUrl
     * @param
     */
    private void getBaseUrl(final String imgUrl, final CommCallBack callBack) {
        OkGo.<String>get(imgUrl)
                .tag("baseurl")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (parseUrl(response.body())) {
                            callBack.complete(0, "域名解析OK");
                        } else {
                            if (CommConfig.DOMAIN_BACK.equals(imgUrl)) {
//                                callBack.complete(BaseApi.ERROR_CODE_APP, "域名解析失败");
                            } else {
                                getBaseUrl(CommConfig.DOMAIN_BACK, callBack);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        // 需要优化
//                        String msg = response.getException().getMessage();
//                        if (msg != null && msg.contains("Unable to resolve host")) {
//                            if (!NetworkUtils.isAvailableByPing()) {
//                                ToastUtil.showToast("请检查网络");
//                                urlInterface.fail();
//                            } else {
//                                ToastUtil.showToast("域名无法访问");
//                                urlInterface.fail();
//                            }
//                        }
                        if (CommConfig.DOMAIN_BACK.equals(imgUrl)) {
//                            callBack.complete(BaseApi.ERROR_CODE_APP, "域名解析失败");
                        } else {
                            getBaseUrl(CommConfig.DOMAIN_BACK, callBack);
                        }
                    }
                });
    }


}
