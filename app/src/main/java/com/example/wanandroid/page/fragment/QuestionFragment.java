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
import com.example.wanandroid.page.adapter.HomeArticleRecycleAdapter;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/14
 */
public class QuestionFragment extends Fragment {
    private static final String TAG = "QuestionFragment";
    private Activity mActivity;
    private View mRootView;
    private RecyclerView mRecycleView;
    private ArrayList<HomeArticleData> mQuestionData = new ArrayList<>();
    private HomeArticleRecycleAdapter mAdapter;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e(TAG, "Handler2" + msg.obj.toString());
                    jsonDecodeRV(msg.obj.toString());
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
        mRootView = inflater.inflate(R.layout.fragment_question, container, false);
        initData();
        mRecycleView = mRootView.findViewById(R.id.fragment_question_rv);
        mAdapter = new HomeArticleRecycleAdapter(mQuestionData);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnHomeRecycleViewListener(new HomeArticleRecycleAdapter.HomeRecycleViewListener() {
            @Override
            public void onHomeRecycleViewClick(View view, Object data, int position) {
                WebViewFragment webViewFragment = new WebViewFragment(data.toString());
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.addToBackStack(null);
                transaction.add(R.id.fragment_main, webViewFragment);
                transaction.commit();
                Log.d(TAG, "onHomeRecycleViewClick:111 " + webViewFragment);
            }
        });


        return mRootView;
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://wanandroid.com/wenda/list/1/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 10;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void jsonDecodeRV(String jsonData) {
        mQuestionData.clear();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String author = data.getString("author");
                String time = data.getString("niceDate");
                if (time.length() > 10) {
                    time = time.substring(0, 10);
                }
                String title = data.getString("title");
                String content = data.getString("desc");
                String htmlRegex = "<[^>]+>";
                String spaceRegex = "\\s*|\t|\r|\n";
                content = content.replaceAll(htmlRegex, ""); //去除html标签
                content = content.replaceAll(spaceRegex, ""); //过滤空格
                String chapterName = data.getString("superChapterName");
                String url = data.getString("link");
                HomeArticleData homeArticleData = new HomeArticleData();
                homeArticleData.setAuthor(author);
                homeArticleData.setTitle(title);
                homeArticleData.setTime(time);
                homeArticleData.setUrl(url);
                homeArticleData.setContent(content);
                homeArticleData.setChapterName(chapterName);
                mQuestionData.add(homeArticleData);
            }
            Log.d(TAG, "jsonDeco2" + mQuestionData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
