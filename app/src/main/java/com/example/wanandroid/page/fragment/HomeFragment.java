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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wanandroid.bean.HomeArticleData;
import com.example.wanandroid.page.adapter.HomeArticleRecycleAdapter;
import com.example.wanandroid.page.adapter.HomeBannerPagerAdapter;
import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeBannerData;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页 ViewPager2+Recycle
 * 展示 Banner和热门文章
 *
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/15
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ArrayList<HomeBannerData> mBannerData = new ArrayList<>();
    private ArrayList<HomeArticleData> mArticleData = new ArrayList<>();
    private ViewPager2 mViewPager2;
    private WebViewFragment mWebViewFragment;
    private RecyclerView mRecyclerView;
    private Activity mActivity;
    private View mRootView;
    private HomeBannerPagerAdapter mBannerPagerAdapter;
    private HomeArticleRecycleAdapter mArticleRecycleAdapter;

    private boolean mFirstLoading = true;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    jsonDecodeVP2(msg.obj.toString());
                    mBannerPagerAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Handler1" + mBannerData.toString());

                    break;
                case 2:
                    jsonDecodeRV1(msg.obj.toString());
                    mArticleRecycleAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Handler2" + mArticleData.toString());
                    break;
                case 12:
                    jsonDecodeRV2(msg.obj.toString());
                    mArticleRecycleAdapter.notifyDataSetChanged();

            }
            return false;
        }
    });

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        initData();

        mViewPager2 = mRootView.findViewById(R.id.home_item_vp2);
        mBannerPagerAdapter = new HomeBannerPagerAdapter(mActivity.getApplicationContext(), mBannerData);
        mViewPager2.setAdapter(mBannerPagerAdapter);

        mRecyclerView = mRootView.findViewById(R.id.home_item_rv);
        mArticleRecycleAdapter = new HomeArticleRecycleAdapter(mArticleData);
        mRecyclerView.setAdapter(mArticleRecycleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mArticleRecycleAdapter.setOnHomeRecycleViewListener(new HomeArticleRecycleAdapter.HomeRecycleViewListener() {
            @Override
            public void onHomeRecycleViewClick(View view, Object data, int position) {
                mWebViewFragment = (WebViewFragment) getParentFragmentManager().findFragmentById(R.id.fragment_main);
                replaceFragment(mWebViewFragment, data.toString());
//                StartUtils.startActivityById(getActivity(),view.getId());
                Log.d(TAG, "onHomeRecycleViewClick:111 " + mWebViewFragment);
            }
        });
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstLoading) {
            //如果不是第一次加载，刷新数据
            mBannerPagerAdapter.notifyDataSetChanged();
            mArticleRecycleAdapter.notifyDataSetChanged();
        }
        mFirstLoading = false;
    }


    private static HomeFragment newInstance(String str) {
        HomeFragment frag = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param_url", str);
        frag.setArguments(bundle);
        return frag;
    }

    public void initData() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com/banner/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = response;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        NetUtil.sendHttpRequest("https://www.wanandroid.com/article/top/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = response;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: " + e);
            }
        });
        NetUtil.sendHttpRequest("https://www.wanandroid.com/article/list/0/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message=Message.obtain();
                message.what=12;
                message.obj=response;
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
e.printStackTrace();
            }
        });
    }


    private void jsonDecodeVP2(String jsonData) {
        mBannerData.clear(); //初始化数据
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String title = data.getString("title");
                String imagePath = data.getString("imagePath");
                HomeBannerData homeBannerData = new HomeBannerData();
                homeBannerData.setTitle(title);
                homeBannerData.setImageUrl(imagePath);
                mBannerData.add(homeBannerData);
            }
            Log.d(TAG, "jsonDeco1" + mBannerData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonDecodeRV1(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
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
                mArticleData.add(homeArticleData);
            }
            Log.d(TAG, "jsonDeco2" + mArticleData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jsonDecodeRV2(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String author = data.getString("author");
                String time = data.getString("niceDate");
                if (time.length() > 10) {
                    time = time.substring(0, 10);
                }
                String title = data.getString("title");
                String chapterName = data.getString("superChapterName");
                String url = data.getString("link");
                HomeArticleData homeArticleData = new HomeArticleData();
                homeArticleData.setAuthor(author);
                homeArticleData.setTitle(title);
                homeArticleData.setTime(time);
                homeArticleData.setUrl(url);
                homeArticleData.setChapterName(chapterName);
                mArticleData.add(homeArticleData);
            }
            Log.d(TAG, "jsonDeco2" + mArticleData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceFragment(Fragment childFragment, String url) {
        if (childFragment == null) {
            childFragment = new WebViewFragment(url);
            Log.d(TAG, "replaceFragment: " + childFragment);
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.fragment_main, childFragment);
            transaction.commit();
        } else {
            Log.e(TAG, "replaceFragment: found exiting childFragment, no need to add it again");
        }
    }
}
