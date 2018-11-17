package com.halkyproject.pausemenu.fragments

import android.icu.text.NumberFormat
import android.os.Bundle
import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.R
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
        return when (obj.valueType) {

            MovementSourcePrevision.ValueType.ABSOLUTE_IN,
            MovementSourcePrevision.ValueType.ABSOLUTE_OUT -> NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.value)
            MovementSourcePrevision.ValueType.PERCENTAGE_IN,
            MovementSourcePrevision.ValueType.PERCENTAGE_OUT -> NumberFormat.getInstance().format(obj.value!!.setScale(2)) + "%"
            MovementSourcePrevision.ValueType.RESET_IN -> getString(R.string.finances_valueType_resetIn)
            MovementSourcePrevision.ValueType.RESET_OUT -> getString(R.string.finances_valueType_resetOut)
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
        bundle.putSerializable(MovementSourcePrevisionList.KEY_PARENT, obj.movSource)
        return bundle
    }

}
