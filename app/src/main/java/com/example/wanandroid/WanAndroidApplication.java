package com.example.wanandroid;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 获取全局Context和 Handler
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public class WanAndroidApplication extends Application {
    private static Context mContext;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
    }


    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }
}
