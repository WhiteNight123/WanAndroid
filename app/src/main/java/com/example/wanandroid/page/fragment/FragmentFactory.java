package com.example.wanandroid.page.fragment;

import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;

/**
 * 根据资源id返回fragment
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/24
 */
public class FragmentFactory {
    public static Fragment createById(int resId) {
        Fragment fragment = null;
        switch (resId) {
            case R.id.fragment_searchresult_cv:
                fragment = new SearchResultFragment();
                break;
        }
        return fragment;
    }
}
