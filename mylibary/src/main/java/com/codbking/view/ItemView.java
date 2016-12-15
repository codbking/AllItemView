package com.codbking.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by codbking
 * //万能itemview
 * 0  default
 * 1  可以打电话
 * 2  版本更新
 * 3  调转网页
 * 4  选择弹框
 * 5  切换开关
 * 6  调整到特定不带参数activity
 */

public class ItemView extends FrameLayout implements View.OnClickListener {

    private IconFontTextView iconTv;
    private TextView textTv;
    private TextView valueTv;

    private String icon;
    private String text;
    private Drawable mDrawable;
    private String value;
    private String activityName;
    private String url;
    private CharSequence[] selectValue;
    private Type type = Type.TEXT;

    private android.widget.ImageView image;
    private IconFontTextView arrorView;
    private ImageView switchView;

    private OnSwitchListener onSwitchListener;

    public interface OnSwitchListener {
        void onSwith(boolean isSelect);
    }

    private enum Type {
        TEXT(0),
        PHONE(1),
        VERSION(2),
        WEB(3),
        SELECT(4),
        SWITCHBTN(5),
        ACTIVITY(6);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public  static Type getType(int value) {
            switch (value) {
                case 0:
                    return Type.TEXT;
                case 1:
                    return Type.PHONE;
                case 2:
                    return Type.VERSION;
                case 3:
                    return Type.WEB;
                case 4:
                    return Type.SELECT;
                case 5:
                    return Type.SWITCHBTN;
                case 6:
                    return Type.ACTIVITY;
            }
            return Type.TEXT;
        }

    }

    //listener
    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public void setSwitchViewListenern(OnClickListener listener) {
        switchView.setOnClickListener(listener);
    }

    public ItemView(Context context) {
        super(context);
    }
    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        icon = a.getString(R.styleable.ItemView_ItemView_icon);
        text = a.getString(R.styleable.ItemView_ItemView_text);
        mDrawable = a.getDrawable(R.styleable.ItemView_ItemView_src);
        value = a.getString(R.styleable.ItemView_ItemView_value);
        type = Type.getType(a.getInt(R.styleable.ItemView_ItemView_type, 0));
        selectValue = a.getTextArray(R.styleable.ItemView_ItemView_select_value);
        activityName=a.getString(R.styleable.ItemView_ItemView_acname);
        url=a.getString(R.styleable.ItemView_ItemView_url);
        a.recycle();
        init();
    }

    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.cbk_item_icon_and_text, this);
        this.switchView = (ImageView) view.findViewById(R.id.switchView);
        this.arrorView = (IconFontTextView) view.findViewById(R.id.arrorView);
        this.image = (ImageView) view.findViewById(R.id.image);
        this.valueTv = (TextView) view.findViewById(R.id.valueTv);
        this.textTv = (TextView) view.findViewById(R.id.text);
        this.iconTv = (IconFontTextView) view.findViewById(R.id.icon);
        //setvalue
        textTv.setText(text);
        valueTv.setText(value);

        if (TextUtils.isEmpty(icon)) {
            iconTv.setVisibility(View.GONE);
        } else {
            iconTv.setVisibility(View.VISIBLE);
            iconTv.setTextImg2(icon);
        }
        if (mDrawable == null) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            image.setImageDrawable(mDrawable);
        }

        if (type == Type.SWITCHBTN) {
            arrorView.setVisibility(GONE);
            switchView.setVisibility(VISIBLE);
        }

        setTypeValue();

    }

    private void setTypeValue() {
        switch(type){
            case PHONE:
                valueTv.setTextColor(Utils.getColor(getContext(),R.color.text_bule));
                setOnClickListener(this);
         break;
            case VERSION:
                valueTv.setText(Utils.getVersionName(getContext()));
                setOnClickListener(this);
         break;
            case SELECT:
                setOnClickListener(this);
                if (selectValue == null || selectValue.length == 0) {
                    return;
                }
                setSelectValue(selectValue[0].toString());
                break;
            case SWITCHBTN:
                switchView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        view.setSelected(!view.isSelected());

                        if (onSwitchListener != null) {
                            onSwitchListener.onSwith(view.isSelected());
                        }

                    }
                });
                break;
            case WEB:
            case ACTIVITY:
                setOnClickListener(this);
                break;

        }
    }


    @Override
    public void setSelected(boolean selected) {
        if(type==Type.SWITCHBTN){
            switchView.setSelected(selected);
        }else{
            super.setSelected(selected);
        }
    }

    @Override
    public boolean isSelected() {
        if(type==Type.SWITCHBTN){
            return switchView.isSelected();
        }
        return super.isSelected();
    }


    public void setLabel(String label) {
        this.textTv.setText(label);
    }

    public void setText(String value) {
        this.value = value;
        this.valueTv.setText(value);
    }

    public String getSelectValue() {
        return valueTv.getTag() == null ? "" : valueTv.getTag().toString();
    }
    private void setSelectValue(String s) {
        if (s.contains("@")) {
            String[] arr = s.split("@");
            valueTv.setText(arr[0]);
            valueTv.setTag(arr[1]);
        } else {
            valueTv.setText(s);
            valueTv.setTag(s);
        }
    }


    @Override
    public void onClick(View view) {
        switch (type) {
            case PHONE:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getContext().startActivity(intent);
                break;
            case VERSION:
//                CheckUtils.checkForUpdate(getContext());
                break;
            case SELECT:
                if (selectValue == null || selectValue.length == 0) {
                    return;
                }
                //判断有没有带值过来
                ListDialog<CharSequence> dialog = new ListDialog<CharSequence>(getContext());
                dialog.setOnItemClickListener(new ListDialog.OnItemClickListener<CharSequence>() {
                    @Override
                    public void onItemClick(CharSequence charSequence) {
                        setSelectValue(charSequence.toString());
                    }
                });
                dialog.setData(selectValue, "@");
                dialog.show();
                break;
            case WEB:
                WebViewActivity.start(getContext(), url);
                break;
            case ACTIVITY:
                try {
                    intent=new Intent(getContext(),Class.forName(activityName));
                    getContext().startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
