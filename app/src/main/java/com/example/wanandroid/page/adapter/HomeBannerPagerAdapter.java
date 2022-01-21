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
import com.example.wanandroid.bean.HomeBannerData;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/15
 */
public class HomeBannerPagerAdapter extends RecyclerView.Adapter<HomeBannerPagerAdapter.InnerHolder> {
    private ArrayList<HomeBannerData> mHomeBannerData;
    public Context mContext;

    public HomeBannerPagerAdapter(Context context, ArrayList<HomeBannerData> data) {
        this.mContext = context;
        this.mHomeBannerData = data;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_vp2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBannerPagerAdapter.InnerHolder holder, int position) {
        holder.textView.setText(mHomeBannerData.get(position).getTitle());
        Glide.with(mContext).load(mHomeBannerData.get(position).getImageUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.imageView.setImageDrawable(resource);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHomeBannerData.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public InnerHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_iv_vp2);
            textView = itemView.findViewById(R.id.home_tv_vp2);

        }
    }
}
