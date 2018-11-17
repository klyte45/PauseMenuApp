package com.halkyproject.pausemenu.superclasses

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.halkyproject.lifehack.interfaces.BasicEntityModel
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SpinnerTypeAdapter
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


abstract class GenericListingActivity<Entity, Activity, Fragment> : BasicListingActivity<Entity, GenericListingActivity<Entity, Activity, Fragment>, Fragment>() where Entity : BasicEntityModel<Entity>, Activity : GenericListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

    abstract fun getEditActivityClass(): Class<*>
    open fun getListTitle(): Int? {
        return null
    }

    open fun getListTitleStr(): String? {
        return null
    }

    open fun beforeCreate(savedInstanceState: Bundle?) {}

    open fun getOptionsFilter1(): List<Any>? {
        return null
    }

    open fun getOptionsFilter2(): List<Any>? {
        return null
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }


    open fun addToBundle(): Bundle {
        return Bundle()
    }

    private var optionsAccountType: List<FinancialAccount.AccountType?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__basic_listing_2filters)

        beforeCreate(savedInstanceState)

        if (getListTitle() != null) {
            m_title.setText(getListTitle()!!)
        } else if (getListTitleStr() != null) {
            m_title.text = getListTitleStr()
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

    fun addNew(v: View) = safeExecute({}()) {
        val intent = Intent(this, getEditActivityClass())
        val b = Bundle()
        b.putAll(addToBundle())
        b.putInt(BasicFragment.KEY_EDIT_ID, -1)
        intent.putExtras(b)
        startActivity(intent)
    }

    override fun onFragmentInteraction(uri: Uri) = safeExecute({}()) {

    }
}
