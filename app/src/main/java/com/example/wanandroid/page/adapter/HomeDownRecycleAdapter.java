package com.example.wanandroid.page.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;
import com.example.wanandroid.bean.HomeDownData;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/17
 */
public class HomeDownRecycleAdapter extends RecyclerView.Adapter<HomeDownRecycleAdapter.InnerHolder> {
    private ArrayList<HomeDownData> data;
    private HomeRecycleViewListener mListener;

    public HomeDownRecycleAdapter(ArrayList<HomeDownData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_rv, parent, false);
        final InnerHolder holder=new InnerHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                String url=data.get(position).getUrl();
                mListener.onHomeRecycleViewClick(view,url,position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.tvAuthor.setText(data.get(position).getAuthor());
        holder.tvTime.setText(data.get(position).getTime());
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvContent.setText(data.get(position).getContent());
        holder.tvChapterName.setText(data.get(position).getChapterName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
        TextView tvChapterName;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.home_tv_rv_author);
            tvTime = itemView.findViewById(R.id.home_tv_rv_time);
            tvTitle = itemView.findViewById(R.id.home_tv_rv_title);
            tvContent = itemView.findViewById(R.id.home_tv_rv_content);
            tvChapterName = itemView.findViewById(R.id.home_tv_rv_chaptername);
        }
    }
    //增加点击监听
    public void setOnHomeRecycleViewListener(HomeRecycleViewListener listener){
        this.mListener=listener;
    }
    //点击监听回调接口
    public interface HomeRecycleViewListener{
        void onHomeRecycleViewClick(View view,Object data,int position);
    }
}
