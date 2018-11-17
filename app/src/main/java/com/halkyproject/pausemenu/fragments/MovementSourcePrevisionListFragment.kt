package com.halkyproject.pausemenu.fragments

import android.icu.text.NumberFormat
import android.os.Bundle
import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.activities.finances.MovementSourcePrevisionEdit
import com.halkyproject.pausemenu.activities.finances.MovementSourcePrevisionList
import com.halkyproject.pausemenu.extensions.formatDateValue
import com.halkyproject.pausemenu.superclasses.GenericFragment
import java.text.DateFormat

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MovementSourcePrevisionListFragment : GenericFragment<MovementSourcePrevision>() {
    override fun getMainTitle(): String {
        return DateFormat.getDateInstance().format(obj.startDate)
    }

    override fun getBottomTextRight(): String {
        return if (obj.value != null) {
            NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.value)
        } else {
            NumberFormat.getInstance().format(obj.percValue!!.setScale(2)) + "%"
        }
    }

    override fun getSubTitle(): String {
        return resources.getString(resources.getIdentifier(obj.frequency.getLocaleId(), "string", "com.halkyproject.pausemenu"))
    }

    override fun getBottomTextLeft(): String {
        return obj.frequency.formatDateValue(this.requireActivity(), obj)
    }

    override fun getBgState(): BgState {
        return BgState.NORMAL
    }

    override fun getIdForAction(): Int? {
        return obj.id
    }

    override fun getEditClass(): Class<*> {
        return MovementSourcePrevisionEdit::class.java
    }

    override fun addToBundle(): Bundle {
        val bundle: Bundle = super.addToBundle()
        bundle.putInt(MovementSourcePrevisionList.KEY_PARENT_ID, obj.movSourceId!!)
        bundle.putString(MovementSourcePrevisionList.KEY_PARENT_NAME, obj.movSource!!.name)
        return bundle
    }

}
