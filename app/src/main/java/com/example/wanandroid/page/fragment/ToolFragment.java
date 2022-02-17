package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/14
 */
public class ToolFragment extends Fragment implements View.OnClickListener {
    private ArrayList<HomeArticleData> mData = new ArrayList<>();
    private static final String TAG = "ToolFragment";
    private Activity mActivity;
    private View mRootView;
    private Button mBtnWeather;
    private Button mBtnHistory;
    private Button mBtnTophub;
    private Button mBtnOneArticle;
    private Button mBtnZhiHu;
    private Button mBtnCovid;

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
        mBtnHistory = mRootView.findViewById(R.id.fragment_tool_btn_history);
        mBtnHistory.setOnClickListener(this::onClick);
        mBtnTophub = mRootView.findViewById(R.id.fragment_tool_btn_hotarticle);
        mBtnTophub.setOnClickListener(this::onClick);
        mBtnOneArticle = mRootView.findViewById(R.id.fragment_tool_btn_onearticle);
        mBtnOneArticle.setOnClickListener(this::onClick);
        mBtnZhiHu = mRootView.findViewById(R.id.fragment_tool_btn_zhihu);
        mBtnZhiHu.setOnClickListener(this::onClick);
        mBtnCovid = mRootView.findViewById(R.id.fragment_tool_btn_COVID);
        mBtnCovid.setOnClickListener(this::onClick);
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_tool_btn_weather:
                WeatherFragment weatherFragment = new WeatherFragment();
                FragmentManager fm1 = getParentFragmentManager();
                FragmentTransaction transaction1 = fm1.beginTransaction();
                transaction1.addToBackStack(null);
                transaction1.replace(R.id.fragment_tool_fl, weatherFragment);
                transaction1.commit();
                break;
            case R.id.fragment_tool_btn_history:
                HistoryFragment historyFragment = new HistoryFragment();
                FragmentManager fm2 = getParentFragmentManager();
                FragmentTransaction transaction2 = fm2.beginTransaction();
                transaction2.addToBackStack(null);
                transaction2.replace(R.id.fragment_tool_fl, historyFragment);
                transaction2.commit();
                break;
            case R.id.fragment_tool_btn_hotarticle:
                TopHubFragment topHubFragment = new TopHubFragment();
                FragmentManager fm3 = getParentFragmentManager();
                FragmentTransaction transaction3 = fm3.beginTransaction();
                transaction3.addToBackStack(null);
                transaction3.replace(R.id.fragment_tool_fl, topHubFragment);
                transaction3.commit();
                break;
            case R.id.fragment_tool_btn_onearticle:
                OneArticleFragment oneArticleFragment = new OneArticleFragment();
                FragmentManager fm4 = getParentFragmentManager();
                FragmentTransaction transaction4 = fm4.beginTransaction();
                transaction4.addToBackStack(null);
                transaction4.replace(R.id.fragment_tool_fl, oneArticleFragment);
                transaction4.commit();
                break;
            case R.id.fragment_tool_btn_zhihu:
                ZhihuFragment zhihuFragment = new ZhihuFragment();
                FragmentManager fm5 = getParentFragmentManager();
                FragmentTransaction transaction5 = fm5.beginTransaction();
                transaction5.addToBackStack(null);
                transaction5.replace(R.id.fragment_tool_fl, zhihuFragment);
                transaction5.commit();
                break;
            case R.id.fragment_tool_btn_COVID:
                COVIDFragment covidFragment = new COVIDFragment();
                FragmentManager fm6 = getParentFragmentManager();
                FragmentTransaction transaction6 = fm6.beginTransaction();
                transaction6.addToBackStack(null);
                transaction6.replace(R.id.fragment_tool_fl, covidFragment);
                transaction6.commit();
                break;

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
//        FragmentManager fm = getChildFragmentManager();
//        fm.popBackStack();
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
