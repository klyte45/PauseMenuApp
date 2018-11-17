package com.halkyproject.pausemenu.activities.finances

import android.os.Bundle
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.fragments.MovementSourcePrevisionListFragment
import com.halkyproject.pausemenu.singletons.finances.MovementSourcePrevisionService
import com.halkyproject.pausemenu.superclasses.GenericListingActivity
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


class MovementSourcePrevisionList : GenericListingActivity<MovementSourcePrevision, MovementSourcePrevisionList, MovementSourcePrevisionListFragment>() {
    companion object {
        const val KEY_PARENT = "PARENT"
    }

    var movSource: MovementSource? = null

    override fun beforeCreate(savedInstanceState: Bundle?) {
        movSource = (intent?.extras?.getSerializable(KEY_PARENT) as MovementSource?) ?: throw Exception("Objeto deve ser passado!")
    }

    override fun getListTitleStr(): String {
        return movSource!!.name
    }

    override fun getEditActivityClass(): Class<*> {
        return MovementSourcePrevisionEdit::class.java
    }

    override fun addToBundle(): Bundle {
        val bundle: Bundle = super.addToBundle()
        bundle.putSerializable(KEY_PARENT, movSource!!)
        return bundle
    }

//    override fun getOptionsFilter1(): List<String>? {
//        return arrayListOf(
//                getString(resources.getIdentifier("all.active", "string", "com.halkyproject.pausemenu")),
//                getString(resources.getIdentifier("all.inactive", "string", "com.halkyproject.pausemenu")),
//                getString(resources.getIdentifier("all.all", "string", "com.halkyproject.pausemenu"))
//        )
//    }

    override fun runOnBackground(): List<MovementSourcePrevision> {
        return MovementSourcePrevisionService.search(MovementSourcePrevisionService.Filter(movSource!!.id!!))
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }

    override fun getFragmentClass(): Class<MovementSourcePrevisionListFragment> {
        return MovementSourcePrevisionListFragment::class.java
    }

}
