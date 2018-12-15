package com.halkyproject.pausemenu.auxobjs.finances

import com.halkyproject.lifehack.model.finances.FinancialAccount
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

class ExtractEntry(private val date: Date, private val valueOut: BigDecimal, private val valueIn: BigDecimal, private val balanceOut: BigDecimal, private val balanceIn: BigDecimal, private val outAccountId: Int, private val inAccountId: Int, private val description: String) {

    fun getSingleEntries(availableAccounts: Map<Int, FinancialAccount>): List<SingleEntry> {
        return listOf(
                SingleEntry(date, valueOut, balanceOut, availableAccounts[outAccountId]!!, description),
                SingleEntry(date, valueIn, balanceIn, availableAccounts[inAccountId]!!, description)
        )
    }

    class SingleEntry(val date: Date, val value: BigDecimal, val balance: BigDecimal, val account: FinancialAccount, val description: String) : Serializable
}