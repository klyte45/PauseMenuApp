package com.halkyproject.pausemenu.model.finances

import java.util.*


enum class Currency(val locale: Locale) {
    BRL(Locale("pt", "BR")),
    USD(Locale.US),
    EUR(Locale.GERMANY)
}