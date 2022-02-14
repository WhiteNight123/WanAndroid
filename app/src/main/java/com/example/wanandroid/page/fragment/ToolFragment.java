package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/14
 */
public class ToolFragment extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private View mRootView;
    private Button mBtnWeather;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {


            return false;
        }
    });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tool, container, false);
        mBtnWeather = mRootView.findViewById(R.id.fragment_tool_btn_weather);
        mBtnWeather.setOnClickListener(this::onClick);

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_tool_btn_weather:
                break;
        }
    }
}
