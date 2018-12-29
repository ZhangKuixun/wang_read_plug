package com.read.chajian.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.read.AppContext;
import com.read.chajian.R;


/**
 * Created by librabin on 16/9/12.
 */
public class ToastUtil {

    private static String lastToast = "";
    private static long lastToastTime;


    public static void showSuccToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT, Gravity.TOP | Gravity.FILL_HORIZONTAL,
                R.color.color_5fbfb1);
    }

    public static void showErrorToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT, Gravity.TOP | Gravity.FILL_HORIZONTAL,
                R.color.color_feda11);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT, Gravity.TOP | Gravity.FILL_HORIZONTAL,
                R.color.default_theme_color);
    }


    public static void showToast(String message, int duration,
                                 int gravity, int backcolor) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast)
                    || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(AppContext.getInstance()).inflate(
                      R.layout.base_toast, null);
                ((TextView) view.findViewById(R.id.tv_toast)).setText(message);
                (view.findViewById(R.id.ll_toast_back)).setBackgroundResource(backcolor);
                Toast toast = new Toast(AppContext.getInstance());
                toast.setView(view);
                toast.setGravity(gravity, 0, 0);
                toast.setDuration(duration);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }


}