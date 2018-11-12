package com.halkyproject.pausemenu.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.pausemenu.PauseApp
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.interfaces.getResourceId


class SpinnerTypeAdapter(context: Context, resource: Int, items: List<Any>, private val fontSize: Float = 10f, private val normalColorResId: Int = R.color.colorPrimary) : ArrayAdapter<Any>(context, resource, items) {

    // Affects default (closed) state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view = super.getView(position, convertView, parent) as TextView?

        val selectedItem = getItem(position)
        val normalColor =
                if (selectedItem is Colourizable) {
                    selectedItem.getColor().getResourceId()
                } else {
                    normalColorResId
                }
        view?.typeface = PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P)
        view?.setSingleLine(false)
        view?.isAllCaps = true
        view?.setTextColor(ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(ContextCompat.getColor(context, normalColor), ContextCompat.getColor(context, R.color.inactiveFg))
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
        val item = getItem(position)

        val normalColor =
                if (item is Colourizable) {
                    item.getColor().getResourceId()
                } else {
                    R.color.colourizableWhite
                }

        view?.typeface = PauseApp.getApp().getTypeFace(PauseApp.FontsEnum.P2P)
        view?.isAllCaps = true
        view?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        view?.setTextColor(ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(ContextCompat.getColor(context, normalColor), ContextCompat.getColor(context, R.color.inactiveFg))
        ))
        view?.ellipsize = null

        return view
    }
}