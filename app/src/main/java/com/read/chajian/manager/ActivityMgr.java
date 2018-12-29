package com.read.chajian.manager;

import com.blankj.utilcode.util.SPUtils;

/**
 * Created by USER on 2018/12/25.
 */

public class ActivityMgr {
    // 当前activity

    private static ActivityMgr instance;
    private String mCurrentActivity = "";

    private static final String SP_CURRENT_ACTVIITY = "sp_current_activity";

    public static ActivityMgr getInstance() {
        if (instance == null) {
            instance = new ActivityMgr();
        }
        return instance;
    }


    public String getmCurrentActivity() {
        return mCurrentActivity = SPUtils.getInstance().getString(SP_CURRENT_ACTVIITY);
    }

    public void setmCurrentActivity(String mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
        SPUtils.getInstance().put(SP_CURRENT_ACTVIITY, mCurrentActivity);
    }
}
