package com.example.wanandroid.page.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/15
 */
public class COVIDFragment extends Fragment {
    private static final String TAG = "COVIDFragment";
    private Activity mActivity;
    private ArrayList<HomeArticleData> mData = new ArrayList<>();
    private View mRootView;

    private TextView mTvCity;
    private TextView mTvRisk;
    private TextView mTvInDesc;
    private TextView mTvOutDesc;
    private TextView mTvHealthyCode;
    private EditText mEditText;
    private Button mButton;
    private ImageView mIvHealthyCode;
    private SharedPreferences mPref;
    private LinearProgressIndicator mProgress;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 18:
                    jsonDecode1(msg.obj.toString());
                    break;
                case 19:
                    Log.e(TAG, "handleMessage: " + msg.obj.toString());
                    mProgress.setVisibility(View.INVISIBLE);
                    jsonDecode2(msg.obj.toString());
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
        mRootView = inflater.inflate(R.layout.fragment_covid, container, false);
        mPref = mActivity.getSharedPreferences("covid", Context.MODE_PRIVATE);
        mProgress = mRootView.findViewById(R.id.fragment_covid_pb);
        if (mPref.getString("合肥", "") == "") {
            initData();
        }
        mTvCity = mRootView.findViewById(R.id.fragment_covid_tv_city);
        mTvRisk = mRootView.findViewById(R.id.fragment_covid_tv_risk_level);
        mTvInDesc = mRootView.findViewById(R.id.fragment_covid_tv_in_desc);
        mTvOutDesc = mRootView.findViewById(R.id.fragment_covid_tv_out_desc);
        mTvHealthyCode = mRootView.findViewById(R.id.fragment_covid_tv_health_code_name);
        mIvHealthyCode = mRootView.findViewById(R.id.fragment_covid_tv_health_code_picture);
        mEditText = mRootView.findViewById(R.id.fragment_covid_et);
        mButton = mRootView.findViewById(R.id.fragment_covid_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPref.getString(mEditText.getText().toString(), "") == null) {
                    Toast.makeText(mActivity, "城市名称不合法", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setVisibility(View.VISIBLE);
                    String url = "https://v2.alapi.cn/api/springTravel/query?token=dRW8QdwxVa5L4RCr&from=10017&to=" + mPref.getString(mEditText.getText().toString(), "");
                    Log.e(TAG, "onClick: " + url);
                    NetUtil.sendHttpRequest(url, "GET", null, new NetCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message = Message.obtain();
                            message.what = 19;
                            message.obj = response;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                            mRootView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                            e.printStackTrace();
                        }
                    });
                }
                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
                View view = mActivity.getWindow().peekDecorView();
                if (null != view) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
        });

        return mRootView;
    }


    private void initData() {
        NetUtil.sendHttpRequest("https://v2.alapi.cn/api/springTravel/city?token=dRW8QdwxVa5L4RCr", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 18;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void jsonDecode1(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("cities");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    mPref.edit().putString(jsonObject2.getString("city"), jsonObject2.getString("city_id")).apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonDecode2(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("to_info");
            mTvCity.setText(jsonObject2.getString("city_name"));
            switch (jsonObject2.getString("risk_level")) {
                case "1":
                    mTvRisk.setText("低风险");
                    mTvRisk.setTextColor(Color.GREEN);
                    break;
                case "2":
                    mTvRisk.setText("中风险");
                    mTvRisk.setTextColor(Color.YELLOW);
                    break;
                case "3":
                    mTvRisk.setText("高风险");
                    mTvRisk.setTextColor(Color.RED);
                case "4":
                    mTvRisk.setText("部分地区中风险");
                    mTvRisk.setTextColor(0xFF0000);
                    break;
                case "5":
                    mTvRisk.setText("部分地区高风险");
                    mTvRisk.setTextColor(0xFF0000);
                    break;
                case "6":
                    mTvRisk.setText("部分地区中,高风险");
                    mTvRisk.setTextColor(0xFF0000);
                    break;
                default:
                    mTvRisk.setText("");
                    break;

            }
            mTvInDesc.setText("进入政策\n" + jsonObject2.getString("low_in_desc"));
            mTvOutDesc.setText("出行政策\n" + jsonObject2.getString("out_desc"));
            mTvHealthyCode.setText(jsonObject2.getString("health_code_name"));
            Glide.with(mActivity).load(jsonObject2.getString("health_code_picture")).into(mIvHealthyCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
