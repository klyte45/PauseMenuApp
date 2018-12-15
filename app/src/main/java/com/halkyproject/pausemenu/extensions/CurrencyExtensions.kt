package com.halkyproject.pausemenu.extensions

import com.halkyproject.lifehack.model.enums.Currency
import java.math.BigDecimal

fun Currency.getExchangeRate(other: Currency): BigDecimal {
    if (other == this) return BigDecimal.ONE.setScale(20, BigDecimal.ROUND_HALF_DOWN)
    return when (this) {
        Currency.BRL -> when (other) {
            Currency.USD -> BigDecimal(3.7).setScale(20, BigDecimal.ROUND_HALF_DOWN)
            Currency.EUR -> BigDecimal(4.5).setScale(20, BigDecimal.ROUND_HALF_DOWN)
            else -> null
        }
        Currency.USD -> when (other) {
            Currency.BRL -> (BigDecimal.ONE.setScale(20) / other.getExchangeRate(this)).setScale(20, BigDecimal.ROUND_HALF_DOWN)
            Currency.EUR -> BigDecimal(4.5 / 3.7).setScale(20, BigDecimal.ROUND_HALF_DOWN)
            else -> null
        }
        Currency.EUR -> when (other) {
            Currency.BRL,
            Currency.USD -> (BigDecimal.ONE.setScale(20, BigDecimal.ROUND_HALF_DOWN) / other.getExchangeRate(this)).setScale(20, BigDecimal.ROUND_HALF_DOWN)
            else -> null
        }
    } ?: BigDecimal.ONE.setScale(20, BigDecimal.ROUND_HALF_DOWN)
}
