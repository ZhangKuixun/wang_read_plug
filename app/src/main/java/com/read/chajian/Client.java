/*
 * Copyright © 2016 - 2018 by GitHub.com/JasonQS
 * anti-recall.qsboy.com
 * All Rights Reserved
 */

package com.read.chajian;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.read.chajian.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;


public abstract class Client {


    private Context context;
    protected String client;
    protected String TAG = "Client";

    public Client(Context context) {
        this.context = context;
    }

    protected abstract void init(AccessibilityNodeInfo root);

    //    protected abstract void onContentChanged();
    public void onContentChanged(AccessibilityNodeInfo root) {
        if (root == null) {
            Log.d(TAG, "onContentChanged: root is null, return");
            return;
        }

        init(root);

    }


    public static final String RECYCLERVIEWNAME = "android.support.v7.widget.RecyclerView";

    public static final String BUTTON_NAME = "android.widget.Button";

    public static final String TEXTVIEW_NAME = "android.widget.TextView";

    public static final String WebViewClassName = "android.webkit.WebView";


    public static final String VIEWPAGER_NAME = "android.support.v4.view.ViewPager";

    /**
     * 根据传回来的view 类型做出判断
     *
     * @param node
     * @param type
     * @param ints
     * @return
     */
    public AccessibilityNodeInfo recycleFindRecyView(AccessibilityNodeInfo node, String type, String text, int... ints) {
        if (node == null) {
            return null;
        }

        String name = node.getClassName().toString();
        if (type.equals(RECYCLERVIEWNAME)) {
            if (name.equals(type)) {
                return node;
            }
        } else if (type.equals(BUTTON_NAME)) {

            if (name.equals(type) && node.getText().equals(text)) {
                LogUtil.e(node.getText());
                return node;
            }
        } else if (type.equals(VIEWPAGER_NAME)) {
            if (name.equals(type)) {
                return node;
            }
        } else if (type.equals(WebViewClassName)) {
            if(name.equals(type)){
                LogUtils.e(node);
            }
            if (name.equals(type) && node.isScrollable()) {
                return node;
            }
        } else if (type.equals(TEXTVIEW_NAME)) {
            return node;
        }

        int count = node.getChildCount();
        if (count > 0) {
            tabcount++;
            int len = ints.length + 1;
            int[] newInts = Arrays.copyOf(ints, len);

            for (int i = 0; i < count; i++) {
                newInts[len - 1] = i;
                AccessibilityNodeInfo result = recycleFindRecyView(node.getChild(i), type, text, newInts);
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

    /**
     * 打印当前界面所有的view
     *
     * @param root
     * @return
     */
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
}


