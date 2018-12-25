package com.read.chajian;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.read.chajian.utils.LogUtil;

/**
 * Created by USER on 2018/12/19.
 */

public class QuTouTiao extends Client {


    public QuTouTiao(Context context) {
        super(context);
        client = "QiTouTiao";
    }


    protected boolean init(AccessibilityNodeInfo root) {

        if (!clickTouTiaoTWO(root)) {
            if (scrollRecycler(root)) {
                return false;
            }
        } else {
            return true;
        }

        return false;
    }


    protected void parser(AccessibilityNodeInfo root) {
        shuaXing(root);

    }



    //点击首页的文章
    private boolean clickTouTiaoTWO(final AccessibilityNodeInfo root) {
        AccessibilityNodeInfo eventSource = root;
        try {
            final int[] is = {};

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
                                    continue;
                                } else {
                                    String packetInfo = printPacketInfo(ll);
                                    if (!packetInfo.contains("视频") && !packetInfo.contains("置顶") && !printPacketInfo(ll).contains("更新") && !printPacketInfo(ll).contains("广告")) {
                                        //点击文章成功与否
                                        return ll.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
        AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, RECYCLERVIEWNAME, is);
        if (ObjectUtils.isNotEmpty(nodes)) {
            return nodes.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }
        return false;
    }

    //如果是图库 先查询是否有veiwPage
    private boolean clickTouTiaoContent(final AccessibilityNodeInfo root) {
        AccessibilityNodeInfo eventSource = root;
        try {
            final int[] is = {};
            AccessibilityNodeInfo nodes = recycleFindRecyView(eventSource, VIEWPAGER_NAME, is);
            if (ObjectUtils.isNotEmpty(nodes)) {
                LogUtils.e(nodes);
                nodes.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    //点击首页刷新
    private boolean shuaXing(AccessibilityNodeInfo eventSource) {
        try {
            AccessibilityNodeInfo nodes = null;
            final int[] is = {};
            nodes = recycleFindRecyView(eventSource, Button_NAME, is);
            if (ObjectUtils.isNotEmpty(nodes)) {
                if (nodes.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    return true;
                }
            }

        } catch (Exception e) {
            LogUtil.e(e);
            return false;
        }
        return false;
    }
}
