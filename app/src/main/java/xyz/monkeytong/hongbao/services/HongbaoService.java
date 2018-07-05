package xyz.monkeytong.hongbao.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.graphics.Path;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;

import xyz.monkeytong.hongbao.entity.RlEntiity;
import xyz.monkeytong.hongbao.utils.HongbaoSignature;
import xyz.monkeytong.hongbao.utils.LogUtil;
import xyz.monkeytong.hongbao.utils.PowerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private static final String HUOSHAN_GUANZHU_ACTIVITY = "follow.ui.FollowingActivity";
    private static final String HUOSHAN_TITLE_PERSON = "关注的人";
    private static final String HUOSHAN_YI_GUANZHU = "已关注";

    private static final String DIAN_ZAN_ID = "com.ss.android.ugc.aweme:id/a8w";


    private String currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
    Handler mhandle = new Handler();
    private AccessibilityNodeInfo rootNodeInfo, mReceiveNode, mUnpackNode;
    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived;
    private int mUnpackCount = 0;
    private boolean mMutex = false, mListMutex = false, mChatMutex = false;
    private HongbaoSignature signature = new HongbaoSignature();

    private PowerUtil powerUtil;
    private SharedPreferences sharedPreferences;
    //搜索按钮id
    private static final String DOUYIN_SEARCH_IMAGE_ID = "com.ss.android.ugc.aweme:id/a1c";

    //editview id
    private static final String DOUYIN_SEARCH_EDITVIEW = "com.ss.android.ugc.aweme:id/kc";

    //点击视频所处于的状态的android.support.v7.widget.RecyclerView

    private static final String DOUYIN_RECYCLERVIEW = "com.ss.android.ugc.aweme:id/g8";

    //评论内容
    private static final String COMMENT_PINGLUN = "com.ss.android.ugc.aweme:id/r3";

    /**
     * AccessibilityEvent
     *
     * @param event 事件
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.e(event.getEventType());
        if (sharedPreferences == null) return;

        setCurrentActivityName(event);
        if (!mMutex) {
            mMutex = true;
            watchList(event);
            mMutex = false;
        }
//
        if (!mListMutex) {
            mListMutex = true;
            clickVideo(event);
            mListMutex = false;
        }
//        LookViedo(event);
        clickZanComment(event);


        /* 检测通知消息 */

//            if (sharedPreferences.getBoolean("pref_watch_notification", false) && watchNotifications(event)) {
//                return;
//            }
//            if (sharedPreferences.getBoolean("pref_watch_list", false) && watchList(event)){
//                return;
//            }
//            mListMutex = false;
//        }

