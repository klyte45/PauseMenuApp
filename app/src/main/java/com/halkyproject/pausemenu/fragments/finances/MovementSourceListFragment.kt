package com.halkyproject.pausemenu.fragments.finances

import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.finances.MovementSourceEdit
import com.halkyproject.pausemenu.interfaces.getResourceId
import com.halkyproject.pausemenu.superclasses.GenericEntityFragment

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MovementSourceListFragment : GenericEntityFragment<MovementSource>() {
    override fun getMainTitle(): String {
        return obj.name
    }

    override fun getBottomTextRight(): String {
        return ""
    }

    override fun getSubTitle(): String {
        return "<font color='#FF0000'>${getString(R.string.finances_debtShort)}</font> <font color='${resources.getColor(obj.outAccount!!.type.getColor().getResourceId(), null)}'>${obj.outAccount!!.name}</font>"
    }

    override fun getBottomTextLeft(): String {
        return "<font color='#00FF00'>${getString(R.string.finances_creditShort)}</font> <font color='${resources.getColor(obj.inAccount!!.type.getColor().getResourceId(), null)}'>${obj.inAccount!!.name}</font>"
    }

    override fun getBgState(): BgState {
        return if (obj.inAccount!!.active && obj.outAccount!!.active) {
            BgState.NORMAL
        } else {
            BgState.INACTIVE
        }
    }

    override fun getIdForAction(): Int? {
        return obj.id
    }

    override fun getEditClass(): Class<*> {
        return MovementSourceEdit::class.java
    }

    override fun getBottomFontHeightSp(): Float {
        return super.getMiddleFontHeightSp()
    }

}
