package com.example.wanandroid.page.adapter;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * ViewPager2 滑动同步 BottomNavigationView
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/16
 */
public class HomePageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        setToolBarTitle(position);
    }

    private void setToolBarTitle(int currentTitleIndex) {
        mToolbar.setTitle(mBottomNavigationView.getMenu().getItem(currentTitleIndex).getTitle());
    }

    public HomePageChangeCallback(BottomNavigationView bottomNavigationView, Toolbar toolbar) {
        this.mBottomNavigationView = bottomNavigationView;
        this.mToolbar = toolbar;
    }
}
