package com.halkyproject.pausemenu.interfaces

import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.pausemenu.R

fun Colourizable.BasicColor.getResourceId(): Int {
    return when (this) {
        Colourizable.BasicColor.RED -> R.color.colourizableRed
        Colourizable.BasicColor.YELLOW -> R.color.colourizableYellow
        Colourizable.BasicColor.GREEN -> R.color.colourizableGreen
        Colourizable.BasicColor.CYAN -> R.color.colourizableCyan
        Colourizable.BasicColor.BLUE -> R.color.colourizableBlue
        Colourizable.BasicColor.MAGENTA -> R.color.colourizableMagenta
        Colourizable.BasicColor.WHITE -> R.color.colourizableWhite
        Colourizable.BasicColor.BLACK -> R.color.colourizableBlack
    }
}