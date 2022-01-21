package com.example.wanandroid.page.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import java.util.ArrayList;

/**
 * 管理Fragment
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/15
 */
public class FragmentPaperAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragments;

    public FragmentPaperAdapter(FragmentActivity fragmentActivity, ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}

