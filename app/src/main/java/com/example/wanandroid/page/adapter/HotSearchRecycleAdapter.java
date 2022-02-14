package com.example.wanandroid.page.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.R;

import java.util.ArrayList;

/**
 * @author WhiteNight123 (Guo Xiaoqiang)
 * @email 1448375249@qq.com
 * @data 2022/1/21
 */
public class HotSearchRecycleAdapter extends RecyclerView.Adapter<HotSearchRecycleAdapter.InnerHolder> {
    private ArrayList<String> mHotSearchDdata;
    private HotSearchViewListener mListener;

    public HotSearchRecycleAdapter(ArrayList<String> data) {
        this.mHotSearchDdata = data;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotsearch_item_rv, parent, false);
        final InnerHolder holder = new InnerHolder(view);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String data = mHotSearchDdata.get(position);
                mListener.onHotSearchItemClick(view, data, position);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.button.setText(mHotSearchDdata.get(position));
    }

    @Override
    public int getItemCount() {
        return mHotSearchDdata.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {
        private Button button;

        public InnerHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.hotsearch_btn);
        }
    }

    public void setOnHotSearchRecycleViewListener(HotSearchViewListener listener) {
        this.mListener = listener;

    }

    public interface HotSearchViewListener {
        void onHotSearchItemClick(View view, String data, int position);
    }
}
