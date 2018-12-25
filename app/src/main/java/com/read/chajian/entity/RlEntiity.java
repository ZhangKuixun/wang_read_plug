package com.read.chajian.entity;

/**
 * Created by USER on 2018/3/21.
 */

public class RlEntiity {


    private int hashcode;

    private boolean click;


    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public RlEntiity(int hashcode) {
        this.hashcode = hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "RlEntiity{" +
                "hashcode=" + hashcode +
                ", click=" + click +
                '}';
    }
}
