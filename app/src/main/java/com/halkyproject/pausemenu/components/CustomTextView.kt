package com.halkyproject.pausemenu.components


import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.AppCompatTextView
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import com.halkyproject.pausemenu.PauseApp
import com.halkyproject.pausemenu.R

/**
 * Created by rujul on 2/5/2016.
 */
class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    init {
        val array = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextFont,
                0, 0)
        val typefaceType: Int
        try {
            typefaceType = array.getInteger(R.styleable.CustomTextFont_font_name, 0)
        } finally {
            array.recycle()
        }
        if (!isInEditMode) {
            typeface = PauseApp.getApp().getTypeFace(typefaceType)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        processTextWidth()
    }

    private fun processTextWidth(watcher: TextWatcher? = null) {
        if (maxLines == 1 && (width) > 0) {
            if (watcher != null) removeTextChangedListener(watcher)
            val bounds = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bounds)
            val textWidth = bounds.width()
            ellipsize = null
            textScaleX = if (textWidth > width) {
                width * 1f / (textWidth * 1.03f)
            } else {
                textScaleX
            }
            if (watcher != null) addTextChangedListener(watcher)
        } else {
            textScaleX = 1f
        }
    }
}