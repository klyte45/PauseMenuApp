package com.halkyproject.pausemenu.superclasses

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.halkyproject.pausemenu.R
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
abstract class GenericTitleLineFragment<Entity> : BasicFragment<Entity>() where Entity : Serializable {


    abstract fun getText(): String
    open fun getBgColor(): Int? {
        return null
    }

    open fun getFgColor(): Int? {
        return null
    }

    open fun getFontHeightSp(): Float {
        return 36f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment__basic_item_title, container, false)
        if (getBgColor() != null) v.findViewById<ViewGroup>(R.id.m_bgLayer).backgroundTintList = ColorStateList(arrayOf(IntArray(0)), intArrayOf(resources.getColor(getBgColor()!!, null)))
        with(v.findViewById<TextView>(R.id.tt_title)) {
            if (getFgColor() != null) setTextColor(getFgColor()!!)
            text = Html.fromHtml(this@GenericTitleLineFragment.getText(), 0)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, getFontHeightSp())
        }
        v.isClickable = true
        v.isFocusable = true
        return v
    }
}
