package com.example.testweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testweather.util.FontItemManager;
import com.example.testweather.R;

import java.util.List;

/**
 * Created by tujianhua on 2017/11/14.
 */

public class SelectAdapter extends BaseExpandableListAdapter{
    private Context mcontext;
    private List<String> provinces;
    //private List<String> cities;
    private List<List<String>> pro_item;

    public SelectAdapter(Context mcontext, List<String> provinces, List<List<String>> pro_item) {
        this.mcontext = mcontext;
        this.provinces = provinces;
        this.pro_item = pro_item;
    }

    @Override


    public int getGroupCount() {
        return provinces.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return pro_item.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return provinces.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return pro_item.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ParentViewHolder parentViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mcontext).inflate(R.layout.elv_parent, viewGroup, false);
            parentViewHolder=new ParentViewHolder();
            parentViewHolder.iv_group = view.findViewById(R.id.iv_group);
            parentViewHolder.tv_group = view.findViewById(R.id.tv_group);
            view.setTag(parentViewHolder);
        }else {
            parentViewHolder= (ParentViewHolder) view.getTag();
        }
        if (b){
            parentViewHolder.iv_group.setImageResource(R.drawable.elv_shrink);
        }else {
            parentViewHolder.iv_group.setImageResource(R.drawable.elv_expand);
        }
        parentViewHolder.tv_group.setText(provinces.get(i));
        FontItemManager.getInstance().changeFont(parentViewHolder.tv_group);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = LayoutInflater.from(mcontext).inflate(R.layout.evl_son, viewGroup, false);
            childViewHolder=new ChildViewHolder();
            childViewHolder.tv_item = view.findViewById(R.id.tv_item);
            view.setTag(childViewHolder);
        }else {
            childViewHolder= (ChildViewHolder) view.getTag();
        }
        childViewHolder.tv_item.setText(pro_item.get(i).get(i1));
        FontItemManager.getInstance().changeFont(childViewHolder.tv_item);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private static class ParentViewHolder{
        ImageView iv_group;
        TextView tv_group;
    }

    private static class ChildViewHolder{
       // ImageView iv_item;
        TextView tv_item;
    }
}
