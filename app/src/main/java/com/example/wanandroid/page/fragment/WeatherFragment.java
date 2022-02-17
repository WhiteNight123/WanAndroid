package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.page.adapter.WeatherRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/15
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";
    private ArrayList<HomeArticleData> mHourData=new ArrayList<>();
    private Activity mActivity;
    private View mRootView;
    private RecyclerView mHourRecycleView;
    private TextView mTvCity;
    private TextView mTVTemperature;
    private TextView mTVMin;
    private TextView mTVMax;
    private TextView mTVWeather;
    private WeatherRecycleAdapter mAdapter;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 13:
                    jsonDecode(msg.obj.toString());
                    mAdapter.notifyDataSetChanged();
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
        mRootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mTvCity = mRootView.findViewById(R.id.fragment_weather_tv_city);
        mTVTemperature = mRootView.findViewById(R.id.fragment_weather_tv_temp);
        mTVMin = mRootView.findViewById(R.id.fragment_weather_tv_min);
        mTVMax = mRootView.findViewById(R.id.fragment_weather_tv_max);
        mTVWeather = mRootView.findViewById(R.id.fragment_weather_tv_weather);
        mHourRecycleView = mRootView.findViewById(R.id.fragment_weather_rv_hour);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHourRecycleView.setLayoutManager(linearLayoutManager);
        mAdapter = new WeatherRecycleAdapter(mHourData);
        mHourRecycleView.setAdapter(mAdapter);
        initData();
        return mRootView;
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://v2.alapi.cn/api/tianqi?token=dRW8QdwxVa5L4RCr", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 13;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络遇到错误了", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void jsonDecode(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            mTvCity.setText(jsonObject1.getString("city"));
            mTVTemperature.setText(jsonObject1.getString("temp") + "°C");
            mTVWeather.setText(jsonObject1.getString("weather"));
            mTVMin.setText("最高:" + jsonObject1.getString("min_temp") + "°C");
            mTVMax.setText("最低:" + jsonObject1.getString("max_temp") + "°C");
            JSONArray jsonArray = jsonObject1.getJSONArray("hour");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                HomeArticleData data = new HomeArticleData();
                data.setTime(jsonObject2.getString("time").substring(11,16));
                data.setTitle(jsonObject2.getString("wea"));
                data.setContent(jsonObject2.getString("temp") + "°C");
                mHourData.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        onDestroy();
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
