package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.page.adapter.ZhihuRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/15
 */
public class ZhihuFragment extends Fragment {
    private static final String TAG = "ZhihuFragment";
    private ArrayList<HomeArticleData> mData = new ArrayList<>();
    private Activity mActivity;
    private View mRootView;
    private RecyclerView mRecycleView;
    private ZhihuRecycleAdapter mAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 17:
                    Log.e(TAG, "handleMessage: " + msg.obj.toString());
                    jsonDecode(msg.obj.toString());
                    Log.e(TAG, "handleMessage: " + mData.toString());

                    mAdapter.notifyDataSetChanged();
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
        mRootView = inflater.inflate(R.layout.fragment_zhihu, container, false);
        initData();
        mRecycleView = mRootView.findViewById(R.id.fragment_zhihu_rv);
        mAdapter = new ZhihuRecycleAdapter(getActivity().getApplicationContext(), mData);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnZhihuRecycleViewListener(new ZhihuRecycleAdapter.ZhihuRecycleViewListener() {
            @Override
            public void onZhihuRecycleViewClick(View view, Object data, int position) {
                WebViewFragment webViewFragment = new WebViewFragment(data.toString());
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_zhihu, webViewFragment);
                transaction.commit();
            }
        });

        return mRootView;
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://v2.alapi.cn/api/zhihu?token=dRW8QdwxVa5L4RCr", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 17;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void jsonDecode(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("top_stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                HomeArticleData data = new HomeArticleData();
                data.setAuthor(jsonObject2.getString("hint"));
                data.setTitle(jsonObject2.getString("title"));
                data.setUrl(jsonObject2.getString("url"));
                data.setTime(jsonObject2.getString("image"));
                mData.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
        Log.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
    }
}
