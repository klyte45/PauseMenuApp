package com.halkyproject.pausemenu.interfaces

import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.pausemenu.R

fun Colourizable.BasicColor.getResourceId(): Int {
    return when (this) {
        Colourizable.BasicColor.RED -> R.color.colourizableRed
        Colourizable.BasicColor.ORANGE -> R.color.colourizableOrange
        Colourizable.BasicColor.YELLOW -> R.color.colourizableYellow
        Colourizable.BasicColor.LIME -> R.color.colourizableLime
        Colourizable.BasicColor.GREEN -> R.color.colourizableGreen
        Colourizable.BasicColor.EMERALD -> R.color.colourizableEmerald
        Colourizable.BasicColor.CYAN -> R.color.colourizableCyan
        Colourizable.BasicColor.LIGHT_BLUE -> R.color.colourizableLightBlue
        Colourizable.BasicColor.BLUE -> R.color.colourizableBlue
        Colourizable.BasicColor.PURPLE -> R.color.colourizablePurple
        Colourizable.BasicColor.MAGENTA -> R.color.colourizableMagenta
        Colourizable.BasicColor.PINK -> R.color.colourizablePink
        Colourizable.BasicColor.WHITE -> R.color.colourizableWhite
        Colourizable.BasicColor.GRAY -> R.color.colourizableGray
        Colourizable.BasicColor.BLACK -> R.color.colourizableBlack
    }
}