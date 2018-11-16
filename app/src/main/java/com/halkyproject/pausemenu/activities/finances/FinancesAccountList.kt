package com.halkyproject.pausemenu.activities.finances

import android.support.constraint.ConstraintLayout
import android.widget.LinearLayout
import com.halkyproject.lifehack.interfaces.Localizable
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.AccountCrudCardFragment
import com.halkyproject.pausemenu.singletons.finances.AccountService
import com.halkyproject.pausemenu.superclasses.GenericListingActivity
import com.halkyproject.pausemenu.wrappers.DefaultColorI18nWrapper
import com.halkyproject.pausemenu.wrappers.DefaultI18nWrapper
import kotlinx.android.synthetic.main.activity__basic_listing_2filters.*


class FinancesAccountList : GenericListingActivity<FinancialAccount, FinancesAccountList, AccountCrudCardFragment>() {
    override fun getListTitle(): Int {
        return R.string.finances_accounts
    }

    private lateinit var optionsAccountType: MutableList<DefaultI18nWrapper<*>>

    override fun getEditActivityClass(): Class<*> {
        return FinancesAccountEdit::class.java
    }

    override fun getOptionsFilter1(): List<Any>? {
        optionsAccountType = arrayListOf(
                DefaultI18nWrapper(this, Localizable.Adapter { "all.allTypes" })
        )
        optionsAccountType.addAll(FinancialAccount.AccountType.values().map { DefaultColorI18nWrapper(this, it) })
        return optionsAccountType
    }

    override fun getOptionsFilter2(): List<Any>? {
        return arrayListOf(
                getString(resources.getIdentifier("all.active", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.inactive", "string", "com.halkyproject.pausemenu")),
                getString(resources.getIdentifier("all.all", "string", "com.halkyproject.pausemenu"))
        )
    }

    override fun runOnBackground(): List<FinancialAccount> {
        val searchFilter = AccountService.FinancialAccountFilter(
                if (optionsAccountType[m_spinnerFilter1.selectedItemPosition].item is FinancialAccount.AccountType)
                    optionsAccountType[m_spinnerFilter1.selectedItemPosition].item as FinancialAccount.AccountType else null
                , if (m_spinnerFilter2.selectedItemPosition == 2) null else m_spinnerFilter2.selectedItemPosition == 0)
        return AccountService.search(searchFilter)
    }

    override fun getScrollLayout(): LinearLayout {
        return scrollLayout
    }

    override fun getFragmentClass(): Class<AccountCrudCardFragment> {
        return AccountCrudCardFragment::class.java
    }
}
