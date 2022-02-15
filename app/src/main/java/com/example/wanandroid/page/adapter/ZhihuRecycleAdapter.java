package com.example.wanandroid.page.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/15
 */
public class ZhihuRecycleAdapter extends RecyclerView.Adapter<ZhihuRecycleAdapter.InnerHolder> {
    private Context mContext;
    private ArrayList<HomeArticleData> mData;
    private ZhihuRecycleAdapter.ZhihuRecycleViewListener mListener;

    public ZhihuRecycleAdapter(Context mContext, ArrayList<HomeArticleData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ZhihuRecycleAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zhihu_item_rv, parent, false);
        final ZhihuRecycleAdapter.InnerHolder holder = new ZhihuRecycleAdapter.InnerHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String url = mData.get(position).getUrl();
                mListener.onZhihuRecycleViewClick(view, url, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ZhihuRecycleAdapter.InnerHolder holder, int position) {
        holder.tvAuthor.setText(mData.get(position).getAuthor());
        holder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getTime()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.imageView.setImageDrawable(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        ImageView imageView;
        TextView tvTitle;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.fragment_zhihu_tv_author);
            imageView = itemView.findViewById(R.id.fragment_zhihu_iv_image);
            tvTitle = itemView.findViewById(R.id.fragment_zhihu_tv_title);
        }
    }

    //增加点击监听
    public void setOnZhihuRecycleViewListener(ZhihuRecycleViewListener listener) {
        this.mListener = listener;
    }

    //点击监听回调接口
    public interface ZhihuRecycleViewListener {
        void onZhihuRecycleViewClick(View view, Object data, int position);
    }
}
