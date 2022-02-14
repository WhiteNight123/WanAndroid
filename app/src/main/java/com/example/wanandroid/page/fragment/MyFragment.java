package com.example.wanandroid.page.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wanandroid.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/10
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MyFragment";
    public static int LOGIN_STATE = 0;
    private int errorCode;
    private String errorMsg;
    private String name;
    private String email;
    private Activity mActivity;
    private ImageView mImageView;
    private TextView mTVName;
    private TextView mTVEmail;
    private TextView mTVRank;
    private TextView mTVLevel;
    private TextView mTVPoints;
    private TextView mTVAboutAuthor;
    private TextView mTVSetting;
    private View mRootView;
    private SharedPreferences mPref;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.e(TAG, "handleMessage: " + errorCode);
            switch (msg.what) {
                case 6:
                    if (errorCode == -1001) {
                        //Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
                        mPref.edit().remove("cookie").apply();
                        deleteData();

                    } else if (errorCode == 0) {
                        //Toast.makeText(mActivity, "自动登录成功", Toast.LENGTH_SHORT).show();
                        LOGIN_STATE = 1;
                        jsonDecodeData(msg.obj.toString());
                    }
                    break;
            }
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
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);
        initView();
        initData();

        mTVName.setOnClickListener(this::onClick);
        mTVAboutAuthor.setOnClickListener(this::onClick);
        mTVPoints.setOnClickListener(this::onClick);
        mTVSetting.setOnClickListener(this::onClick);
        return mRootView;
    }

    private void initView() {
        mImageView = mRootView.findViewById(R.id.fragment_my_iv_portrait);
        mTVName = mRootView.findViewById(R.id.fragment_my_tv_username);
        mTVEmail = mRootView.findViewById(R.id.fragment_my_tv_mail);
        mTVRank = mRootView.findViewById(R.id.fragment_my_tv_rank);
        mTVLevel = mRootView.findViewById(R.id.fragment_my_tv_level);
        mTVPoints = mRootView.findViewById(R.id.fragment_my_tv_ponits);
        mTVAboutAuthor = mRootView.findViewById(R.id.fragment_my_tv_about);
        mTVSetting = mRootView.findViewById(R.id.fragment_my_tv_setting);
        mPref = mActivity.getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void initData() {

        if (!mPref.getString("cookie", "").equals("")) {
            //System.out.println("免登录 " + pref.getString("cookie", ""));
            Log.e(TAG, "initData: " + mPref.getString("cookie", ""));
            autoLogin("https://wanandroid.com//user/lg/userinfo/json");
        }
    }

    public void deleteData() {
        mTVName.setText("未登录");
        mTVLevel.setText("等级");
        mTVEmail.setText("");
        mTVRank.setText("排名");
    }

    private void autoLogin(String mUrl) {
        Log.e(TAG, "autoLogin: " + mPref.getString("cookie", ""));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("cookie", mPref.getString("cookie", ""));

                    connection.setConnectTimeout(8000);
                    InputStream in = connection.getInputStream();
                    String responseData = StreamToString(in);
                    Message message = Message.obtain();
                    message.what = 6;
                    message.obj = responseData;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String StreamToString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        String oneLine;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            while ((oneLine = reader.readLine()) != null) {
                sb.append(oneLine).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void jsonDecodeData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            errorCode = jsonObject.getInt("errorCode");
            Log.e(TAG, "jsonDecodeData: " + errorCode);
            errorMsg = jsonObject.getString("errorMsg");
            Log.e(TAG, "jsonDecodeData: " + jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("userInfo");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("coinInfo");
            mTVName.setText(jsonObject2.getString("nickname"));
            mTVEmail.setText(jsonObject2.getString("email"));
            mTVLevel.setText("等级:" + jsonObject3.getString("level"));
            mTVRank.setText("排名:" + jsonObject3.getString("rank"));
            errorCode = jsonObject.getInt("errorCode");
            errorMsg = jsonObject.getString("errorMsg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_my_tv_username:
                if (LOGIN_STATE == 0) {
                    FragmentManager fm1 = getParentFragmentManager();
                    FragmentTransaction transaction1 = fm1.beginTransaction();
                    transaction1.addToBackStack(null);
                    LoginFragment fragment = new LoginFragment();
                    transaction1.add(R.id.fragment_main, fragment);
                    transaction1.show(fragment);
                    transaction1.hide(this);
                    transaction1.commit();
                }
                break;
            case R.id.fragment_my_tv_setting:
                FragmentManager fm4 = getParentFragmentManager();
                FragmentTransaction transaction4 = fm4.beginTransaction();
                transaction4.addToBackStack(null);
                SettingFragment settingFragment = new SettingFragment();
                transaction4.add(R.id.fragment_main, settingFragment);
                transaction4.show(settingFragment);
                transaction4.hide(this);
                transaction4.commit();
                break;
            case R.id.fragment_my_tv_about:
                FragmentManager fm2 = getParentFragmentManager();
                FragmentTransaction transaction2 = fm2.beginTransaction();
                transaction2.addToBackStack(null);
                AboutFragment aboutFragment = new AboutFragment();
                transaction2.add(R.id.fragment_main, aboutFragment);
                transaction2.commit();
                break;
            case R.id.fragment_my_tv_ponits:
                FragmentManager fm3 = getParentFragmentManager();
                FragmentTransaction transaction3 = fm3.beginTransaction();
                transaction3.addToBackStack(null);
                MyPointFragment myPointFragment = new MyPointFragment();
                transaction3.add(R.id.fragment_main, myPointFragment);
                transaction3.commit();
                break;

            default:
                break;
        }
    }


    @Override

    public void onHiddenChanged(boolean hidden) {

        if (hidden) {
            //相当于Fragment的onPause
            //initData();
        } else {
            // 相当于Fragment的onResume
            initData();
        }
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
