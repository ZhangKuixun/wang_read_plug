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

import com.baina.floatwindowlib.freeposition.DraggableFloatWindow;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.read.chajian.Client;
import com.read.chajian.QuTouTiao;
import com.read.chajian.entity.event.BackEvent;
import com.read.chajian.manager.ActivityMgr;
import com.read.chajian.utils.HongbaoSignature;
import com.read.chajian.utils.LogUtil;
import com.read.chajian.utils.NodesInfo;
import com.read.chajian.utils.PowerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.read.AppContext.getInstance;
import static com.read.AppContext.pkgqukan;


public class ReadToutiaoService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";

    private static final String READ_ACTIVITY = ".NewsDetailActivity";
    private static final String IMAGEREAD_ACTIVITY = ".ImageNewsDetailActivity";
    private static final String VIDEO_ACTIVITY = ".VideoNewsDetailActivity";
    //领取金币 专用取值
    private int mSign = 0;
    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    Handler mhandle = new Handler();


    private PowerUtil powerUtil;
    private SharedPreferences sharedPreferences;


    private static final String QU_TOUTIAO_MAIN = ".main.MainActivity";

    private String packageName;

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);

    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        this.watchFlagsFromPreference();
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
        EventBus.getDefault().unregister(this);
    }

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
//
//                onClick(event);


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
            if (!"".equals(currentActivityName)) {
                ActivityMgr.getInstance().setmCurrentActivity(currentActivityName);

                LogUtils.e(currentActivityName);
            }
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


//        NodesInfo.show(source, TAG, "d");

        switch (packageName) {
            case pkgqukan:
                final QuTouTiao mQuTouTiao = new QuTouTiao(this);
                if (currentActivityName.contains(QU_TOUTIAO_MAIN)) {
//                    LogUtils.e(mQuTouTiao.printPacketInfo(getRootInActiveWindow()));

                    if (ObjectUtils.isNotEmpty(mQuTouTiao.getNodeInfo(getRootInActiveWindow(), "领取"))) {
                        mSign++;
                        if (mSign > 10) {
                            mQuTouTiao.getNodeInfo(getRootInActiveWindow(), "领取").getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            mSign = 0;
                        }
                    }
                    mQuTouTiao.onContentChanged(getRootInActiveWindow());

                } else if (currentActivityName.contains(READ_ACTIVITY)) {
                    mhandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mQuTouTiao.clickTouTiaoContent(getRootInActiveWindow(), Client.WebViewClassName, 0);
                        }
                    }, 5000);

                } else if (currentActivityName.contains(IMAGEREAD_ACTIVITY)) {
                    mhandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mQuTouTiao.clickTouTiaoContent(getRootInActiveWindow(), Client.VIEWPAGER_NAME, 1);
                        }
                    }, 5000);

                } else if (currentActivityName.contains(VIDEO_ACTIVITY)) {
                    int min = 4;
                    int max = 8;
                    Random random = new Random();
                    int time = (random.nextInt(max) % (max - min + 1) + min) * 10000;
                    mhandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                    }, time);
                }
                break;
//            case pkgQQ:
//                new QQClient(this).onContentChanged(getRootInActiveWindow());
//                break;
        }
    }


    /**
     * 选择线路
     *
     * @param backEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectline(BackEvent backEvent) {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }


    private void watchFlagsFromPreference() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.powerUtil = new PowerUtil(this);
        Boolean watchOnLockFlag = sharedPreferences.getBoolean("pref_watch_on_lock", false);
        this.powerUtil.handleWakeLock(watchOnLockFlag);

    }

}
