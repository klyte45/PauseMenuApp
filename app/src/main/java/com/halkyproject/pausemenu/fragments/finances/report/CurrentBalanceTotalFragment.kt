package com.halkyproject.pausemenu.fragments.finances.report

import android.icu.text.NumberFormat
import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.interfaces.getDarkResourceId
import com.halkyproject.pausemenu.interfaces.getLightResourceId
import com.halkyproject.pausemenu.superclasses.GenericTableItemFragment

class CurrentBalanceTotalFragment : GenericTableItemFragment<Pair<Currency, Number>>() {

    override fun getBgColor(): Int {
        return Colourizable.BasicColor.WHITE.getLightResourceId()
    }

    override fun getFgColor(): Int {
        return Colourizable.BasicColor.BLACK.getDarkResourceId()
    }

    override fun getTextColumns(): List<String> {
        return listOf(getString(R.string.all_total), NumberFormat.getCurrencyInstance(obj.first.locale).format(obj.second))
    }
}