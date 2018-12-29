package com.read.chajian.manager;

import android.app.Activity;
import android.os.Environment;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;


import java.io.File;

public class AppMgr {

    private static final String SP_NORMAL_START = "sp_normal_start";

    private static final String SP_APP_COMPANY_NAME = "sp_app_company_name";

    private static final String SP_APP_AGREEMENT_URL = "agreement_url";

    private static final String SP_APP_FREE_RADIUS_PASSWORD = "free_radius_password";

    private static final String SP_APP_FREE_RADIUS_ACCOUNT = "free_radius_account";

    private static final String SP_APP_CLIENTAPIURL = "sp_app_client_api_url";

    private static final String SP_APP_H5_PAY_URL = "sp_app_h5_pay_url";

    private static final String SP_APP_SERVICE_PHONE = "sp_app_service_phone";

    private static final String SP_APP_SERVICE_WEIBO = "sp_app_service_weibo";

    private static final String SP_APP_SERVICE_QQ = "sp_app_service_qq";

    private static final String SP_APP_SERVICE_WECHAT = "sp_app_service_wx";

    private static final String HOME_INIT_DATA_CODE = "haigui_ucenter_ad_1,haigui_ucenter_ad_2," +
            "haigui_ucenter_ad_3,haiguizhuanshu,haigui_rementuijian,haigui_android_node_ad";
    private static AppMgr instance;

    private String mDeviceId;

//    private StartListener mStartListener;

    private String mCheckEnableUrl;


    private String mFreeRadiusAccount;

    private String mFreeRadiusPassword;

//    private StartStep mStartStep = StartStep.FETCH_DOMAIN;
//    private HomeInitBean mHomeInitBean;

    private String HOME_INIT_BEAN = "homeInitBean";

    private AppMgr() {

    }

    public static AppMgr getInstance() {
        if (instance == null) {
            synchronized (AppMgr.class) {
                instance = new AppMgr();
            }
        }
        return instance;
    }


