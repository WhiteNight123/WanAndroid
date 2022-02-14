package com.example.wanandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wanandroid.WanAndroidApplication;
import com.example.wanandroid.page.activity.AnotherActivity;

/**
 * 实现跳转界面
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public class StartUtils {
    private static final String TAG = "StartUtils: ";

    public static void startActivityById(Context context, int resId) {
        Intent intent = new Intent(context, AnotherActivity.class);
        intent.putExtra("resId", resId);
        Log.e(TAG, "startActivityById: "+resId );
        context.startActivity(intent);
    }
}
