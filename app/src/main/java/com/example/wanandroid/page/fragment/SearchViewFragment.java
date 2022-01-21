package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.wanandroid.R;
import com.example.wanandroid.page.activity.MainActivity;
import com.example.wanandroid.page.adapter.HotSearchRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Toolbar+SearchView
 * 搜索功能
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/20
 */
public class SearchViewFragment extends Fragment {
    private static final String TAG = "SearchViewFragment:";
    private Activity mActivity;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchEditView;
    private ArrayList<String> mHotSearchData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private HotSearchRecycleAdapter mHotSearchRecycleAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 3:
                    jsonDecodeRVHotSearch(msg.obj.toString());
                    mHotSearchRecycleAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage: " + mHotSearchData);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public SearchViewFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.fragment_searchview_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //显示返回
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        toolbar.setTitle("搜索");
        //返回监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mSearchEditView.isShown()) {
                        //运用反射调用SearchView中的onCloseClicked()方法
                        //如果搜索框有文本输入，则清除文本内容，否则直接关闭搜索框。
                        Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                        method.setAccessible(true);
                        method.invoke(mSearchView);
                    } else {
                        requireActivity().onBackPressed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        initData();
        Log.d(TAG, "onCreateViewhahah: " + mHotSearchData);
        mRecyclerView = rootView.findViewById(R.id.fragment_hotsearch_rv);
        mHotSearchRecycleAdapter = new HotSearchRecycleAdapter(mHotSearchData);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHotSearchRecycleAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); //清除activity中的toolbar
        inflater.inflate(R.menu.fragment_menu_search, menu);
        MenuItem item = menu.findItem(R.id.fragment_search_searchview);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setIconified(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("请输入搜索内容");
        mSearchEditView = mSearchView.findViewById(R.id.search_src_text);
        //去掉搜索框的下划线
        LinearLayout search_plate = mSearchView.findViewById(R.id.search_plate);
        LinearLayout submit_area = mSearchView.findViewById(R.id.submit_area);
        search_plate.setBackground(null);
        submit_area.setBackground(null);

        //搜索框打开
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框关闭
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(mActivity, "close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //监听输入文本变化
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //文本提交
                Toast.makeText(mActivity, "submit", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索框发生变化

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void initData() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com//hotkey/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 3;
                message.obj = response;
                handler.sendMessage(message);
                Log.d(TAG, "onFinish: " + response);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void jsonDecodeRVHotSearch(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data1 = jsonArray.getJSONObject(i);
                mHotSearchData.add(data1.getString("name"));
            }
            Log.d(TAG, "JsonDecodeRV: " + mHotSearchData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
