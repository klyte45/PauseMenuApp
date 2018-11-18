package com.halkyproject.pausemenu.singletons.finances

import android.os.AsyncTask
import com.google.gson.reflect.TypeToken
import com.halkyproject.lifehack.controller.FinancesController.Companion.CURRENT_BALANCE_URI
import com.halkyproject.lifehack.controller.FinancesController.Companion.URI_BASE
import com.halkyproject.lifehack.model.aggregation.finances.CurrentBalanceReportItem
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.singletons.HttpService
import java.math.BigDecimal

object FinancesService {

    fun getCurrentBalances(): Map<Currency, List<CurrentBalanceReportItem>> {
        val resultMap = HashMap<Currency, MutableList<CurrentBalanceReportItem>>()
        HttpService.MakeGetTo(URI_BASE + CURRENT_BALANCE_URI, object : TypeToken<List<CurrentBalanceReportItem>>() {}).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get().first!!.forEach {
            if (resultMap[it._id.currency] == null) resultMap[it._id.currency] = arrayListOf(it)
            else resultMap[it._id.currency]!! += it
        }
        return resultMap
    }

    fun getCurrentBalancePhysicalLocal(): BigDecimal {
        val balances = getCurrentBalances()
        if (balances.containsKey(Currency.BRL)) {
            var result: BigDecimal = BigDecimal.ZERO
            for (item in balances[Currency.BRL]!!) {
                when (item._id.type) {
                    FinancialAccount.AccountType.SAVINGS,
                    FinancialAccount.AccountType.CURRENT -> result += item.currentBalance
                    else -> {
                    }
                }
            }
            return result
        }
        return BigDecimal.ZERO
    }
}