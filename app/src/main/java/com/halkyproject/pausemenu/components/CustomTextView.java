package com.halkyproject.pausemenu.components;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.halkyproject.pausemenu.PauseApp;
import com.halkyproject.pausemenu.R;

/**
 * Created by rujul on 2/5/2016.
 */
public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        int typefaceType;
        try {
            typefaceType = array.getInteger(R.styleable.CustomTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(PauseApp.getApp().getTypeFace(typefaceType));
        }
    }
}