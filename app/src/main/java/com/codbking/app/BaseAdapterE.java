package com.codbking.app;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by codbking on 2016/4/11.
 */
abstract class BaseAdapterE<T> extends BaseAdapter {

    private List<T> list;
    private T[]data;

    public void setData(List<T> data) {
            this.list=data;
            notifyDataSetChanged();
    }

    public void setData(T[] data) {
        this.data=data;
        notifyDataSetChanged();
    }

    public void addData(List<T> date) {
        if (date != null) {
            this.list.addAll(date);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(data!=null){
            return data.length;
        }
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
          if(data!=null){
              return data[position];
          }
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
