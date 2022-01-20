package com.example.wanandroid.page.adapter;


import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/16
 */
public class MyPageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        setToolBarTitle(position);
    }

    private void setToolBarTitle(int currentTitleIndex) {
        toolbar.setTitle(bottomNavigationView.getMenu().getItem(currentTitleIndex).getTitle());
    }

    public MyPageChangeCallback(BottomNavigationView bottomNavigationView, Toolbar toolbar) {
        this.bottomNavigationView = bottomNavigationView;
        this.toolbar = toolbar;
    }
}
