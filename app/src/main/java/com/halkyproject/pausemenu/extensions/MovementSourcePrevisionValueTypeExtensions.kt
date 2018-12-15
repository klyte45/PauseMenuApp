package com.halkyproject.pausemenu.extensions

import android.util.Log
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import java.math.BigDecimal

fun MovementSourcePrevision.ValueType.getFunction(): (outAcc: FinancialAccount, inAcc: FinancialAccount, value: BigDecimal) -> Pair<BigDecimal, BigDecimal> {
    return when (this) {
        MovementSourcePrevision.ValueType.RESET_IN -> { outAcc, inAcc, _ ->
            if (inAcc.balance ?: BigDecimal.ZERO < BigDecimal.ZERO) {
                Pair(inAcc.balance!! / inAcc.currency.getExchangeRate(outAcc.currency), inAcc.balance!!.abs())
            } else {
                Pair(BigDecimal.ZERO, BigDecimal.ZERO)
            }
        }
        MovementSourcePrevision.ValueType.RESET_OUT -> { outAcc, inAcc, value ->
            if (outAcc.balance ?: BigDecimal.ZERO > BigDecimal.ZERO) {
                Pair(outAcc.balance!! * BigDecimal(-1), outAcc.balance!! * inAcc.currency.getExchangeRate(outAcc.currency))
            } else {
                Pair(BigDecimal.ZERO, BigDecimal.ZERO)
            }
        }
        MovementSourcePrevision.ValueType.ABSOLUTE_IN -> { outAcc, inAcc, value ->
            Pair(value / inAcc.currency.getExchangeRate(outAcc.currency) * BigDecimal(-1), value)
        }
        MovementSourcePrevision.ValueType.ABSOLUTE_OUT -> { outAcc, inAcc, value -> Pair(value * BigDecimal(-1), value * inAcc.currency.getExchangeRate(outAcc.currency)) }
        MovementSourcePrevision.ValueType.PERCENTAGE_IN -> { outAcc, inAcc, perc ->
            if (inAcc.balance ?: BigDecimal.ZERO > BigDecimal.ZERO) {
                val value = inAcc.balance!!.abs() * perc / BigDecimal(100)
                Pair(value / inAcc.currency.getExchangeRate(outAcc.currency) * BigDecimal(-1), value)
            } else {
                Pair(BigDecimal.ZERO, BigDecimal.ZERO)
            }
        }
        MovementSourcePrevision.ValueType.PERCENTAGE_OUT -> { outAcc, inAcc, perc ->
            if (outAcc.balance ?: BigDecimal.ZERO < BigDecimal.ZERO) {
                val value = outAcc.balance!!.abs() * perc / BigDecimal(100)
                Pair(value * BigDecimal(-1), value * inAcc.currency.getExchangeRate(outAcc.currency))
            } else {
                Pair(BigDecimal.ZERO, BigDecimal.ZERO)
            }
        }
    }
}
