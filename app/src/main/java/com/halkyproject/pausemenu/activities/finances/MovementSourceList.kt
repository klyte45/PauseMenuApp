package com.halkyproject.pausemenu.activities.finances

import android.support.constraint.ConstraintLayout
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.MovementSourceListFragment
import com.halkyproject.pausemenu.singletons.finances.MovementSourceService
import com.halkyproject.pausemenu.superclasses.GenericListingActivity
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


class MovementSourceList : GenericListingActivity<MovementSource, MovementSourceList, MovementSourceListFragment>() {
    override fun getListTitle(): Int {
        return R.string.finances_movSources
    }

    override fun getEditActivityClass(): Class<*> {
        return MovementSourceEdit::class.java
    }

    override fun getOptionsFilter1(): List<String>? {
        return arrayListOf(
                getString(resources.getIdentifier("all.active", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.inactive", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.all", "string", "com.halkyproject.pausemenu"))
        )
    }

    override fun runOnBackground(): List<MovementSource> {
        return MovementSourceService.search(MovementSourceService.MovementSourceFilter(if (m_spinnerFilter1.selectedItemPosition == 2) null else m_spinnerFilter1.selectedItemPosition == 0))
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }

    override fun getFragmentClass(): Class<MovementSourceListFragment> {
        return MovementSourceListFragment::class.java
    }

}
