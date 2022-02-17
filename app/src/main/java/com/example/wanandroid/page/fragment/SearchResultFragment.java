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
import android.widget.Toast;

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
import com.example.wanandroid.page.adapter.HomeArticleRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/8
 */
public class SearchResultFragment extends Fragment {
    private static final String TAG = "SearchResultFragment: ";
    private ArrayList<HomeArticleData> mSearchData = new ArrayList<>();
    private WebViewFragment mWebViewFragment;
    private Activity mActivity;
    private HashMap<String, String> hashMap;
    private View mRootView;
    private RecyclerView mRecycleView;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 4:
                    jsonDecodeRV(msg.obj.toString());
                    mSearchRecycleAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });
    private HomeArticleRecycleAdapter mSearchRecycleAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        initData();

        Bundle bundle = getArguments();
        String search = bundle.getString("key");
        hashMap = new HashMap<>();
        hashMap.put("k", search);

        mRecycleView = mRootView.findViewById(R.id.fragment_searchresult_rv);
        mSearchRecycleAdapter = new HomeArticleRecycleAdapter(mSearchData);
        mRecycleView.setAdapter(mSearchRecycleAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycleView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mSearchRecycleAdapter.setOnHomeRecycleViewListener(new HomeArticleRecycleAdapter.HomeRecycleViewListener() {
            @Override
            public void onHomeRecycleViewClick(View view, Object data, int position) {
                mWebViewFragment = (WebViewFragment) getParentFragmentManager().findFragmentById(R.id.fragment_main);
                replaceFragment(mWebViewFragment, data.toString());
            }
        });
        return mRootView;
    }

    public void initData() {
        Bundle bundle = getArguments();
        String search = bundle.getString("key");
        hashMap = new HashMap<>();
        hashMap.put("k", search);
        Log.d(TAG, "onCreateView: " + hashMap.toString());

        NetUtil.sendHttpRequest("https://www.wanandroid.com/article/query/0/json", "POST", hashMap, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d(TAG, "onFinish: " + response);
                Message msg = Message.obtain();
                msg.what = 4;
                msg.obj = response;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络遇到错误了", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d(TAG, "onError: " + e.toString());
            }
        });
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

    private void replaceFragment(Fragment childFragment, String url) {
        if (childFragment == null) {
            childFragment = new WebViewFragment(url);
            FragmentManager fm = getParentFragment().getParentFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_main, childFragment);
            transaction.commit();
        }
    }

    private void jsonDecodeRV(String jsonData) {
        mSearchData.clear();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String author = data.getString("author");
                if ("".equals(author)) {
                    author = data.getString("shareUser");
                }
                String time = data.getString("niceDate");
                if (time.length() > 10) {
                    time = time.substring(0, 10);
                }
                String title = data.getString("title");
                String htmlRegex = "<[^>]+>";
                String spaceRegex = "\\s*|\t|\r|\n";
                //title = title.replaceAll(htmlRegex, ""); //去除html标签
                //title = content.replaceAll(spaceRegex, ""); //过滤空格
                String chapterName = data.getString("superChapterName");
                String url = data.getString("link");
                HomeArticleData homeArticleData = new HomeArticleData();
                homeArticleData.setAuthor(author);
                homeArticleData.setTitle(title);
                homeArticleData.setTime(time);
                homeArticleData.setUrl(url);
                homeArticleData.setChapterName(chapterName);
                mSearchData.add(homeArticleData);
                Log.d(TAG, "jsonDeco2" + mSearchData.toString());
            }
            Log.d(TAG, "jsonDeco2" + mSearchData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
