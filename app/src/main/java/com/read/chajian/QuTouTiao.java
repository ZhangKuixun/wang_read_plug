package com.read.chajian;

import android.content.Context;
import android.os.Handler;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.read.chajian.entity.event.BackEvent;
import com.read.chajian.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;


/**
 * Created by USER on 2018/12/19.
 */

public class QuTouTiao extends Client {

    Handler mHandler = null;
    private int count;

    private int mRondom = 0;

    private AccessibilityNodeInfo webViewNodes;


    public QuTouTiao(Context context) {
        super(context);

        client = "QiTouTiao";
        mHandler = new Handler();
    }


    protected void init(final AccessibilityNodeInfo root) {
        shuaXing(root, "刷新");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * 如果既没找到视频又没找到图库，也没找到图片的时候会中断
                 * 需要优化
                 */

                if (!clickTouTiaoTWO(root)) {
                    if (scrollRecycler(root)) {
                        clickTouTiaoTWO(root);
                    }

                }
            }
        }, 5000);

    }


    //点击首页的文章
    private boolean clickTouTiaoTWO(final AccessibilityNodeInfo root) {

        AccessibilityNodeInfo eventSource = root;
        LogUtils.e(eventSource);
        try {
            final int[] is = {};

            try {
                AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, RECYCLERVIEWNAME, "", is);

                if (ObjectUtils.isNotEmpty(nodes)) {

                    int count = nodes.getChildCount();
                    LogUtil.e(count);
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            AccessibilityNodeInfo ll = nodes.getChild(i);
                            if (ObjectUtils.isNotEmpty(ll)) {
                                if (ll.getClassName().equals("android.widget.FrameLayout")) {
                                    continue;
                                } else {
                                    String packetInfo = printPacketInfo(ll);
                                    if (!packetInfo.contains("置顶") && !printPacketInfo(ll).contains("更新") && !printPacketInfo(ll).contains("广告")) {
                                        //点击文章成功与否
                                        if (ll.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        LogUtil.e(printPacketInfo(ll));
                                        continue;
                                    }
                                }
                            }
                        }

                    }
                }
            } catch (Exception e) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    //如果文章点击不成功 滑动页面
    // TODO: 2018/12/20_ 可能会有元素变化导致的问题
    private boolean scrollRecycler(final AccessibilityNodeInfo root) {
        AccessibilityNodeInfo eventSource = root;
        final int[] is = {};
        AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, RECYCLERVIEWNAME, "", is);
        if (ObjectUtils.isNotEmpty(nodes)) {
            return nodes.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        return false;
    }

    //如果是图库 先查询是否有veiwPage
    public boolean clickTouTiaoContent(final AccessibilityNodeInfo root, String mViewClassName, int rondm) {
        AccessibilityNodeInfo eventSource = root;
        try {
            final int[] is = {};
            if (rondm == 0) {
                int min = 4;
                int max = 6;
                Random random = new Random();
                mRondom = random.nextInt(max) % (max - min + 1) + min;
            } else {
                mRondom = 2;
            }
            final AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, mViewClassName, "", is);

            if (ObjectUtils.isNotEmpty(nodes)) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        count++;
                        if (count < mRondom) {

//                                nodes.performAction(AACTION_CLEAR_FOCUS, ACTION_SELECT, ACTION_CLEAR_SELECTION, ACTION_CLICK, ACTION_LONG_CLICK, ACTION_ACCESSIBILITY_FOCUS, ACTION_NEXT_AT_MOVEMENT_GRANULARITY, ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, ACTION_NEXT_HTML_ELEMENT, ACTION_PREVIOUS_HTML_ELEMENT)
                            nodes.addAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            LogUtils.e(nodes);
                            if (nodes.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {


                                mHandler.postDelayed(this, 5000);
                            } else {
                                EventBus.getDefault().post(new BackEvent("1"));
                            }


                        } else if (count == mRondom) {
                            EventBus.getDefault().post(new BackEvent("1"));
                        }
                    }
                };

                mHandler.postDelayed(r, 3000);

            }
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }
        return false;
    }

    /**
     * @param eventSource
     * @param text        根据名字点击
     * @return
     */
    public boolean shuaXing(AccessibilityNodeInfo eventSource, String text) {
        try {
            AccessibilityNodeInfo nodes = null;
            final int[] is = {};
            LogUtils.e(eventSource);
            nodes = eventSource.findAccessibilityNodeInfosByText(text).get(0);
            LogUtils.e(nodes);
            if (ObjectUtils.isNotEmpty(nodes)) {
                if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * @param eventSource
     * @param text        根据名字点击
     * @return
     */

    public AccessibilityNodeInfo getNodeInfo(AccessibilityNodeInfo eventSource, String text) {
        try {

            return eventSource.findAccessibilityNodeInfosByText("领取").get(0);
        } catch (Exception e) {
            return null;
        }

    }

}
