package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.page.adapter.QuestionRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/14
 */
public class RankFragment extends Fragment {
    private ArrayList<HomeArticleData> mRankData = new ArrayList<>();
    private Activity mActivity;
    private View mRootView;
    private Toolbar mToolbar;
    private RecyclerView mRecycleView;
    private QuestionRecycleAdapter mAdapter;
    private LinearProgressIndicator mProgress;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 11:
                    jsonDecodeData(msg.obj.toString());
                    mProgress.setVisibility(View.INVISIBLE);

                    mAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });

    public RankFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_rank, container, false);
        mProgress=mRootView.findViewById(R.id.fragment_rank_pb);
        mProgress.setVisibility(View.VISIBLE);
        initData();
        mToolbar = mRootView.findViewById(R.id.fragment_rank_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        mToolbar.setTitle("排行榜");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
        mRecycleView = mRootView.findViewById(R.id.fragment_rank_rv);
        mAdapter = new QuestionRecycleAdapter(mRankData);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return mRootView;
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com/coin/rank/1/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 11;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                mRootView.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(mActivity, "网络遇到错误了", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void jsonDecodeData(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data1 = jsonArray.getJSONObject(i);
                HomeArticleData data = new HomeArticleData();
                data.setTitle(data1.getString("rank"));
                data.setAuthor(data1.getString("username"));
                data.setContent(data1.getString("coinCount"));
                mRankData.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
