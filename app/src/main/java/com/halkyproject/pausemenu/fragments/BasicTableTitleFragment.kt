package com.halkyproject.pausemenu.fragments

import com.halkyproject.pausemenu.superclasses.GenericTitleLineFragment

class BasicTableTitleFragment : GenericTitleLineFragment<String>() {
    override fun getText(): String {
        return obj
    }
}