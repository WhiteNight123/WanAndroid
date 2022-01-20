package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/18
 */
public class WebViewFragment extends Fragment {
    private static final String TAG = "q";
    private Activity mActivity;
    private FloatingActionButton button;

    private String url;
    private View rootView;
    private WebView webView;

    public WebViewFragment(String url) {
        this.url = url;
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
        rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = rootView.findViewById(R.id.webview);
        button=rootView.findViewById(R.id.fragment_webview_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        Log.e(TAG, "onCreateView: ");
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
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
