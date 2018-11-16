package com.halkyproject.pausemenu.extensions

import android.content.Context
import android.icu.text.DateFormatSymbols
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.R

fun MovementSourcePrevision.Frequency.formatDateValue(ctx: Context, obj: MovementSourcePrevision): String {
    return when (this) {
        MovementSourcePrevision.Frequency.MONTHLY -> String.format(ctx.getString(R.string.finances_frequency_monthFormat), obj.refDay)
        MovementSourcePrevision.Frequency.WEEKLY -> String.format(ctx.getString(R.string.finances_frequency_weekFormat), DateFormatSymbols.getInstance().weekdays[obj.refDay!!])
        MovementSourcePrevision.Frequency.FIFTHLY -> String.format(ctx.getString(R.string.finances_frequency_fifthlyFormat), obj.refDay, obj.refDay!! + 15)
        MovementSourcePrevision.Frequency.DAILY -> ctx.getString(R.string.finances_frequency_dayFormat)
        MovementSourcePrevision.Frequency.YEARLY -> String.format(ctx.getString(R.string.finances_frequency_anualFormat), obj.refDay, DateFormatSymbols.getInstance().months[obj.refMonth!!])
        MovementSourcePrevision.Frequency.NONE -> String.format(ctx.getString(R.string.finances_frequency_oneshotFormat), android.icu.text.DateFormat.getInstance().format(obj.startDate))
    }
}

fun MovementSourcePrevision.Frequency.getOptions(): Pair<List<String>?, List<String>?> {
    return when (this) {
        MovementSourcePrevision.Frequency.DAILY, MovementSourcePrevision.Frequency.NONE -> Pair(null, null)
        MovementSourcePrevision.Frequency.MONTHLY, MovementSourcePrevision.Frequency.FIFTHLY, MovementSourcePrevision.Frequency.YEARLY -> {
            val listDays = ArrayList<String>()
            var i = this.minDay!!
            while (i <= this.maxDay!!) {
                listDays +=
                        if (this == MovementSourcePrevision.Frequency.FIFTHLY) {
                            "$i & ${i + 15}"
                        } else {
                            i.toString()
                        }
                i++
            }
            var monthList: List<String>? = null
            if (this == MovementSourcePrevision.Frequency.YEARLY) {
                monthList = ArrayList()
                var j = this.minMonth!!
                while (j <= this.maxMonth!!) {
                    monthList.add(DateFormatSymbols.getInstance().months[j])
                    j++
                }
            }
            Pair(listDays, monthList)
        }
        MovementSourcePrevision.Frequency.WEEKLY -> {
            val listDays = ArrayList<String>()
            var i = this.minDay!!
            while (i <= this.maxDay!!) {
                listDays += DateFormatSymbols.getInstance().weekdays[i]
                i++
            }
            Pair(listDays, null)
        }
    }
}