    public String getDeviceId() {
        if (mDeviceId == null) {
            mDeviceId = EncryptUtils.encryptMD5ToString(DeviceUtils.getAndroidID()).toLowerCase();
        }
        return mDeviceId;
    }

//    public void setStartListener(StartListener listener) {
//        mStartListener = listener;
//    }


//    /**
//     * APP到达启动界面后的所有初始化工作
//     */
//    public void handleStart() {
//        switch (mStartStep) {
//            case FETCH_DOMAIN: // 获取域名
//                handleDomain();
//                break;
//            case FETCH_INIT:   // 获取初始化信息
//                handleInit();
//                break;
////            case CHECK_APP_ENBALE:
////                checkAppEnable();
////                break;
//            case CHECK_VERSION:
//                handleCheckUpdate();
//                break;
//
//            case HOME_PAGE_SWITCH:
//                handleHome();
//                break;
//            case FIRST_START:
//                handleFirstStart();
//                break;
//            case AUTO_LOGIN:
//                handleAutoLogin();
//                break;
//
//            case FINISH_LOGIN:
//            case FINISH_MAIN:
//                nofityStartState(0, "ok");
//                break;
//
//
//        }
//    }
//
//
//    private void handleDomain() {
//
//        DomainMgr.getInstance().fetchURL(new CommCallBack() {
//            @Override
//            public void complete(int code, String msg) {
//                if (code == 0) {
//                    mStartStep = StartStep.FETCH_INIT;
//                    handleStart();
//                } else {
//                    nofityStartState(code, msg);
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 处理初始化参数
//     */
//    private void handleInit() {
//        CommApi.fetchInitData(new BaseCallback<BaseResponse<InitBean>>() {
//            @Override
//            public void onSucc(BaseResponse<InitBean> result) {
//                InitBean initBean = result.data;
//                setH5PayUrl(initBean.getH5PayUrl());
//                setAgreementUrl(initBean.getAgreementUrl());
//                setCompanyName(initBean.getCompanyName());
//                setCheckEnableUrl(initBean.getCheckEnableUrl());
//                setmFreeRadiusAccount(initBean.getFreeRadiusAccount());
//                setmFreeRadiusPassword(initBean.getFreeRadiusPassword());
//                setSpAppClientapiurl(initBean.getClientApiUrl());
//                mStartStep = StartStep.CHECK_VERSION;
//                handleStart();
//            }
//
//            @Override
//            public void onFail(ApiException apiError) {
//                nofityStartState(apiError.getErrCode(), apiError.getErrMsg());
//            }
//        }, this);
//    }
//    public void setStartStep(StartStep startStep) {
//        mStartStep = startStep;
//    }
//    /**
//     * 检测APP是否可用
//     */
//    private void checkAppEnable() {
//        if (ObjectUtils.isEmpty(mCheckEnableUrl)) {
//            mStartStep = StartStep.CHECK_VERSION;
//            handleStart();
//            return;
//        }
//
//        ApiHttpClient.post(mCheckEnableUrl, new BaseCallback() {
//            @Override
//            public void onSucc(Object result) {
//                mStartStep = StartStep.CHECK_VERSION;
//                handleStart();
//            }
//
//            @Override
//            public void onFail(ApiException apiError) {
//                if (apiError.getErrCode() == 1) {
//                    nofityStartState(apiError.getErrCode(), apiError.getErrMsg());
//                } else {
//                    mStartStep = StartStep.CHECK_VERSION;
//                    handleStart();
//                }
//            }
//        }, this);
//    }
//
//
//    /**
//     * 检测更新
//     */
//    private void handleCheckUpdate() {
//        CommApi.checkUpdate(new BaseCallback<BaseResponse<VersionBean>>() {
//            @Override
//            public void onSucc(BaseResponse<VersionBean> result) {
//                LogUtils.e(result.data);
//                // 需要更新
//                VersionBean versionBean = result.data;
//                showUpdateDialog(versionBean);
//            }
//
//            @Override
//            public void onFail(ApiException apiError) {
//                if (apiError.getErrCode() == 1) { // 已经是最新版本
//                    mStartStep = StartStep.HOME_PAGE_SWITCH;
//                    handleStart();
//                } else {
//                    nofityStartState(apiError.getErrCode(), apiError.getErrMsg());
//                }
//            }
//        }, this);
//    }
//
//    /**
//     * 初始化主页的数据
//     */
//    private void handleHome() {
//        HomeApi.fetchDataInit(HOME_INIT_DATA_CODE, new BaseCallback<BaseResponse<HomeInitBean>>() {
//            @Override
//            public void onSucc(BaseResponse<HomeInitBean> result) {
//                mHomeInitBean = result.data;
//                ZMCache.get(AppContext.getInstance()).put(HOME_INIT_BEAN, mHomeInitBean);
//                mStartStep = StartStep.FIRST_START;
//
//                handleStart();
//            }
//
//            @Override
//            public void onFail(ApiException apiError) {
//                mStartStep = StartStep.FIRST_START;
//
//                handleStart();
//
//            }
//        }, this);
//    }
//
//    /**
//     * 处理第一次启动APP
//     */
//    private void handleFirstStart() {
//        if (isNormalStart()) {
//            mStartStep = StartStep.AUTO_LOGIN;
//            handleStart();
//            return;
//        }
//
//        CommApi.reportAppState("install", new BaseCallback<BaseResponse<Void>>() {
//            @Override
//            public void onSucc(BaseResponse<Void> result) {
//                setNormalStart();
//                mStartStep = StartStep.AUTO_LOGIN;
//                handleStart();
//            }
//
//            @Override
//            public void onFail(ApiException apiError) {
////                nofityStartState(apiError.getErrCode(), apiError.getErrMsg());
//                mStartStep = StartStep.AUTO_LOGIN;
//                handleStart();
//            }
//        }, this);
//    }
//
//
//    private void handleAutoLogin() {
//
//        int loginType = UserMgr.getInstance().getLoginType();
//        if (loginType == CommConfig.LOGIN_TYPE_ACCOUNT) {
//
//            String accountId = UserMgr.getInstance().getAccountId();
//            String password = UserMgr.getInstance().getPassword();
//            if (ObjectUtils.isEmpty(accountId) || ObjectUtils.isEmpty(password)) {
//                mStartStep = StartStep.FINISH_LOGIN;
//                handleStart();
//            } else {
//                UserMgr.getInstance().doLoginByEmail(accountId, password, new CommCallBack() {
//                    @Override
//                    public void complete(int code, String msg) {
//                        if (code == 0) {
//                            mStartStep = StartStep.FINISH_MAIN;
//                        } else {
//                            mStartStep = StartStep.FINISH_LOGIN;
//                        }
//                        handleStart();
//                    }
//                });
//            }
//        } else if (loginType == CommConfig.LOGIN_TYPE_DEVICE) {
//            UserMgr.getInstance().doLoginByDevice(new CommCallBack() {
//                @Override
//                public void complete(int code, String msg) {
//                    if (code == 0) {
//                        mStartStep = StartStep.FINISH_MAIN;
//                    } else {
//                        mStartStep = StartStep.FINISH_LOGIN;
//                    }
//                    handleStart();
//                }
//            });
//        } else {
//            mStartStep = StartStep.FINISH_LOGIN;
//            handleStart();
//        }
//
//    }
//
//
//    private void showUpdateDialog(VersionBean version) {
//        Activity activity = AppContext.getTopActivity();
//        UIHelper.showUpdateDialog(activity, version);
//    }
//
//
//    private void nofityStartState(int code, String msg) {
//        if (mStartListener != null) {
//            mStartListener.commplete(mStartStep, code, msg,mHomeInitBean);
//        }
//    }
//
//
//


    private boolean isNormalStart() {
        return SPUtils.getInstance().getBoolean(SP_NORMAL_START);
    }

    private void setNormalStart() {
//        UserMgr.getInstance().setLoginType(CommConfig.LOGIN_TYPE_DEVICE);
        SPUtils.getInstance().put(SP_NORMAL_START, true);
    }


    public void setH5PayUrl(String h5PayUrl) {
        SPUtils.getInstance().put(SP_APP_H5_PAY_URL, h5PayUrl);
    }

    public String getH5PayUrl() {
        return SPUtils.getInstance().getString(SP_APP_H5_PAY_URL);
    }

    public void setCompanyName(String companyName) {
        SPUtils.getInstance().put(SP_APP_COMPANY_NAME, companyName);
    }

    public String getCompanyName() {
        return SPUtils.getInstance().getString(SP_APP_COMPANY_NAME);
    }

    public void setAgreementUrl(String agreementUrl) {
        SPUtils.getInstance().put(SP_APP_AGREEMENT_URL, agreementUrl);
    }

    public String getAgreementUrl() {
        return SPUtils.getInstance().getString(SP_APP_AGREEMENT_URL);
    }




    public String getWeiBo() {
        return SPUtils.getInstance().getString(SP_APP_SERVICE_WEIBO);
    }

    public String getWechat() {
        return SPUtils.getInstance().getString(SP_APP_SERVICE_WECHAT);
    }

    public String getQQ() {
        return SPUtils.getInstance().getString(SP_APP_SERVICE_QQ);
    }

    public String getPhone() {
        return SPUtils.getInstance().getString(SP_APP_SERVICE_PHONE);
    }

    public String getApkPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;
    }

    public void setCheckEnableUrl(String url) {
        mCheckEnableUrl = url;
    }




}
