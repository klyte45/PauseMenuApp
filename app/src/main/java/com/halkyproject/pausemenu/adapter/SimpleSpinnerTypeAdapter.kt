package com.halkyproject.pausemenu.adapter

import android.content.Context
import com.halkyproject.lifehack.interfaces.Disablable
import com.halkyproject.pausemenu.R


class SimpleSpinnerTypeAdapter(context: Context, resource: Int, items: List<Any>, private val fontSize: Float = 10f, private val normalColorResId: Int = R.color.defaultMenuItemColor) : SpinnerTypeAdapter<Void, Disablable<Void>>(context, resource, items, fontSize)