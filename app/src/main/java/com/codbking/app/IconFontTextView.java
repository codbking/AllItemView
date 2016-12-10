package com.codbking.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by codbking
 */
public class IconFontTextView extends TextView {


    private String iconfontString = "";
    private String textImg = "";

    private String icon = "";
    private String selectIcon = "";


    public IconFontTextView(Context context) {
        super(context);
        init();
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconFontTextView);
        iconfontString = a.getString(R.styleable.IconFontTextView_iconfont);
        textImg = a.getString(R.styleable.IconFontTextView_textImg);
        a.recycle();
        init();
    }

    public void setHtmlText(String text) {
        CharSequence charSequence = Html.fromHtml(text, null, null);
        setText(charSequence);
    }

    public void setTextImg(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        CharSequence charSequence = Html.fromHtml("&#x" + text, null, null);
        setText(charSequence);
    }

    public void setTextImg2(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        CharSequence charSequence = Html.fromHtml("&#x" + text + ";", null, null);
        setText(charSequence);
    }

    public void setIcon(String text) {
        CharSequence charSequence = Html.fromHtml(text, null, null);
        setText(charSequence);
    }

    private void init() {
        if (TextUtils.isEmpty(iconfontString)) {
            iconfontString = "icon.ttf";
        }
        Typeface iconfont = Typeface.createFromAsset(getContext().getAssets(), iconfontString);
        setTypeface(iconfont);

        if (!TextUtils.isEmpty(textImg)) {
            if (textImg.contains("@")) {
                String[] arr = textImg.split("@");
                icon = arr[0];
                selectIcon = arr[1];
            }else{
                icon=textImg;
            }

            setTextImg2(icon);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (TextUtils.isEmpty(selectIcon)) {
            return;
        }
        if (selected) {
            setTextImg2(selectIcon);
        } else {
            setTextImg2(icon);
        }
    }

}
