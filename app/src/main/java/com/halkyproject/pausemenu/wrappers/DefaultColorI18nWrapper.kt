package com.halkyproject.pausemenu.wrappers

import android.content.Context
import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.lifehack.interfaces.Localizable

class DefaultColorI18nWrapper<T>(ctx: Context, item: T) : DefaultI18nWrapper<T>(ctx, item), Colourizable by DefaultColorWrapper<T>(item)
        where T : Colourizable, T : Localizable