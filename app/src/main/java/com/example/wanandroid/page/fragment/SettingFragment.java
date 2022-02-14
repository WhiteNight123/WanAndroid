package com.example.wanandroid.page.fragment;

import static com.example.wanandroid.page.activity.MainActivity.mMyFragment;
import static com.example.wanandroid.page.fragment.MyFragment.LOGIN_STATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;

import java.lang.reflect.InvocationTargetException;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/13
 */
public class SettingFragment extends Fragment {
    private Activity mActivity;
    private static final String TAG = "SettingFragment";
    private TextView mTVLogout;
    private SharedPreferences mPref;
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
        View mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
        Toolbar toolbar = mRootView.findViewById(R.id.fragment_setting_toolbar);
        mTVLogout = mRootView.findViewById(R.id.fragment_setting_tv_logout);
        mPref = mActivity.getSharedPreferences("login", Context.MODE_PRIVATE);
        mTVLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGIN_STATE = 0;
                Toast.makeText(mActivity, "退出登录成功", Toast.LENGTH_SHORT).show();
                mPref.edit().clear().apply();
                try {
                    Class.forName("com.example.wanandroid.page.fragment.MyFragment").getMethod("deleteData").invoke(mMyFragment);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        if (LOGIN_STATE == 1) {
            mTVLogout.setText("退出登录");

        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        toolbar.setTitle("设置");
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }
}
