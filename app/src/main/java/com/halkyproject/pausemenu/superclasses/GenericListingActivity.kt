package com.halkyproject.pausemenu.superclasses

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.BasicEntityModel
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SpinnerTypeAdapter
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


abstract class GenericListingActivity<Entity, Activity, Fragment> : BasicListingActivity<Entity, GenericListingActivity<Entity, Activity, Fragment>, Fragment>() where Entity : BasicEntityModel, Activity : GenericListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

    abstract fun getEditActivityClass(): Class<*>
    abstract fun getListTitle(): Int

    open fun getOptionsFilter1(): List<String>? {
        return null
    }

    open fun getOptionsFilter2(): List<String>? {
        return null
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }

    override fun getLoadingFrame(): ConstraintLayout {
        return m_loadingFrame
    }

    private var optionsAccountType: List<FinancialAccount.AccountType?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__basic_listing_2filters)

        m_title.setText(getListTitle())

        val accountTypeList = arrayListOf(getString(resources.getIdentifier("all.allTypes", "string", "com.halkyproject.pausemenu")))
        optionsAccountType = ArrayList()
        (optionsAccountType as ArrayList<FinancialAccount.AccountType?>).add(null)
        for (type in FinancialAccount.AccountType.values()) {
            accountTypeList.add(getString(resources.getIdentifier(type.localeEntry, "string", "com.halkyproject.pausemenu")))
            (optionsAccountType as ArrayList<FinancialAccount.AccountType?>).add(type)
        }
        val options1 = getOptionsFilter1()
        val options2 = getOptionsFilter2()
        if (options1 != null) {
            m_spinnerFilter1.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, options1)
            m_spinnerFilter1.onItemSelectedListener = defaultSpinnerListener
        } else {
            m_spinnerFilter1.visibility = View.GONE
        }
        if (options2 != null) {
            m_spinnerFilter2.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, options2)
            m_spinnerFilter2.onItemSelectedListener = defaultSpinnerListener
        } else {
            m_spinnerFilter2.visibility = View.GONE
        }
        reload()
    }

    fun addNew(v: View) {
        startActivityForResult(Intent(this, getEditActivityClass()), 0)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
