package com.halkyproject.pausemenu.wrappers

import android.content.Context
import android.content.res.Resources
import com.halkyproject.lifehack.interfaces.Localizable

open class DefaultI18nWrapper<T>(private val ctx: Context, item: T) : BasicWrapper<T>(item) where T : Localizable {
    final override fun toString(): String {
        return ctx.getString(ctx.resources.getIdentifier(item.getLocaleId(), "string", "com.halkyproject.pausemenu"))
    }
}
