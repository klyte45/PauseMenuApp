package com.halkyproject.pausemenu.fragments.finances.report

import android.icu.text.NumberFormat
import android.view.View
import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.pausemenu.auxobjs.finances.ExtractEntry
import com.halkyproject.pausemenu.interfaces.getDarkResourceId
import com.halkyproject.pausemenu.interfaces.getLightResourceId
import com.halkyproject.pausemenu.singletons.finances.FinancesService.DEFAULT_FORMATTER
import com.halkyproject.pausemenu.superclasses.GenericTableItemFragment
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ProjectionExtractItemFragment : GenericTableItemFragment<ExtractEntry.SingleEntry>() {


    override fun getBgColor(): Int {
        return obj.account.type.getColor().getDarkResourceId()
    }

    override fun getFgColor(): Int {
        return Colourizable.BasicColor.WHITE.getLightResourceId()
    }

    override fun getTextColumns(): List<String> {
        return listOf(
                obj.date.toInstant().atZone(ZoneId.of("GMT")).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                obj.description,
                obj.account.name,
                NumberFormat.getCurrencyInstance(obj.account.currency.locale).format(obj.balance),
                DEFAULT_FORMATTER.format(obj.value)
        )
    }

    override fun getDefaultColumnAlignment(): List<Int> {
        return listOf(View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_TEXT_START, View.TEXT_ALIGNMENT_TEXT_END, View.TEXT_ALIGNMENT_TEXT_END)
    }
}