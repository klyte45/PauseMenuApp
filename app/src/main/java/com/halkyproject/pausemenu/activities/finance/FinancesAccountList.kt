package com.halkyproject.pausemenu.activities.finance

import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.AccountCrudCardFragment
import com.halkyproject.pausemenu.singletons.AccountService
import com.halkyproject.pausemenu.superclasses.GenericListingActivity
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


class FinancesAccountList : GenericListingActivity<FinancialAccount, FinancesAccountList, AccountCrudCardFragment>(), AccountCrudCardFragment.OnFragmentInteractionListener {
    override fun getListTitle(): Int {
        return R.string.finances_accounts
    }

    private var optionsAccountType: List<FinancialAccount.AccountType?> = ArrayList()

    override fun getEditActivityClass(): Class<*> {
        return FinancesAccountEdit::class.java
    }

    override fun getOptionsFilter1(): List<String>? {
        val accountTypeList = arrayListOf(getString(resources.getIdentifier("all.allTypes", "string", "com.halkyproject.pausemenu")))
        optionsAccountType = ArrayList()
        (optionsAccountType as ArrayList<FinancialAccount.AccountType?>).add(null)
        for (type in FinancialAccount.AccountType.values()) {
            accountTypeList.add(getString(resources.getIdentifier(type.localeEntry, "string", "com.halkyproject.pausemenu")))
            (optionsAccountType as ArrayList<FinancialAccount.AccountType?>).add(type)
        }
        return accountTypeList
    }

    override fun getOptionsFilter2(): List<String>? {
        return arrayListOf(
                getString(resources.getIdentifier("all.active", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.inactive", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.all", "string", "com.halkyproject.pausemenu"))
        )
    }

    override fun runOnBackground(): List<FinancialAccount> {
        val searchFilter = AccountService.FinancialAccountFilter(optionsAccountType[m_spinnerFilter1.selectedItemPosition], if (m_spinnerFilter2.selectedItemPosition == 2) null else m_spinnerFilter2.selectedItemPosition == 0)
        return AccountService.search(searchFilter)
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }

    override fun getLoadingFrame(): ConstraintLayout {
        return m_loadingFrame
    }

    override fun getFragmentClass(): Class<AccountCrudCardFragment> {
        return AccountCrudCardFragment::class.java
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
