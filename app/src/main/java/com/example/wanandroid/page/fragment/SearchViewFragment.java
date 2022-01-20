package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;

import java.util.Objects;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/20
 */
public class SearchViewFragment extends Fragment {
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.fragment_toolbar_searchview);
        //toolbar.setNavigationIcon(R.drawable.ic_arrowleft);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true); //返回


        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.fragment_menu_search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu_search, menu);
        MenuItem item = menu.findItem(R.id.fragment_search_searchview);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入搜索内容");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
