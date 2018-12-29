package com.read.chajian.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class TestUtils {

    private static final String TAG = TestUtils.class.getSimpleName();

    public static void fixIp() {
        OkGo.<String>post("https://www.mddns.net/hb")
                .tag(TAG)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        LogUtils.e(response.body());
                    }
                });
    }
}
