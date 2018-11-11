package com.halkyproject.pausemenu.superclasses

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.halkyproject.lifehack.interfaces.BasicEntityModel
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.components.CustomTextView

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
abstract class GenericFragment<Entity> : BasicFragment<Entity>() where Entity : BasicEntityModel {
    abstract fun getMainTitle(): String
    abstract fun getSubTitle(): String
    abstract fun getBottomTextLeft(): String
    abstract fun getBottomTextRight(): String
    abstract fun getBgState(): BgState
    abstract fun getIdForAction(): Int?
    abstract fun getBundleKey(): String
    abstract fun getEditClass(): Class<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment__basic_crud_card, container, false)

        v.findViewById<CustomTextView>(R.id.m_title).text = Html.fromHtml(getMainTitle(), 0)
        v.findViewById<CustomTextView>(R.id.m_subtitle).text = Html.fromHtml(getSubTitle(), 0)
        v.findViewById<CustomTextView>(R.id.m_bottomDataLeft).text = Html.fromHtml(getBottomTextLeft(), 0)
        v.findViewById<CustomTextView>(R.id.m_bottomDataRight).text = Html.fromHtml(getBottomTextRight(), 0)
        v.findViewById<LinearLayout>(R.id.m_bgLayer).backgroundTintList = ColorStateList(arrayOf(IntArray(0)), intArrayOf(resources.getColor(getBgState().colorResId, null)))

        v.isClickable = true
        v.isFocusable = true
        v.setOnClickListener {
            val intent = Intent(context, getEditClass())
            val b = Bundle()
            b.putInt(getBundleKey(), getIdForAction() ?: -1)
            intent.putExtras(b)
            startActivityForResult(intent, 0)
        }
        return v
    }


    enum class BgState(val colorResId: Int) {
        NORMAL(R.color.colorSaturation1),
        INACTIVE(R.color.inactiveBg)
    }
}