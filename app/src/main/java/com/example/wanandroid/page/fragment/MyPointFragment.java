package com.example.wanandroid.page.fragment;

import static com.example.wanandroid.page.fragment.MyFragment.LOGIN_STATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.page.adapter.MyPointRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/13
 */
public class MyPointFragment extends Fragment {
    private static final String TAG = "MyPointFragment";
    private Activity mActivity;
    private ArrayList<HomeArticleData> mPointData = new ArrayList<>();
    private View mRootView;
    private Toolbar mToolbar;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private SharedPreferences mPref;
    private MyPointRecycleAdapter mRecycleAdapter;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 7:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String point = jsonObject1.getString("coinCount");
                        mTextView.setText(point);
                        Log.e(TAG, "handleMessage: " + point);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    jsonDecode(msg.obj.toString());
                    mRecycleAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage:" + mPointData.toString());
                    break;
            }
            return false;
        }
    });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my_point, container, false);
        mPref = mActivity.getSharedPreferences("login", Context.MODE_PRIVATE);

        if(LOGIN_STATE==0){
            Toast.makeText(mActivity,"请先登录",Toast.LENGTH_SHORT);
        }else {
            initData();
            mToolbar = mRootView.findViewById(R.id.fragment_mypoint_toolbar);
            mTextView = mRootView.findViewById(R.id.fragment_mypoint_tv);
            mRecyclerView = mRootView.findViewById(R.id.fragment_mypoint_rv);
            mRecycleAdapter = new MyPointRecycleAdapter(mPointData);
            mRecyclerView.setAdapter(mRecycleAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.fragment_menu_rank:
                        RankFragment rankFragment=new RankFragment();
                        FragmentManager fm = getParentFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.addToBackStack(null);
                        transaction.add(R.id.fragment_main, rankFragment);

                        transaction.commit();
                }

                return false;
            }
        });
        mToolbar.setTitle("我的积分");

        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_menu_rank,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com/lg/coin/userinfo/json", mPref.getString("cookie", ""), new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 7;
                message.obj = response;
                mHandler.sendMessage(message);
                Log.e(TAG, "onFinish: " + response);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: " + e.toString());

            }
        });
        NetUtil.sendHttpRequest("https://www.wanandroid.com//lg/coin/list/1/json", mPref.getString("cookie", ""), new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 8;
                message.obj = response;
                mHandler.sendMessage(message);
                Log.e(TAG, "onFinish: " + response);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: " + e.toString());

            }
        });
    }

    private void jsonDecode(String jsonData) {
        mPointData.clear();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String point = data.getString("coinCount");
                String content = data.getString("desc");

                HomeArticleData RecycleData = new HomeArticleData();
                RecycleData.setAuthor("+" + point); //积分
                RecycleData.setTitle(content.substring(20).replace(",", ""));
                RecycleData.setTime(content.substring(0, 19));
                mPointData.add(RecycleData);
            }
            Log.d(TAG, "jsonDeco2" + mPointData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
