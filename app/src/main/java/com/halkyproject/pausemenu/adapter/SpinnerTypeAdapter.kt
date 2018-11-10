package com.halkyproject.pausemenu.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.halkyproject.pausemenu.PauseApp
import com.halkyproject.pausemenu.R


class SpinnerTypeAdapter(context: Context, resource: Int, items: List<String>, private val fontSize: Float = 10f) : ArrayAdapter<String>(context, resource, items) {

    // Affects default (closed) state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getView(position, convertView, parent) as TextView?
        view?.typeface = PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P)
        view?.setSingleLine(false)
        view?.isAllCaps = true
        view?.setTextColor(ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(ContextCompat.getColor(context, R.color.colorPrimary), ContextCompat.getColor(context, R.color.inactiveFg))
        ))
        view?.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
        view?.ellipsize = null
        view?.maxLines = 2
        view?.isEnabled = parent.isEnabled && (convertView?.isEnabled ?: false)
        return view
    }

    // Affects opened state of the spinner
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getDropDownView(position, convertView, parent) as TextView?
        view?.typeface = PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P)
        view?.isAllCaps = true
        view?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        view?.ellipsize = null
        return view
    }
}