package com.read.chajian.entity.event;

/**
 * Created by USER on 2018/12/25.
 */

public class BackEvent {

    /**
     * code  1  返回当前界面
     */
    private String action;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public BackEvent(String action) {
        this.action = action;
    }
}
