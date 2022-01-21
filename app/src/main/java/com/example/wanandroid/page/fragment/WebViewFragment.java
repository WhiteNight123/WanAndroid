package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * WebView
 * 显示网页
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/18
 */
public class WebViewFragment extends Fragment {
    private static final String TAG = "WebViewFragment:";
    private Activity mActivity;
    private FloatingActionButton button;

    private String mUrl;
    private View mRootView;
    private WebView mWebView;

    public WebViewFragment(String url) {
        this.mUrl = url;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        //url = getArguments().getString("param_url");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_webview, container, false);
        mWebView = mRootView.findViewById(R.id.fragment_wb);
        button = mRootView.findViewById(R.id.fragment_webview_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUrl);
        Log.e(TAG, "onCreateView: ");
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
