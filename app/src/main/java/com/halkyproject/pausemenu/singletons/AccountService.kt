//com.halkyproject.pausemenu.singletons=com.halkyproject.lifehack.model.finances.FinancialAccount=AccountService;
package com.halkyproject.pausemenu.singletons

import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.superclasses.BasicEntityService


object AccountService : BasicEntityService<FinancialAccount, AccountService.FinancialAccountFilter>() {
    override fun getClassEntity(): Class<FinancialAccount> {
        return FinancialAccount::class.java
    }

    override fun getClassEntityArray(): Class<Array<FinancialAccount>> {
        return Array<FinancialAccount>::class.java
    }

    override fun getBaseUrl(): String {
        return FinancialAccount.BASE_URL
    }

    class FinancialAccountFilter(val type: FinancialAccount.AccountType?, val active: Boolean?)
}