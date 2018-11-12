package com.halkyproject.pausemenu.fragments

import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.pausemenu.activities.finance.MovementSourceEdit
import com.halkyproject.pausemenu.superclasses.GenericFragment

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MovementSourceListFragment : GenericFragment<MovementSource>() {
    override fun getMainTitle(): String {
        return obj.name
    }

    override fun getSubTitle(): String {
        return ""
    }

    override fun getBottomTextLeft(): String {
        return "<font color='#FF0000'>${obj.outAccount!!.name}</font>"
    }

    override fun getBottomTextRight(): String {
        return "<font color='#00FF00'>${obj.inAccount!!.name}</font>"
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

}
