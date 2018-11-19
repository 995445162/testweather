package com.example.testweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testweather.entity.CityItem;
import com.example.testweather.R;

import java.util.List;

/**
 * Created by tujianhua on 2017/11/23.
 */

public class AddAdapter extends BaseAdapter {
    private List<CityItem>current_cities;
    private Context mcontext;
    private onDeleteClickListener onDeleteClickListener;

    public AddAdapter(List<CityItem> current_cities, Context mcontext) {
        this.current_cities = current_cities;
        this.mcontext = mcontext;
    }

    public interface onDeleteClickListener{
         void onClick(View view,int position);
    }

    public void setOnDeleteClickListener(onDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener=onDeleteClickListener;
    }

    @Override


    public int getCount() {
        return current_cities.size();
    }

    @Override
    public Object getItem(int i) {
        return current_cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mcontext).inflate(R.layout.list_item, viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.iv_delete = view.findViewById(R.id.iv_delete);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(current_cities.get(i).getName());
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onClick(view,i);
            }
        });
        return view;
    }

    class ViewHolder{
        private TextView tv_name;
        private ImageView iv_delete;
    }
}
