package com.halkyproject.pausemenu.fragments.finances.report

import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.interfaces.getDarkResourceId
import com.halkyproject.pausemenu.interfaces.getLightResourceId
import com.halkyproject.pausemenu.singletons.finances.FinancesService.DEFAULT_FORMATTER
import com.halkyproject.pausemenu.superclasses.GenericTableItemFragment

class BasicAccountingTotal : GenericTableItemFragment<Number>() {

    override fun getBgColor(): Int {
        return Colourizable.BasicColor.WHITE.getLightResourceId()
    }

    override fun getFgColor(): Int {
        return Colourizable.BasicColor.BLACK.getDarkResourceId()
    }

    override fun getTextColumns(): List<String> {
        return listOf(getString(R.string.all_total), DEFAULT_FORMATTER.format(obj))
    }
}