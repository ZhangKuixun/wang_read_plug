package com.baina.floatwindowlib.freeposition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.baina.floatwindowlib.OnFlingListener;
import com.baina.floatwindowlib.permission.rom.HuaweiUtils;
import com.baina.floatwindowlib.permission.rom.MeizuUtils;
import com.baina.floatwindowlib.permission.rom.MiuiUtils;
import com.baina.floatwindowlib.permission.rom.OppoUtils;
import com.baina.floatwindowlib.permission.rom.QikuUtils;
import com.baina.floatwindowlib.permission.rom.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by chentao on 2018/1/14.
 * 自由拖动的悬浮窗
 */

public class DraggableFloatWindow {

    private static final String TAG = DraggableFloatWindow.class.getSimpleName();
    private static DraggableFloatWindow mDraggableFloatWindow;
    private static WindowManager.LayoutParams mParams = null;
    private static WindowManager mWindowManager = null;
    private static DraggableFloatView mDraggableFloatView;
    private Dialog dialog;


    public DraggableFloatWindow(Context context, View popView) {

        initDraggableFloatView(context);
    }

    /**
     * 第一种得到弹窗的方法
     *
     * @param context，上下文对象
     * @param popView，弹窗内容
     * @return
     */
    public static DraggableFloatWindow getDraggableFloatWindow(Context context, View popView) {
        if (mDraggableFloatWindow == null) {
            synchronized (DraggableFloatWindow.class) {
                if (mDraggableFloatWindow == null) {
                    initDraggableFloatViewWindow(context);
                    mDraggableFloatWindow = new DraggableFloatWindow(context, popView);
                }
            }
        }
        return mDraggableFloatWindow;
    }

    public void setOnTouchButtonListener(DraggableFloatView.OnTouchButtonClickListener touchButtonListener) {
        mDraggableFloatView.setTouchButtonClickListener(touchButtonListener);
    }

    private static boolean showState;

    private void show() {

        attachFloatViewToWindow();
    }

    public void dismiss() {
        detachFloatViewFromWindow();
    }


    public void applyOrShowFloatWindow(Context context) {
        if (checkPermission(context)) {
            show();
        } else {
            applyPermission(context);
        }
    }


    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context);
            }
        } else {
            commonROMPermissionApply(context);
        }
    }

    private void ROM360PermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void oppoROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                commonROMPermissionApplyInternal(context);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
    }

    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(false);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }

    /**
     * attach floatView to window
     */
    private static void attachFloatViewToWindow() {
        showState = true;
        if (mDraggableFloatView == null)
            throw new IllegalStateException("DraggableFloatView can not be null");
        if (mParams == null)
            throw new IllegalStateException("WindowManager.LayoutParams can not be null");
        try {
            mWindowManager.updateViewLayout(mDraggableFloatView, mParams);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            //if floatView not attached to window,addView
            mWindowManager.addView(mDraggableFloatView, mParams);
        }
    }

    /**
     * detach floatView from window
     */
    private static void detachFloatViewFromWindow() {
        // TODO: 18-7-30 @lhr2528 you can fix issue 2 here
        if (showState) {
            mWindowManager.removeView(mDraggableFloatView);
            showState = false;
        }
    }

    /**
     * 初始化initFloatViewWindow参数
     *
     * @param context，上下文对象
     */
    private static void initDraggableFloatViewWindow(Context context) {
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //The default position is vertically to the right
//        mParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        mParams.format = PixelFormat.RGBA_8888;
    }

    /**
     * 初始化touch按钮所在window
     *
     * @param context，上下文对象
     */
    private void initDraggableFloatView(Context context) {
        mDraggableFloatView = new DraggableFloatView(context, new OnFlingListener() {
            @Override
            public void onMove(float moveX, float moveY) {
                mParams.x = (int) (mParams.x + moveX);
                mParams.y = (int) (mParams.y + moveY);
                mWindowManager.updateViewLayout(mDraggableFloatView, mParams);
            }
        });
    }


}
