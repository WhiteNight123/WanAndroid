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
 * @data 2022/2/14
 */
public class QuestionRecycleAdapter extends RecyclerView.Adapter<QuestionRecycleAdapter.InnerHolder> {
    private ArrayList<HomeArticleData> mRankData;

    public QuestionRecycleAdapter(ArrayList<HomeArticleData> mRankData) {
        this.mRankData = mRankData;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.tvRank.setText(mRankData.get(position).getTitle());
        holder.tvName.setText(mRankData.get(position).getAuthor());
        holder.tvPoint.setText(mRankData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mRankData.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        TextView tvRank;
        TextView tvName;
        TextView tvPoint;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.fragment_rank_tv_rank);
            tvName = itemView.findViewById(R.id.fragment_rank_tv_username);
            tvPoint = itemView.findViewById(R.id.fragment_rank_tv_point);
        }

    }
}
