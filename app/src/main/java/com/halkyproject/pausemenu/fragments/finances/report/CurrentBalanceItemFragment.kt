package com.halkyproject.pausemenu.fragments.finances.report

import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.lifehack.model.aggregation.finances.CurrentBalanceReportItem
import com.halkyproject.pausemenu.interfaces.getDarkResourceId
import com.halkyproject.pausemenu.interfaces.getLightResourceId
import com.halkyproject.pausemenu.singletons.finances.FinancesService.DEFAULT_FORMATTER
import com.halkyproject.pausemenu.superclasses.GenericTableItemFragment

class CurrentBalanceItemFragment : GenericTableItemFragment<CurrentBalanceReportItem>() {

    override fun getBgColor(): Int {
        return obj._id.type.getColor().getDarkResourceId()
    }

    override fun getFgColor(): Int {
        return Colourizable.BasicColor.WHITE.getLightResourceId()
    }

    override fun getTextColumns(): List<String> {
        return listOf(getString(resources.getIdentifier(obj._id.type.getLocaleId(), "string", "com.halkyproject.pausemenu")), DEFAULT_FORMATTER.format(obj.currentBalance))
    }
}