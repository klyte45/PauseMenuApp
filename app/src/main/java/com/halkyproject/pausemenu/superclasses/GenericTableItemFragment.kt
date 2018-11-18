package com.halkyproject.pausemenu.superclasses

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.components.CustomTextView
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
abstract class GenericTableItemFragment<Entity> : BasicFragment<Entity>() where Entity : Serializable {


    abstract fun getTextColumns(): List<String>
    abstract fun getBgColor(): Int
    abstract fun getFgColor(): Int
    open fun getFontHeightSp(): Float {
        return 10f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment__basic_table_line, container, false)

        val columns = listOf(R.id.m_column1, R.id.m_column2, R.id.m_column3, R.id.m_column4, R.id.m_column5)
        val texts = getTextColumns()
        for ((idx, colId) in columns.withIndex()) {
            with(v.findViewById<CustomTextView>(colId)) {
                if (idx < texts.size) {

                    setTextColor(ColorStateList(
                            arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                            intArrayOf(ContextCompat.getColor(context, getFgColor()), ContextCompat.getColor(context, R.color.inactiveFg))
                    ))

                    text = Html.fromHtml(texts[idx], 0)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, getFontHeightSp())

                    if (idx == texts.size - 1) {
                        textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                    }
                } else {
                    visibility = View.GONE
                }
            }
        }

        v.findViewById<View>(R.id.m_bgLayer).backgroundTintList = ColorStateList(arrayOf(IntArray(0)), intArrayOf(ContextCompat.getColor(context!!, getBgColor())))

        v.isClickable = true
        v.isFocusable = true

        return v
    }
}
