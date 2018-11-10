package com.halkyproject.pausemenu.activities

import com.halkyproject.lifehack.model.Company
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.CompanyCrudCardFragment
import com.halkyproject.pausemenu.singletons.CompanyService
import com.halkyproject.pausemenu.superclasses.GenericListingActivity

class CompanyCrud : GenericListingActivity<Company, CompanyCrud, CompanyCrudCardFragment>() {
    override fun getListTitle(): Int {
        return R.string.config_companies
    }

    override fun getEditActivityClass(): Class<*> {
        return CompanyEdit::class.java
    }

    override fun runOnBackground(): List<Company> {
//        val searchFilter = AccountService.FinancialAccountFilter(optionsAccountType[m_spinnerFilter1.selectedItemPosition], if (m_spinnerFilter2.selectedItemPosition == 2) null else m_spinnerFilter2.selectedItemPosition == 0)
        return CompanyService.findAll()
    }


    override fun getFragmentClass(): Class<CompanyCrudCardFragment> {
        return CompanyCrudCardFragment::class.java
    }

}
