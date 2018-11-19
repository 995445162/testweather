package com.example.testweather.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testweather.entity.CityItem;
import com.example.testweather.util.FontItemManager;
import com.example.testweather.R;

import java.util.List;

/**
 * Created by tujianhua on 2017/10/28.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private Context mcontext;
    /*List<Map> cities;
    public WeatherAdapter(List<Map> cities) {
        this.cities=cities;
    }*/

    private List<CityItem> cities_list;

    private onItemClickListener mOnItemClickListener;
    private onItemLongClickListener mOnItemLongClickListener;

    public WeatherAdapter(List<CityItem> cities_list) {
        this.cities_list = cities_list;
    }

    public interface onItemClickListener{
        void onItemClick(View view,int position);
    }
    public interface onItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(onItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
    public void setOnItemLongClickListener(onItemLongClickListener mOnItemLongClickListener){
        this.mOnItemLongClickListener=mOnItemLongClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tv_name;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mcontext == null) {
            mcontext=parent.getContext();
        }
        View view = LayoutInflater.from(mcontext).inflate(R.layout.cityitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_name.setText(cities_list.get(position).getName());
        FontItemManager.getInstance().changeFont(holder.tv_name);
        if (mOnItemClickListener != null) {
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(view,position);
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.tv_name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position=holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(view,position);
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return cities_list.size();
    }
}