//        if (!mChatMutex) {
//            mChatMutex = true;
//            if (sharedPreferences.getBoolean("pref_watch_chat", false)) watchChat(event);
//            mChatMutex = false;
//        }
    }

    //视频点击处理
    private void clickZanComment(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        //视频播放界面
        if (currentActivityName.contains("com.ss.android.ugc.live/.detail.ui.DetailActivity")) {
            final AccessibilityNodeInfo info = event.getSource();
            LogUtil.e("zan");
//                printPacketInfo(info);
            try {
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<AccessibilityNodeInfo> nodes;
                        int zan = 0;
                        nodes = info.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.live:id/like_video");
                        AccessibilityNodeInfo infos = nodes.get(0);
                        if (ObjectUtils.isNotEmpty(infos)) {
                            if (infos.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {

                            }
                            LogUtil.e(infos);
                            LogUtil.e(infos.getViewIdResourceName());
                            LogUtil.e(infos.getDrawingOrder());
                            LogUtil.e(infos.describeContents());
                            LogUtil.e(infos.getRangeInfo());
                        }
                    }
                }, 2000);
            } catch (Exception e) {
                LogUtil.e(e);
            }
        }
    }

    private void LookViedo(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }
        LogUtil.e(currentActivityName);
        try {
            //抖音主界面 跳转自动搜索用户界面
            if (currentActivityName.contains("main.MainActivity")) {

                findNodeByViewID(event.getSource(), DOUYIN_SEARCH_IMAGE_ID, 1);


            }
            //热门搜索界面
            if (currentActivityName.contains("discover.activity.HotSearchAndDiscoveryActivity")) {
                findNodeByViewID(event.getSource(), DOUYIN_SEARCH_EDITVIEW, 1);
            }
            //详情界面
            if (currentActivityName.contains("profile.ui.UserProfileActivity")) {

                findNodeByViewID(event.getSource(), DOUYIN_RECYCLERVIEW, 1);
            }
            //视频播放界面
            if (currentActivityName.contains("detail.ui.DetailActivity")) {
                final AccessibilityNodeInfo info = event.getSource();

//                printPacketInfo(info);
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findNodeByViewID(info, COMMENT_PINGLUN, 1);
                    }
                }, 2000);

            }

        } catch (Exception e) {
            LogUtil.e(e);
        }


    }

    private static int tabcount = -1;
    private static StringBuilder sb;

    public static void printPacketInfo(AccessibilityNodeInfo root) {
        sb = new StringBuilder();
        tabcount = 0;
        int[] is = {};
        analysisPacketInfo(root, is);
        LogUtil.e(sb.toString());
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
            sb.append("text:").append(text);
        } else if ("Button".equals(name)) {
            CharSequence text = info.getText();
            sb.append("Button:").append(text);
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
//    private void watchChat(AccessibilityEvent event) {
//        this.rootNodeInfo = getRootInActiveWindow();
//
//        if (rootNodeInfo == null) return;
//
//        mReceiveNode = null;
//        mUnpackNode = null;
//
//        checkNodeInfo(event.getEventType());
//
//        /* 如果已经接收到红包并且还没有戳开 */
//        if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReceiveNode != null)) {
//            mMutex = true;
//
//            mReceiveNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            mLuckyMoneyReceived = false;
//            mLuckyMoneyPicked = true;
//        }
//        /* 如果戳开但还未领取 */
//        if (mUnpackCount == 1 && (mUnpackNode != null)) {
//            int delayFlag = sharedPreferences.getInt("pref_open_delay", 0) * 1000;
//            new android.os.Handler().postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            try {
//                                openPacket();
//                            } catch (Exception e) {
//                                mMutex = false;
//                                mLuckyMoneyPicked = false;
//                                mUnpackCount = 0;
//                            }
//                        }
//                    },
//                    delayFlag);
//        }
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


            getPackageManager().getActivityInfo(componentName, 0);

            currentActivityName = componentName.flattenToShortString();
            //需要清理保存的数据
//            if (!currentActivityName.contains("FollowingActivity")) {
//                clickMap.clear();
//            }
            LogUtil.e(currentActivityName);
        } catch (PackageManager.NameNotFoundException e) {
            currentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
    }

    private int clickNum = 0;

    //所有未点击的元素集合
    Map<Integer, Boolean> clickMap = new HashMap<Integer, Boolean>();
    Map<Integer, Boolean> clickVideoMap = new HashMap<Integer, Boolean>();
    private boolean videoAllClick = false;

    //点击视频
    private synchronized void clickVideo(AccessibilityEvent event) {

        //元素的个数
        int Num = 0;

        LogUtil.e(currentActivityName);
        try {
            if (currentActivityName.contains("UserProfileActivity")) {

                AccessibilityNodeInfo eventSource = event.getSource();
                List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.live:id/tl");
//                LogUtil.e(nodes.toString());
                if (ObjectUtils.isNotEmpty(nodes)) {
                    AccessibilityNodeInfo rlInfo = nodes.get(0);
                    rlInfo.refresh();
                    Num = rlInfo.getChildCount();
                    if (Num == 0) {
                        return;
                    }

                    for (int i = 0; i < Num; i++) {
//                        LogUtil.e(rlInfo.getChild(i));
//                        LogUtil.e(rlInfo.getChild(i).getChild(0).hashCode());
                        if (!clickVideoMap.containsKey(rlInfo.getChild(i).getChild(0).hashCode())) {
                            clickVideoMap.put(rlInfo.getChild(i).getChild(0).hashCode(), false);
                        }
                    }
//                    rlInfo.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Iterator iter = clickVideoMap.entrySet().iterator();
                    if (!videoAllClick) {
                        while (iter.hasNext()) {

                            Map.Entry entry = (Map.Entry) iter.next();
                            int key = (int) entry.getKey();
                            boolean val = (boolean) entry.getValue();
                            if (!val) {
                                videoAllClick = val;
                                //记录是否点击过
                                clickVideoMap.put(key, true);
                                for (int i = 0; i < Num; i++) {
                                    if (rlInfo.getChild(i).getChild(0).hashCode() == key) {
                                        rlInfo.getChild(i).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        break;
                                    }
                                }

                                break;
                            } else {
                                videoAllClick = val;
                            }
                        }
                    }
                    LogUtil.e(clickVideoMap.values());
                    LogUtil.e(videoAllClick);

                    //全部点击完自动滑动界面
                    if (rlInfo.isScrollable()) {

                        if (videoAllClick) {
                            LogUtil.e("滑动没完");
                            //滑动会有错乱。暂时不做
//                            rlInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                            if (rlInfo.getActionList().contains(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)) {
//                                if (clickVideoMap.values().toString().contains("false")) {
//                                    videoAllClick = false;
//                                } else {
//                                    videoAllClick = true;
//                                }
//                                if (videoAllClick) {
                            clickVideoMap.clear();
                            backFragment();
                            //               }

                        }
                        videoAllClick = false;
                        //}
                    }
                } else {
                    if (videoAllClick) {
                        LogUtil.e("滑动到底");
                        backFragment();
                        clickVideoMap.clear();
                        videoAllClick = false;
                    }
                }


            } else {
                if (currentActivityName.contains("FollowingActivity")) {
                    clickMap.clear();
                }
            }
        } catch (
                Exception e)

        {
            LogUtil.e(e);
        }

    }

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


    //关注列表点击
    private void watchList(AccessibilityEvent event) {
        //元素的个数
        int Num = 0;
//        if (currentActivityName.contains("UserProfileActivity")) {
//            backFragment();
//            return;
//        }
        try {
            AccessibilityNodeInfo eventSource = event.getSource();
            // Not a message

            if (currentActivityName.contains("FollowingActivity")) {
//                if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
////                if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventSource == null) {
////                    return;
////                }
//                }


//            List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(HUOSHAN_YI_GUANZHU);
                //获取RecyclerView
                List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByViewId("com.ss.android.ugc.live:id/recycler_view");

                AccessibilityNodeInfo rlInfo = nodes.get(0);
                rlInfo.refresh();

//                rlInfo.recycle();


                if (ObjectUtils.isNotEmpty(nodes)) {

                    Num = rlInfo.getChildCount();
                    LogUtil.e(Num);
                    if (Num == 0) {
                        return;
                    }
                    //添加所有的RecyclerView元素
                    for (int i = 0; i < Num; i++) {
                        LogUtil.e(rlInfo.getChild(i));
                        LogUtil.e(rlInfo.getChild(i).hashCode());
                        if (!clickMap.containsKey(rlInfo.getChild(i).hashCode())) {
                            clickMap.put(rlInfo.getChild(i).hashCode(), false);
                        }
                    }
                    Iterator iter = clickMap.entrySet().iterator();
                    int all = 0;
                    while (iter.hasNext()) {

                        Map.Entry entry = (Map.Entry) iter.next();
                        int key = (int) entry.getKey();
                        boolean val = (boolean) entry.getValue();
                        if (!val) {
                            //记录是否点击过
                            clickMap.put(key, true);
                            for (int i = 0; i < Num; i++) {
                                if (rlInfo.getChild(i).hashCode() == key) {
                                    rlInfo.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    break;
                                }
                            }
                            all++;
                            break;
                        }


                    }

                    //全部点击完自动滑动界面
                    if (rlInfo.isScrollable()) {
                        if (all == 0) {
                            if (!rlInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                                backFragment();
                            }
                        }
                    }
                    LogUtil.e(clickMap.keySet().toString());
                    LogUtil.e(clickMap.values().toString());
                    LogUtil.e(rlInfo);
                } else {
                    LogUtil.e("assa");
                }
            }


        } catch (Exception e) {
            LogUtil.e(e);
        }

//        return false;
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

    private void checkNodeInfo(int eventType) {
        if (this.rootNodeInfo == null) return;

        if (signature.commentString != null) {
            sendComment();
            signature.commentString = null;
        }

        /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        AccessibilityNodeInfo node1 = (sharedPreferences.getBoolean("pref_watch_self", false)) ?
                this.getTheLastNode(WECHAT_VIEW_OTHERS_CH, WECHAT_VIEW_SELF_CH) : this.getTheLastNode(WECHAT_VIEW_OTHERS_CH);
        if (node1 != null &&
                (currentActivityName.contains(WECHAT_LUCKMONEY_CHATTING_ACTIVITY)
                        || currentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY))) {
            String excludeWords = sharedPreferences.getString("pref_watch_exclude_words", "");
            if (this.signature.generateSignature(node1, excludeWords)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = node1;
                Log.d("sig", this.signature.toString());
            }
            return;
        }

        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        AccessibilityNodeInfo node2 = findOpenButton(this.rootNodeInfo);
        if (node2 != null && "android.widget.Button".equals(node2.getClassName()) && currentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            mUnpackNode = node2;
            mUnpackCount += 1;
            return;
        }

        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        boolean hasNodes = this.hasOneOfThoseNodes(
                WECHAT_BETTER_LUCK_CH, WECHAT_DETAILS_CH,
                WECHAT_BETTER_LUCK_EN, WECHAT_DETAILS_EN, WECHAT_EXPIRES_CH);
        if (mMutex && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && hasNodes
                && (currentActivityName.contains(WECHAT_LUCKMONEY_DETAIL_ACTIVITY)
                || currentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY))) {
            mMutex = false;
            mLuckyMoneyPicked = false;
            mUnpackCount = 0;
            performGlobalAction(GLOBAL_ACTION_BACK);
            signature.commentString = generateCommentString();
        }
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
