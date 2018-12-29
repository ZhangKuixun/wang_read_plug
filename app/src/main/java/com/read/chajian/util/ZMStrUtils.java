package com.read.chajian.util;

import android.support.annotation.StringRes;

import com.read.AppContext;


/**
 * Created by USER on 2018/9/13.
 */

public class ZMStrUtils {

    public static String getStringById(@StringRes int strId) {
        return AppContext.getInstance().getString(strId);
    }
}

