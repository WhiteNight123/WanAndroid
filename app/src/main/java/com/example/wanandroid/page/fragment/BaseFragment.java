package com.example.wanandroid.page.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.widget.ContentPage;


/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public ContentPage contentPage;
    public ProgressDialog pdLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 初始化pdLoading
         */
        pdLoading = new ProgressDialog(getActivity());
        pdLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdLoading.setMessage("请稍后");
        pdLoading.setCanceledOnTouchOutside(false);
        pdLoading.setCancelable(true);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //正常显示
    protected abstract View getSuccessView();

    //网络请求
    protected abstract Object requestData();

    //刷新View
    public void refreshPage(Object o) {
        contentPage.refreshPage(o);
    }
}
