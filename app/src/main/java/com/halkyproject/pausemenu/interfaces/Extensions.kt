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
        Colourizable.BasicColor.LIGHT_BLUE -> R.color.colourizableLgtBlue
        Colourizable.BasicColor.BLUE -> R.color.colourizableBlue
        Colourizable.BasicColor.PURPLE -> R.color.colourizablePurple
        Colourizable.BasicColor.MAGENTA -> R.color.colourizableMagenta
        Colourizable.BasicColor.PINK -> R.color.colourizablePink
        Colourizable.BasicColor.WHITE -> R.color.colourizableWhite
        Colourizable.BasicColor.GRAY -> R.color.colourizableGray
        Colourizable.BasicColor.BLACK -> R.color.colourizableBlack
    }
}

fun Colourizable.BasicColor.getDarkResourceId(): Int {
    return when (this) {
        Colourizable.BasicColor.RED -> R.color.colourizableDarkRed
        Colourizable.BasicColor.ORANGE -> R.color.colourizableDarkOrange
        Colourizable.BasicColor.YELLOW -> R.color.colourizableDarkYellow
        Colourizable.BasicColor.LIME -> R.color.colourizableDarkLime
        Colourizable.BasicColor.GREEN -> R.color.colourizableDarkGreen
        Colourizable.BasicColor.EMERALD -> R.color.colourizableDarkEmerald
        Colourizable.BasicColor.CYAN -> R.color.colourizableDarkCyan
        Colourizable.BasicColor.LIGHT_BLUE -> R.color.colourizableDarkLgtBlue
        Colourizable.BasicColor.BLUE -> R.color.colourizableDarkBlue
        Colourizable.BasicColor.PURPLE -> R.color.colourizableDarkPurple
        Colourizable.BasicColor.MAGENTA -> R.color.colourizableDarkMagenta
        Colourizable.BasicColor.PINK -> R.color.colourizableDarkPink
        Colourizable.BasicColor.WHITE -> R.color.colourizableDarkWhite
        Colourizable.BasicColor.GRAY -> R.color.colourizableDarkGray
        Colourizable.BasicColor.BLACK -> R.color.colourizableDarkBlack
    }
}

fun Colourizable.BasicColor.getLightResourceId(): Int {
    return when (this) {
        Colourizable.BasicColor.RED -> R.color.colourizableLighterRed
        Colourizable.BasicColor.ORANGE -> R.color.colourizableLighterOrange
        Colourizable.BasicColor.YELLOW -> R.color.colourizableLighterYellow
        Colourizable.BasicColor.LIME -> R.color.colourizableLighterLime
        Colourizable.BasicColor.GREEN -> R.color.colourizableLighterGreen
        Colourizable.BasicColor.EMERALD -> R.color.colourizableLighterEmerald
        Colourizable.BasicColor.CYAN -> R.color.colourizableLighterCyan
        Colourizable.BasicColor.LIGHT_BLUE -> R.color.colourizableLighterLgtBlue
        Colourizable.BasicColor.BLUE -> R.color.colourizableLighterBlue
        Colourizable.BasicColor.PURPLE -> R.color.colourizableLighterPurple
        Colourizable.BasicColor.MAGENTA -> R.color.colourizableLighterMagenta
        Colourizable.BasicColor.PINK -> R.color.colourizableLighterPink
        Colourizable.BasicColor.WHITE -> R.color.colourizableLighterWhite
        Colourizable.BasicColor.GRAY -> R.color.colourizableLighterGray
        Colourizable.BasicColor.BLACK -> R.color.colourizableLighterBlack
    }
}