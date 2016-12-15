package com.codbking.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by codbking
 */
public class ListDialog<T> extends Dialog implements AdapterView.OnItemClickListener {

    private ListView list;
    private MyAdatepr<T> adapter;

    private String methodName;
    private OnItemClickListener<T> onItemClickListener;

    public ListDialog(Context context) {
        super(context,R.style.dialogStyle);
    }

    public void setData(T[] data, String methodName) {
        if (adapter == null) {
            adapter = new MyAdatepr();
        }
        adapter.setData(data);
        this.methodName = methodName;
    }

    public void setData(List<T> data, String methodName) {
        if (adapter == null) {
            adapter = new MyAdatepr();
        }
        adapter.setData(data);
        this.methodName = methodName;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void setFullScreenAndLayoutBottom() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = (int) Utils.getScreenWidth(getContext());
        getWindow().setAttributes(params);
        getWindow().setWindowAnimations(R.style.BottomPushAni);
    }

    protected void setLayoutCentre() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);
        setFullScreenAndLayoutBottom();

        this.list = (ListView) findViewById(R.id.list);

        View footView = LayoutInflater.from(getContext()).inflate(R.layout.round_btn, null);
        TextView textView = (TextView) footView.findViewById(R.id.text);
        textView.setBackgroundDrawable(
                Utils.getRoundSelectorDrawable(0xcc, Utils.getColor(getContext(),R.color.white), 5));

        list.addFooterView(footView, null, false);
        list.setFooterDividersEnabled(false);

        list.setOnItemClickListener(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (adapter == null) {
            adapter = new MyAdatepr();
        }
        list.setAdapter(adapter);
    }

    //判断是不是基本类型
    private <E> boolean isBasicTypes(E t) {
        return  t instanceof String || t instanceof CharSequence ||
                t instanceof Integer || t instanceof Double ||
                t instanceof Float || t instanceof  Long;
    }

    private class MyAdatepr<T> extends BaseAdapterE<T> {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                TextView textView=new TextView(parent.getContext());
                textView.setBackgroundResource(R.drawable.list_selector_background);
                textView.setGravity(Gravity.CENTER);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getContext().getResources().getDimensionPixelSize(R.dimen.item_height));
                textView.setLayoutParams(params);
                textView.setTextColor(getContext().getResources().getColor(R.color.text_body));
                convertView=textView;
            }

            T object = getItem(position);
            TextView textView = (TextView) convertView;

            if (isBasicTypes(object)) {
                if(methodName.equals("@")){
                    textView.setText(object.toString().split("@")[0]);
                }else{
                    textView.setText(object.toString());
                }
            } else {
                try {
                    Method m = object.getClass().getDeclaredMethod(methodName);
                    Object result = m.invoke(object);
                    textView.setText(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return convertView;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        T t = (T) parent.getItemAtPosition(position);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(t);
        }
    }


    public interface OnItemClickListener<T> {
        void onItemClick(T t);
    }



}
