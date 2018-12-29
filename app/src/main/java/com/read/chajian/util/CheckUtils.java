package com.read.chajian.util;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.RegexUtils;

public class CheckUtils {


    public static boolean checkPwdVerifyCode(String password, String confirmPwd, String verifyCode) {

        if (ObjectUtils.isEmpty(verifyCode)) {
            ToastUtil.showErrorToast("邮箱验证码不能为空");
            return false;
        }

        return checkPwd(password, confirmPwd);
    }


    public static boolean checkOldNewConfirmPwd(String newPwd, String confirmPwd, String verifyCode) {
        if (ObjectUtils.isEmpty(verifyCode)) {
            ToastUtil.showErrorToast("验证码不能为空");
            return  false;
        }



        if (ObjectUtils.isEmpty(newPwd)) {
            ToastUtil.showErrorToast("新密码不能为空");
            return  false;
        }


        if (ObjectUtils.isEmpty(confirmPwd)) {
            ToastUtil.showErrorToast("确认密码不能为空");
            return  false;
        }


        if (newPwd.length() < 6) {
            ToastUtil.showErrorToast("密码长度不能小于6位");
            return false;
        }

        if (!newPwd.equals(confirmPwd)) {
            ToastUtil.showErrorToast("两次密码输入不一致");
            return false;
        }

        return true;
    }


    public static boolean checkPwd(String password, String confirmPwd) {
        if (ObjectUtils.isEmpty(password)) {
            ToastUtil.showErrorToast("新密码不能为空");
            return  false;
        }


        if (ObjectUtils.isEmpty(confirmPwd)) {
            ToastUtil.showErrorToast("确认密码不能为空");
            return  false;
        }


        if (password.length() < 6) {
            ToastUtil.showErrorToast("密码长度不能小于6位");
            return false;
        }

        if (!password.equals(confirmPwd)) {
            ToastUtil.showErrorToast("两次密码输入不一致");
            return false;
        }

        return true;
    }

    public static boolean emailCodePwdConfirm(String email, String verifyCode, String password,
                                              String confirmPwd) {
        if (ObjectUtils.isEmpty(email)) {
            ToastUtil.showErrorToast("邮箱不能为空");
            return false;
        }

        if (!RegexUtils.isEmail(email)) {
            ToastUtil.showErrorToast("邮箱格式不正确");
            return  false;
        }

        if (ObjectUtils.isEmpty(password)) {
            ToastUtil.showErrorToast("新密码不能为空");
            return  false;
        }

        if (password.length() < 6) {
            ToastUtil.showErrorToast("密码长度不能小于6位");
            return false;
        }

        if (!password.equals(confirmPwd)) {
            ToastUtil.showErrorToast("两次密码输入不一致");
            return false;
        }

        if (ObjectUtils.isEmpty(verifyCode)) {
            ToastUtil.showErrorToast("邮箱验证码不能为空");
            return  false;
        }



        return true;
    }

    public static boolean emailPwdCode(String email, String password, String verifyCode) {
        if (ObjectUtils.isEmpty(email)) {
            ToastUtil.showErrorToast("邮箱不能为空");
            return false;
        }

        if (ObjectUtils.isEmpty(verifyCode)) {
            ToastUtil.showErrorToast("邮箱验证码不能为空");
            return  false;
        }


        if (ObjectUtils.isEmpty(password)) {
            ToastUtil.showErrorToast("新密码不能为空");
            return  false;
        }




        if (!RegexUtils.isEmail(email)) {
            ToastUtil.showErrorToast("邮箱格式不正确");
            return  false;
        }

        if (password.length() < 6) {
            ToastUtil.showErrorToast("密码长度不能小于6位");
            return false;
        }

        return true;
    }


    public static boolean checkEmailPwd(String email, String password, String confirmPwd) {

        if (ObjectUtils.isEmpty(email)) {
            ToastUtil.showErrorToast("邮箱不能为空");
            return false;
        }

        if (ObjectUtils.isEmpty(password)) {
            ToastUtil.showErrorToast("新密码不能为空");
            return  false;
        }


        if (ObjectUtils.isEmpty(confirmPwd)) {
            ToastUtil.showErrorToast("确认密码不能为空");
            return  false;
        }


        if (!RegexUtils.isEmail(email)) {
            ToastUtil.showErrorToast("邮箱格式不正确");
            return  false;
        }

        if (password.length() < 6) {
            ToastUtil.showErrorToast("密码长度不能小于6位");
            return false;
        }

        if (!password.equals(confirmPwd)) {
            ToastUtil.showErrorToast("两次密码输入不一致");
            return false;
        }



        return true;
    }
}
