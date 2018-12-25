package com.read.chajian.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.ObjectUtils;
import com.read.chajian.utils.HongbaoSignature;
import com.read.chajian.utils.LogUtil;
import com.read.chajian.utils.PowerUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HongbaoService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String WECHAT_DETAILS_EN = "Details";
    private static final String WECHAT_DETAILS_CH = "红包详情";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_BETTER_LUCK_CH = "手慢了";
    private static final String WECHAT_EXPIRES_CH = "已超过24小时";
    private static final String WECHAT_VIEW_SELF_CH = "查看红包";
    private static final String WECHAT_VIEW_OTHERS_CH = "领取红包";
    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_LUCKMONEY_RECEIVE_ACTIVITY = "LuckyMoneyReceiveUI";
    private static final String WECHAT_LUCKMONEY_DETAIL_ACTIVITY = "LuckyMoneyDetailUI";
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_CHATTING_ACTIVITY = "ChattingUI";


    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    Handler mhandle = new Handler();
    private AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode;
    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived;
    private int mUnpackCount = 0;
    private boolean mMutex = false, mListMutex = false, mTOULIStMutex = false, mQUHUAMutex = false;
    private HongbaoSignature signature = new HongbaoSignature();

    private PowerUtil powerUtil;
    private SharedPreferences sharedPreferences;


    private static final String QU_TOUTIAO_MAIN = "com.jifen.qkbase.main.MainActivity";


    private static final String ReadActivity = "content.view.activity.NewsDetailActivity";

    /**
     * AccessibilityEvent
     *
     * @param event 事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.e(event.getEventType());
        //设置当前的activity
        setCurrentActivityName(event);
//        if (TouTiaoTui()) {
//            AppUtils.launchApp("com.cashtoutiao");
//        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            SuaaTouTiao(event);
            if (!mQUHUAMutex) {
                mQUHUAMutex = true;
                clickTouTiaoContent(event);
            }


        }
        if (!mTOULIStMutex) {
            mTOULIStMutex = true;
            clickTouTiaoTWO(event);
        }


    }

    //点击首页的文章
    private void clickTouTiaoTWO(final AccessibilityEvent event) {
        final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
        int time = 2000;
        try {
            final int[] is = {};
            LogUtil.e(currentActivityName);
            if (currentActivityName.contains(QU_TOUTIAO_MAIN) || currentActivityName.contains(".main.MainTabActivity")) {
                if (currentActivityName.contains(".main.MainTabActivity")) {
                    time = 0;
                }
                mhandle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (currentActivityName.contains(".main.MainTabActivity")) {

                        }
                        LogUtil.e(printPacketInfo(eventSource));
                        try {
                            AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, RECYCLERVIEWNAME, is);


                            if (ObjectUtils.isNotEmpty(nodes)) {

                                int count = nodes.getChildCount();
                                LogUtil.e(count);
                                if (count > 0) {
                                    for (int i = 0; i < count; i++) {
                                        AccessibilityNodeInfo ll = nodes.getChild(i);
                                        if (ObjectUtils.isNotEmpty(ll)) {
                                            if (ll.getClassName().equals("android.widget.FrameLayout")) {
                                                mTOULIStMutex = false;
                                                continue;
                                            } else {
                                                if (!printPacketInfo(ll).contains("视频") && !printPacketInfo(ll).contains("置顶") && !printPacketInfo(ll).contains("更新") && !printPacketInfo(ll).contains("热门")) {
                                                    LogUtil.e(printPacketInfo(ll));
                                                    LogUtil.e(ll.performAction(AccessibilityNodeInfo.ACTION_CLICK));
                                                    mTOULIStMutex = false;
                                                    return;
                                                } else {
                                                    LogUtil.e(printPacketInfo(ll));
                                                    mTOULIStMutex = false;
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    mTOULIStMutex = false;
                                } else {
                                    mTOULIStMutex = false;
                                }
                            } else {
                                mTOULIStMutex = false;
                            }
                        } catch (Exception e) {
                            mTOULIStMutex = false;
                            LogUtil.e(e);
                        }
                    }
                }, time);


            } else {
                mTOULIStMutex = false;
            }
            LogUtil.e(mTOULIStMutex);
        } catch (Exception e) {
            LogUtil.e(e);
            mTOULIStMutex = false;
        }
    }


    //点击首页刷新
    private boolean SuaaTouTiao(AccessibilityEvent event) {
        try {
            AccessibilityNodeInfo nodes = null;
            final int[] is = {};
            final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
            if (currentActivityName.contains(QU_TOUTIAO_MAIN)) {

                if (nodes == null) {
                    nodes = recycleFindRecyView(eventSource, Button_NAME, is);
                    if (ObjectUtils.isNotEmpty(nodes)) {
                        if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                            return true;
                        }
                    }
                } else {
                    nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }


            } else if (currentActivityName.contains(".main.MainTabActivity")) {
                LogUtil.e(printPacketInfo(eventSource));

            } else {
                return false;
            }

        } catch (Exception e) {
            LogUtil.e(e);
            return false;
        }
        return false;
    }

    //点击头条推送
    private boolean TouTiaoTui() {
        try {
            AccessibilityNodeInfo nodes = null;
            final int[] is = {};

            final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
//            LogUtil.e(eventSource);
            if (nodes == null) {
                nodes = recycleFindRecyView(eventSource, TEXTVIEW_NAME, is);
                if (ObjectUtils.isNotEmpty(nodes)) {
                    if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }


        } catch (Exception e) {
            LogUtil.e(e);
            return false;
        }
        return false;
    }


    //点击Web内容
    private void clickTouTiaoContent(AccessibilityEvent event) {

        try {
            if (currentActivityName.contains(ReadActivity) || currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {

                final AccessibilityNodeInfo eventSource = getRootInActiveWindow();
                int time = 2800;
                if (currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {

                    time = 6000;
                }


                mhandle.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (currentActivityName.contains("newsdetail.NewsDetailActivity")||currentActivityName.contains("ImageNewsDetailActivity")) {

                            backFragment();
                            mQUHUAMutex = false;
                            return;
                        }
                        AccessibilityNodeInfo node = recycleFindListView(eventSource);
                        LogUtil.e(printPacketInfo(eventSource));
                        if (ObjectUtils.isNotEmpty(node)) {

                            if (node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                                mhandle.postDelayed(this, 3000);
                            } else {
                                backFragment();
                                mQUHUAMutex = false;
                                return;
                            }

                        } else {
                            mQUHUAMutex = false;
                        }


                    }
                }, time);


            } else {
                mQUHUAMutex = false;
            }
        } catch (Exception e) {
            mQUHUAMutex = false;
        }
    }

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

    private static final String RECYCLERVIEWNAME = "android.support.v7.widget.RecyclerView";

    private static final String Button_NAME = "android.widget.Button";

    private static final String TEXTVIEW_NAME = "android.widget.TextView";

    public AccessibilityNodeInfo recycleFindRecyView(AccessibilityNodeInfo node, String type, int... ints) {
        if (node == null) {
            return null;
        }

        String name = node.getClassName().toString();
        if (type.equals(RECYCLERVIEWNAME)) {
            if (name.equals(type)) {
                return node;
            }
        } else if (type.equals(Button_NAME)) {
            LogUtil.e(name + " " + node.getText());
            if (name.equals(type) && node.getText().equals("刷新")) {

                LogUtil.e(printPacketInfo(node));
                return node;
            }
        } else if (type.equals(TEXTVIEW_NAME)) {
            if (name.equals(type) && node.getText().equals("忽略")) {
                LogUtil.e(printPacketInfo(node));
                return node;
            }
        }

        int count = node.getChildCount();
        if (count > 0) {
            tabcount++;
            int len = ints.length + 1;
            int[] newInts = Arrays.copyOf(ints, len);

            for (int i = 0; i < count; i++) {
                newInts[len - 1] = i;
                AccessibilityNodeInfo result = recycleFindRecyView(node.getChild(i), type, newInts);
                if (result == null) {
                    continue;
                } else {
                    return result;
                }
            }
            tabcount--;
        }
        return null;
    }


    private static int tabcount = -1;
    private static StringBuilder sb;

    public static String printPacketInfo(AccessibilityNodeInfo root) {
        sb = new StringBuilder();
        tabcount = 0;
        int[] is = {};
        analysisPacketInfo(root, is);
        return sb.toString();
    }

    //打印此时的界面状况,便于分析
    private static void analysisPacketInfo(AccessibilityNodeInfo info, int... ints) {
        if (info == null) {
            return;
        }
        if (tabcount > 0) {
            for (int i = 0; i < tabcount; i++) {
                sb.append("\t\t");
            }
        }
        if (ints != null && ints.length > 0) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < ints.length; j++) {
                s.append(ints[j]).append(".");
            }
            sb.append(s).append(" ");
        }
        String name = info.getClassName().toString();
        String[] split = name.split("\\.");
        name = split[split.length - 1];
        if ("TextView".equals(name)) {
            CharSequence text = info.getText();
            if ("刷新".equals(text)) {
                LogUtil.e(info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK));
            }
            sb.append("text:").append(text);
        } else if ("Button".equals(name)) {
            CharSequence text = info.getText();
            sb.append("Button:").append(text);
        } else if ("View".equals(name)) {
            CharSequence text = info.getText();
//            if (text.toString().contains("展开全文")) {
//                LogUtil.e("ffasf");
//                LogUtil.e(info);
//                LogUtil.e(info.getParent());
//                LogUtil.e(info.performAction(AccessibilityNodeInfo.ACTION_CLICK));
//            }
            sb.append("View:").append(text);
        } else {
            sb.append(name);
        }
        sb.append("\n");

        int count = info.getChildCount();
        if (count > 0) {
            tabcount++;
            int len = ints.length + 1;
            int[] newInts = Arrays.copyOf(ints, len);

            for (int i = 0; i < count; i++) {
                newInts[len - 1] = i;
                analysisPacketInfo(info.getChild(i), newInts);
            }
            tabcount--;
        }

    }
//    private static void analysisPacketInfo(AccessibilityNodeInfo info, int... ints) {
//        if (info == null) {
//            return;
//        }
//        if (tabcount > 0) {
//            for (int i = 0; i < tabcount; i++) {
//                sb.append("\t\t");
//            }
//        }
//        if (ints != null && ints.length > 0) {
//            StringBuilder s = new StringBuilder();
//            for (int j = 0; j < ints.length; j++) {
//                s.append(ints[j]).append(".");
//            }
//            sb.append(s).append(" ");
//        }
//        String name = info.getClassName().toString();
//        String[] split = name.split("\\.");
//        name = split[split.length - 1];
//        if ("TextView".equals(name)) {
//            CharSequence text = info.getText();
//            sb.append("text:").append(text);
//        } else if ("Button".equals(name)) {
//            CharSequence text = info.getText();
//            sb.append("Button:").append(text);
//        } else {
//            sb.append(name);
//        }
//        sb.append("\n");
//
//        int count = info.getChildCount();
//        if (count > 0) {
//            tabcount++;
//            int len = ints.length + 1;
//            int[] newInts = Arrays.copyOf(ints, len);
//
//            for (int i = 0; i < count; i++) {
//                newInts[len - 1] = i;
//                analysisPacketInfo(info.getChild(i), newInts);
//            }
//            tabcount--;
//        }
//
//    }


//    private void openPacket() {
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float dpi = metrics.density;
//        if (android.os.Build.VERSION.SDK_INT <= 23) {
//            mUnpackNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        } else {
//            if (android.os.Build.VERSION.SDK_INT > 23) {
//
//                Path path = new Path();
//                if (640 == dpi) {
//                    path.moveTo(720, 1575);
//                } else {
//                    path.moveTo(540, 1060);
//                }
//                GestureDescription.Builder builder = new GestureDescription.Builder();
//                GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 450, 50)).build();
//                dispatchGesture(gestureDescription, new GestureResultCallback() {
//                    @Override
//                    public void onCompleted(GestureDescription gestureDescription) {
//                        Log.d("test", "onCompleted");
//                        mMutex = false;
//                        super.onCompleted(gestureDescription);
//                    }
//
//                    @Override
//                    public void onCancelled(GestureDescription gestureDescription) {
//                        Log.d("test", "onCancelled");
//                        mMutex = false;
//                        super.onCancelled(gestureDescription);
//                    }
//                }, null);
//
//            }
//        }
//    }

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
//            currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
    }


    //所有未点击的元素集合
    Map<Integer, Boolean> clickMap = new HashMap<Integer, Boolean>();


    public static AccessibilityNodeInfo findNodeByViewName(AccessibilityNodeInfo info, String viewName) {
        String name = info.getClassName().toString();
        String[] split = name.split("\\.");
        name = split[split.length - 1];
        if (name.equals(viewName)) {
            return info;
        } else {

            int count = info.getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo inf = findNodeByViewName(info.getChild(i), viewName);
                    if (inf != null) {
                        return inf;
                    }
                }
            } else {
                return null;
            }
        }
        return null;
    }


    /**
     * @param info
     * @param viewName
     * @param type     1是id/0是文字
     */
    public void findNodeByViewID(final AccessibilityNodeInfo info, String viewName, int type) {
        List<AccessibilityNodeInfo> nodes;
        if (type == 1) {
            nodes = info.findAccessibilityNodeInfosByViewId(viewName);
        } else {
            nodes = info.findAccessibilityNodeInfosByText(viewName);
        }
        AccessibilityNodeInfo infos = nodes.get(0);
        LogUtil.e(infos);
        switch (infos.getClassName() + "") {
            //搜索按钮点击
            case "android.widget.ImageView":
                infos.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;

            case "android.widget.EditText":
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo
                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "98440800");
                infos.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                findNodeByViewID(info, "搜索", 0);

                break;
            case "android.widget.TextView":
                infos.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                //检测当前可点击的LinearLayout的子类
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findNodeByViewID(info, "com.ss.android.ugc.aweme:id/ahl", 1);
                    }
                }, 2000);

                break;
            case "android.widget.LinearLayout":
                AccessibilityNodeInfo relatvieInfo = nodes.get(0).getChild(0).getChild(0);
                relatvieInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            case "android.support.v7.widget.RecyclerView":
                final AccessibilityNodeInfo recylerviewInfo = nodes.get(0);
                recylerviewInfo.refresh();
                if (ObjectUtils.isNotEmpty(nodes)) {
                    int Num = 0;
                    Num = recylerviewInfo.getChildCount();
                    LogUtil.e(Num);
                    if (Num == 0) {
                        return;
                    }
//                    //添加所有的RecyclerView元素
//                    for (int i = 0; i < Num; i++) {
//                        LogUtil.e(recylerviewInfo.getChild(i).getChild(0));
//
//
//                    }
                    mhandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recylerviewInfo.getChild(0).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }, 2000);


                }
                break;
            case "android.widget.FrameLayout":
                infos.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;

            default:
                LogUtil.e(nodes);
                break;
        }


    }




    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        LogUtil.e("back");

        return super.onKeyEvent(event);

    }

    private void backFragment() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        // Not a notification
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return false;

        // Not a hongbao
        String tip = event.getText().toString();
        if (!tip.contains(WECHAT_NOTIFICATION_TIP)) return true;

        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                /* 清除signature,避免进入会话后误判 */
                signature.cleanSignature();

                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onInterrupt() {

    }

    private AccessibilityNodeInfo findOpenButton(AccessibilityNodeInfo node) {
        if (node == null)
            return null;

        //非layout元素
        if (node.getChildCount() == 0) {
            if ("android.widget.Button".equals(node.getClassName()))
                return node;
            else
                return null;
        }

        //layout元素，遍历找button
        AccessibilityNodeInfo button;
        for (int i = 0; i < node.getChildCount(); i++) {
            button = findOpenButton(node.getChild(i));
            if (button != null)
                return button;
        }
        return null;
    }


    private void sendComment() {
        try {
            AccessibilityNodeInfo outNode =
                    getRootInActiveWindow().getChild(0).getChild(0);
            AccessibilityNodeInfo nodeToInput = outNode.getChild(outNode.getChildCount() - 1).getChild(0).getChild(1);

            if ("android.widget.EditText".equals(nodeToInput.getClassName())) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo
                        .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, signature.commentString);
                nodeToInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
            }
        } catch (Exception e) {
            // Not supported
        }
    }


    private boolean hasOneOfThoseNodes(String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) return true;
        }
        return false;
    }

    private AccessibilityNodeInfo getTheLastNode(String... texts) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null, tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            if (text == null) continue;

            nodes = this.rootNodeInfo.findAccessibilityNodeInfosByText(text);

            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) return null;
                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                    signature.others = text.equals(WECHAT_VIEW_OTHERS_CH);
                }
            }
        }
        return lastNode;
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

    private String generateCommentString() {
        if (!signature.others) return null;

        Boolean needComment = sharedPreferences.getBoolean("pref_comment_switch", false);
        if (!needComment) return null;

        String[] wordsArray = sharedPreferences.getString("pref_comment_words", "").split(" +");
        if (wordsArray.length == 0) return null;

        Boolean atSender = sharedPreferences.getBoolean("pref_comment_at", false);
        if (atSender) {
            return "@" + signature.sender + " " + wordsArray[(int) (Math.random() * wordsArray.length)];
        } else {
            return wordsArray[(int) (Math.random() * wordsArray.length)];
        }
    }



}
