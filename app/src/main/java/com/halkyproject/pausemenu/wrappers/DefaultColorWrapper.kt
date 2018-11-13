package com.halkyproject.pausemenu.wrappers

import com.halkyproject.lifehack.interfaces.Colourizable

open class DefaultColorWrapper<T>(item: T) : BasicWrapper<T>(item), Colourizable where T : Colourizable {
    override fun getColor(): Colourizable.BasicColor {
        return item.getColor()
    }

}
