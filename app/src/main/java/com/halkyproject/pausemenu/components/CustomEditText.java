package com.halkyproject.pausemenu.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.halkyproject.pausemenu.PauseApp;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        this.setTypeface(PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P));
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P), style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P));
    }
}