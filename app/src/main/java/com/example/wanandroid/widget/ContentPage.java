package com.example.wanandroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wanandroid.R;
import com.example.wanandroid.utils.ConstantUtils;

/**
 * 根据state显示View
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public abstract class ContentPage extends FrameLayout {
    public ContentPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContentPage();
    }

    public ContentPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initContentPage();
    }

    public ContentPage(@NonNull Context context) {
        super(context);
        initContentPage();
    }

    //定义加载状态
    enum PageState {
        STATE_LOADING(0),
        STATE_SUCCESS(1),
        STATE_ERROR(2);
        private int value;

        PageState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    //mState表示当前的vlt
    private PageState mState = PageState.STATE_LOADING;
    private View loadingView;
    private View errorView;
    private View successView;

    /**
     * 初始化ContentPage
     */
    private void initContentPage() {
        //显示默认的View
        showPage();
        //请求服务器的数据
        loadDataAndRefreshPage();
    }

    public void loadDataAndRefreshPage() {
        Object data = loadData();
        mState = checkDara(data);
        showPage();
    }

    /**
     * 请求数据,刷新Page
     *
     * @param o
     */
    public void refreshPage(Object o) {
        if (o == null) {
            //没有数据,为error
            mState = PageState.STATE_ERROR;
        } else {
            mState = PageState.STATE_SUCCESS;
        }
        showPage();
    }

    /**
     * 检查数据对应的state
     */
    private PageState checkDara(Object data) {
        if (data == null) {
            //没有数据,为error
            return PageState.STATE_ERROR;
        } else if (data == ConstantUtils.STATE_LOADING) {
            return PageState.STATE_LOADING;
        } else {
            return PageState.STATE_SUCCESS;
        }
    }

    /**
     * 根据state显示View
     */
    private void showPage() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        switch (mState.getValue()) {
            //加载中
            case 0:
                if (loadingView == null) {
                    loadingView = View.inflate(getContext(), R.layout.content_page_error, null);
                }
                removeAllViews();
                addView(loadingView, params);
                break;
            //加载成功
            case 1:
                removeAllViews();
                successView = createSuccessView();
                addView(successView, params);
                break;
            //加载失败
            case 2:
                if (errorView == null) {
                    errorView = View.inflate(getContext(), R.layout.content_page_error, null);
                    Button button = (Button) errorView.findViewById(R.id.page_error_btn);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //先显示loadingView
                            mState = PageState.STATE_LOADING;
                            showPage();
                            //重新请求数据
                            loadDataAndRefreshPage();
                        }
                    });
                }
                removeAllViews();
                addView(errorView, params);
                break;
        }
    }

    /**
     * 响应成功界面,由子界面自己实现
     */
    protected abstract View createSuccessView();

    /**
     * 界面的请求和解析由子界面自己实现
     */
    protected abstract Object loadData();
}
