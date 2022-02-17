package com.example.wanandroid.page.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.wanandroid.R;
import com.example.wanandroid.utils.NetCallbackListener;
import com.example.wanandroid.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/15
 */
public class OneArticleFragment extends Fragment {
    private Activity mActivity;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvContent;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 16:
                    jsonDecode(msg.obj.toString());
                    break;

            }
            return false;
        }
    });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity=(AppCompatActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_onetitle, container, false);
        initData();
        mTvAuthor = mRootView.findViewById(R.id.fragment_onetitle_tv_author);
        mTvTitle = mRootView.findViewById(R.id.fragment_onetitle_tv_title);
        mTvContent = mRootView.findViewById(R.id.fragment_onetitle_tv_content);

        return mRootView;
    }

    private void initData() {
        NetUtil.sendHttpRequest("https://v2.alapi.cn/api/one?token=dRW8QdwxVa5L4RCr", "GET", null, new NetCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message message = Message.obtain();
                message.what = 16;
                message.obj = response;
                mHandler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "网络遇到错误了", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        });
    }

    private void jsonDecode(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            mTvTitle.setText(jsonObject1.getString("title"));
            mTvAuthor.setText(jsonObject1.getString("subtitle"));
            mTvContent.setText(Html.fromHtml(jsonObject1.getString("content"), Html.FROM_HTML_MODE_COMPACT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
