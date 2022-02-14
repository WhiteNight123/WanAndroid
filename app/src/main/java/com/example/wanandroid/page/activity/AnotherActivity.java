package com.example.wanandroid.page.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wanandroid.R;
import com.example.wanandroid.page.fragment.FragmentFactory;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public class AnotherActivity extends AppCompatActivity {
    private static final String TAG = "hahaha";
    FragmentManager fm;
    public Intent intent;
    public FragmentTransaction ft;
    public int resId;
    public String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_another);
        //获取传递过来的资源id值
        intent = getIntent();
        resId = intent.getIntExtra("resId", 0);
        if (intent.getExtras() != null) {
            resId = intent.getExtras().getInt("resId");
        }
        //这里可以传其他的值
        //id = intent.getStringExtra("id");

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        Log.e(TAG, "onCreate: "+resId );
        ft.replace(R.id.fl_another_activity, FragmentFactory.createById(resId));
        ft.commit();


    }
}
