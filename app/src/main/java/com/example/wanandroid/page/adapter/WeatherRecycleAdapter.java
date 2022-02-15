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
 * @data 2022/2/15
 */
public class WeatherRecycleAdapter extends RecyclerView.Adapter<WeatherRecycleAdapter.InnerHolder> {
    private ArrayList<HomeArticleData> mData;

    public WeatherRecycleAdapter(ArrayList<HomeArticleData> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.TVWeather.setText(mData.get(position).getTitle());
        holder.TVTemp.setText(mData.get(position).getContent());
        holder.TVTime.setText(mData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InnerHolder extends RecyclerView.ViewHolder {

        TextView TVWeather;
        TextView TVTemp;
        TextView TVTime;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            TVWeather = itemView.findViewById(R.id.weather_hour_weather);
            TVTemp = itemView.findViewById(R.id.weather_hour_temp);
            TVTime = itemView.findViewById(R.id.weather_hour_time);
        }
    }

}
