package com.read.chajian.services;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.read.chajian.QuTouTiao;
import com.read.chajian.utils.HongbaoSignature;
import com.read.chajian.utils.LogUtil;
import com.read.chajian.utils.NodesInfo;
import com.read.chajian.utils.PowerUtil;

import static android.content.ContentValues.TAG;
import static com.read.AppContext.pkgqukan;


public class ReadToutiaoService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";


    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    Handler mhandle = new Handler();

    private HongbaoSignature signature = new HongbaoSignature();

    private PowerUtil powerUtil;
    private SharedPreferences sharedPreferences;


    private static final String QU_TOUTIAO_MAIN = ".main.MainActivity";

    private String packageName;
    private static final String READ_ACTIVITY = ".NewsDetailActivity";
    private static final String IMAGEREAD_ACTIVITY = ".ImageNewsDetailActivity";

    /**
     * AccessibilityEvent
     *
     * @param event 事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) {
            Log.d(TAG, "onAccessibilityEvent: package name is null, return");
            return;
        }
        packageName = event.getPackageName() + "";

//        if (TouTiaoTui()) {
//            AppUtils.launchApp("com.cashtoutiao");
//        }
        int eventType = event.getEventType();
        if (eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.w(TAG, AccessibilityEvent.eventTypeToString(eventType));
        } else
            Log.v(TAG, AccessibilityEvent.eventTypeToString(eventType));

        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                LogUtils.e("AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED");
//                onNotification(event);
                break;

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                LogUtil.e(eventType);
                //设置当前的activity
                setCurrentActivityName(event);
                onContentChanged(event);
//                autoLoginWX();
//                onContentChanged(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //设置当前的activity

                LogUtils.e("AccessibilityEvent.TYPE_VIEW_CLICKED");
                onClick(event);
                break;

        }

    }

    private void onClick(AccessibilityEvent event) {
        LogUtils.e(TAG, "onClick " + event.getText());
//        if (event.getSource() == null) {
//            LogUtils.e(TAG, "onClick: event.getSource() is null, return");
//            return;
//        }
        switch (packageName) {
            case pkgqukan:
                LogUtils.e(currentActivityName);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backFragment();
                    }
                }, 10000);

                break;
        }
    }

    /**
     * 设置当前的ActivtyName
     *
     * @param event
     */
    private void setCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );


            currentActivityName = componentName.flattenToShortString();

            LogUtil.e(currentActivityName);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    private void onContentChanged(AccessibilityEvent event) {

        // 只有在一整条消息变动时才进入逻辑, 不然会引入很多无关事件
        AccessibilityNodeInfo source = event.getSource();

        try {
            if (ObjectUtils.isNotEmpty(source)) {
                if (source.getChildCount() == 0)
                    return;
            }
        } catch (NullPointerException e) {
            return;
        }


        NodesInfo.show(source, TAG, "d");

        switch (packageName) {
            case pkgqukan:
                if (currentActivityName.contains(QU_TOUTIAO_MAIN)) {
                    new QuTouTiao(this).onContentChanged(getRootInActiveWindow());
                }
                break;
//            case pkgQQ:
//                new QQClient(this).onContentChanged(getRootInActiveWindow());
//                break;
        }
    }


//    //点击首页刷新
//    private boolean SuaaTouTiao(AccessibilityEvent event) {
//        try {
//            AccessibilityNodeInfo nodes = null;
//            final int[] is = {};
//            final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
//            if (currentActivityName.contains(QU_TOUTIAO_MAIN)) {
//
//                if (nodes == null) {
//                    nodes = recycleFindRecyView(eventSource, Button_NAME, is);
//                    if (ObjectUtils.isNotEmpty(nodes)) {
//                        if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
//                            return true;
//                        }
//                    }
//                } else {
//                    nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    return true;
//                }
//
//
//            } else if (currentActivityName.contains(".main.MainTabActivity")) {
////                LogUtil.e(printPacketInfo(eventSource));
//
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//            LogUtil.e(e);
//            return false;
//        }
//        return false;
//    }

//    //点击头条推送
//    private boolean TouTiaoTui() {
//        try {
//            AccessibilityNodeInfo nodes = null;
//            final int[] is = {};
//
//            final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
////            LogUtil.e(eventSource);
//            if (nodes == null) {
//                nodes = recycleFindRecyView(eventSource, TEXTVIEW_NAME, is);
//                if (ObjectUtils.isNotEmpty(nodes)) {
//                    if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            }
//
//
//        } catch (Exception e) {
//            LogUtil.e(e);
//            return false;
//        }
//        return false;
//    }


//    //点击Web内容
//    private void clickTouTiaoContent(AccessibilityEvent event) {
//
//        try {
//            if (currentActivityName.contains(ReadActivity) || currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {
//
//                final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
//                int time = 2800;
//                if (currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {
//
//                    time = 6000;
//                }
//
//
//                mhandle.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if (currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {
//
//                            backFragment();
//                            mQUHUAMutex = false;
//                            return;
//                        }
//                        AccessibilityNodeInfo node = recycleFindListView(eventSource);
////                        LogUtil.e(printPacketInfo(eventSource));
//                        if (ObjectUtils.isNotEmpty(node)) {
//
//                            if (node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
//                                mhandle.postDelayed(this, 3000);
//                            } else {
//                                backFragment();
//                                mQUHUAMutex = false;
//                                return;
//                            }
//
//                        } else {
//                            mQUHUAMutex = false;
//                        }
//
//
//                    }
//                }, time);
//
//
//            } else {
//                mQUHUAMutex = false;
//            }
//        } catch (Exception e) {
//            mQUHUAMutex = false;
//        }
//    }

    private static final String WebViewClassName = "android.webkit.WebView";

    public AccessibilityNodeInfo recycleFindListView(AccessibilityNodeInfo node) {
        if (ObjectUtils.isNotEmpty(node)) {
            if (node.getChildCount() == 0) {
                return null;
            } else {//listview下面必定有子元素，所以放在此时判断
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (WebViewClassName.equals(node.getClassName()) && node.isScrollable()) {
                        return node;
                    } else if (node.getChild(i) != null) {
                        AccessibilityNodeInfo result = recycleFindListView(node.getChild(i));
                        if (result == null) {
                            continue;
                        } else {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }


    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        LogUtil.e("back");

        return super.onKeyEvent(event);

    }

    private void backFragment() {
        if (currentActivityName.contains(READ_ACTIVITY)) {
            performGlobalAction(GLOBAL_ACTION_BACK);
        } else if (currentActivityName.contains(IMAGEREAD_ACTIVITY)) {

            performGlobalAction(GLOBAL_ACTION_BACK);
        }

    }


    @Override
    public void onInterrupt() {

    }


    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        this.watchFlagsFromPreference();
    }

    private void watchFlagsFromPreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.powerUtil = new PowerUtil(this);
        Boolean watchOnLockFlag = sharedPreferences.getBoolean("pref_watch_on_lock", false);
        this.powerUtil.handleWakeLock(watchOnLockFlag);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_watch_on_lock")) {
            Boolean changedValue = sharedPreferences.getBoolean(key, false);
            this.powerUtil.handleWakeLock(changedValue);
        }
    }

    @Override
    public void onDestroy() {
        this.powerUtil.handleWakeLock(false);
        super.onDestroy();
    }


}
