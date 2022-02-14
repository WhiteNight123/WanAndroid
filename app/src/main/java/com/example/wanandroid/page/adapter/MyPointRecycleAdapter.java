package com.example.wanandroid.page.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeArticleData;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/2/13
 */
public class MyPointRecycleAdapter extends RecyclerView.Adapter<MyPointRecycleAdapter.InnerHolder> {
    //借用了HomeArticleData
    private ArrayList<HomeArticleData> mMyPointData;

    public MyPointRecycleAdapter(ArrayList<HomeArticleData> data) {
        this.mMyPointData = data;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mypoint_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.mTVPoint.setText(mMyPointData.get(position).getAuthor());
        holder.mTVTime.setText(mMyPointData.get(position).getTime());
        holder.mTVTitle.setText(mMyPointData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mMyPointData.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        private TextView mTVTitle;
        private TextView mTVTime;
        private TextView mTVPoint;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mTVTitle = itemView.findViewById(R.id.mypoint_tv_title);
            mTVTime = itemView.findViewById(R.id.mypoint_tv_time);
            mTVPoint = itemView.findViewById(R.id.myponint_tv_point);
        }


    }
}
