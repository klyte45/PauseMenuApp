package com.halkyproject.pausemenu.fragments.finances.report

import android.icu.text.NumberFormat
import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.interfaces.getDarkResourceId
import com.halkyproject.pausemenu.interfaces.getLightResourceId
import com.halkyproject.pausemenu.superclasses.GenericTableItemFragment

class ProjectionAccountItemFragment : GenericTableItemFragment<FinancialAccount>() {

    override fun getBgColor(): Int {
        return obj.type.getColor().getDarkResourceId()
    }

    override fun getFgColor(): Int {
        return Colourizable.BasicColor.WHITE.getLightResourceId()
    }

    override fun getTextColumns(): List<String> {
        return listOf(
                obj.name,
                getString(resources.getIdentifier(obj.type.getLocaleId(), "string", "com.halkyproject.pausemenu")),
                NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.balance)
        )
    }
}