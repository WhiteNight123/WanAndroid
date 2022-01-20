package com.example.wanandroid.page.fragment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wanandroid.bean.HomeDownData;
import com.example.wanandroid.page.adapter.HomeDownRecycleAdapter;
import com.example.wanandroid.page.adapter.HomeUpPagerAdapter;
import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeUpData;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/15
 */
public class HomeFragment extends Fragment {
    private ArrayList<HomeUpData> upData = new ArrayList<>();
    private ArrayList<HomeDownData> downData = new ArrayList<>();
    private ViewPager2 viewPager2;
    private boolean isFirstLoading = true;
    private WebViewFragment webViewFragment;
    private RecyclerView recyclerView;
    private Activity mActivity;
    private View rootView;
    private HomeUpPagerAdapter adapterVP2;
    private HomeDownRecycleAdapter adapterRV;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    jsonDecodeVP2(msg.obj.toString());
                    adapterVP2.notifyDataSetChanged();
                    Log.d(TAG, "Handler1" + upData.toString());

                    break;
                case 2:
                    jsonDecodeRV(msg.obj.toString());
                    adapterRV.notifyDataSetChanged();
                    Log.d(TAG, "Handler2" + downData.toString());
                    break;

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
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initData2();
        initData1();
        viewPager2 = rootView.findViewById(R.id.home_item_vp2);
        adapterVP2 = new HomeUpPagerAdapter(mActivity.getApplicationContext(), upData);
        viewPager2.setAdapter(adapterVP2);
        recyclerView = rootView.findViewById(R.id.home_item_rv);
        adapterRV = new HomeDownRecycleAdapter(downData);
        recyclerView.setAdapter(adapterRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapterRV.setOnHomeRecycleViewListener(new HomeDownRecycleAdapter.HomeRecycleViewListener() {
            @Override
            public void onHomeRecycleViewClick(View view,Object data,int position) {
                webViewFragment = (WebViewFragment) getParentFragmentManager().findFragmentById(R.id.fragment_webview);
                replaceFragment(webViewFragment,data.toString());
                Log.d(TAG, "onHomeRecycleViewClick:111 " + webViewFragment);
            }
        });
        Log.d(TAG, "OnCreatView1" + upData.toString());
        Log.d(TAG, "OnCreatView2" + downData.toString());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            adapterVP2.notifyDataSetChanged();
            adapterRV.notifyDataSetChanged();
        }
        isFirstLoading = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static HomeFragment newInstance(String str) {
        HomeFragment frag = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param_url", str);
        frag.setArguments(bundle);
        return frag;
    }

    public void initData1() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com/banner/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = response;
                handler.sendMessage(msg);
                Log.d(TAG, "net1" + upData.toString());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void initData2() {
        NetUtil.sendHttpRequest("https://www.wanandroid.com/article/top/json", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = response;
                handler.sendMessage(msg);
                Log.d(TAG, "net2" + downData.toString());
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: " + e);
            }
        });
    }

    private void jsonDecodeVP2(String jsonData) {
        upData.clear();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String title = data.getString("title");
                String imagePath = data.getString("imagePath");
                HomeUpData homeUpData = new HomeUpData();
                homeUpData.setTitle(title);
                homeUpData.setImageUrl(imagePath);
                upData.add(homeUpData);
            }
            Log.d(TAG, "jsonDeco1" + upData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsonDecodeRV(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                String author = data.getString("author");
                String time = data.getString("niceDate");
                if(time.length()>10){
                    time=time.substring(0,10);
                }
                String title = data.getString("title");
                String content = data.getString("desc");
                String htmlRegex = "<[^>]+>";
                String spaceRegex = "\\s*|\t|\r|\n";
                content = content.replaceAll(htmlRegex, ""); //去除html标签
                content = content.replaceAll(spaceRegex, ""); //过滤空格
                String chapterName = data.getString("superChapterName");
                String url = data.getString("link");
                HomeDownData homeDownData = new HomeDownData();
                homeDownData.setAuthor(author);
                homeDownData.setTitle(title);
                homeDownData.setTime(time);
                homeDownData.setUrl(url);
                homeDownData.setContent(content);
                homeDownData.setChapterName(chapterName);
                downData.add(homeDownData);
                Log.d(TAG, "jsonDeco2" + downData.toString());
            }
            Log.d(TAG, "jsonDeco2" + downData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceFragment(Fragment childFragment,String url) {
        if (childFragment == null) {
            childFragment = new WebViewFragment(url);
            Log.d(TAG, "replaceFragment: " + childFragment);
            FragmentManager fm = getParentFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.fragment_webview, childFragment);
            transaction.commit();
        } else {
            Log.e(TAG, "replaceFragment: found exiting childFragment, no need to add it again");
        }
    }
}
