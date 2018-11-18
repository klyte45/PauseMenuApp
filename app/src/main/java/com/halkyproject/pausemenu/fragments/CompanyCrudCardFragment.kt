package com.halkyproject.pausemenu.fragments

import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.Company
import com.halkyproject.pausemenu.activities.CompanyEdit
import com.halkyproject.pausemenu.singletons.FormatSingleton
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
class CompanyCrudCardFragment : GenericEntityFragment<Company>() {
    override fun getMainTitle(): String {
        return obj.mainName
    }

    override fun getSubTitle(): String {
        return obj.realName
    }

    override fun getBottomTextLeft(): String {
        return "${obj.getCountryEnum().emoji} ${obj.cityDisplayName}"
    }

    override fun getBottomTextRight(): String {
        return FormatSingleton.mask(obj.documentNumber, FormatSingleton.FORMAT_CNPJ)
    }

    override fun getBgState(): BgState {
        return BgState.NORMAL
    }

    override fun getIdForAction(): Int? {
        return obj.id
    }

    override fun getEditClass(): Class<*> {
        return CompanyEdit::class.java
    }
}
