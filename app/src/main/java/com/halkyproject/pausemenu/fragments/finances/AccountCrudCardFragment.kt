package com.halkyproject.pausemenu.fragments.finances

import android.icu.text.NumberFormat
import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.lifehack.model.finances.FinancialAccount.Companion.ACCOUNT_VALUES_TYPE_BANK
import com.halkyproject.pausemenu.activities.finances.FinancesAccountEdit
import com.halkyproject.pausemenu.interfaces.getResourceId
import com.halkyproject.pausemenu.singletons.FormatSingleton
import com.halkyproject.pausemenu.superclasses.GenericEntityFragment

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AccountCrudCardFragment : GenericEntityFragment<FinancialAccount>() {
    override fun getMainTitle(): String {
        return obj.name
    }

    override fun getSubTitle(): String {
        return if (ACCOUNT_VALUES_TYPE_BANK.contains(obj.type) && obj.currency == Currency.BRL) {
            "${obj.bankNumber} - ${obj.branch} - ${FormatSingleton.mask(obj.number
                    ?: "", FormatSingleton.FORMAT_FINANCIAL_ACCOUNT)}"
        } else {
            "N/A"
        }
    }

    override fun getBottomTextLeft(): String {
        return "<font color='${resources.getColor(obj.type.getColor().getResourceId(), null)}'>${getString(resources.getIdentifier(obj.type.getLocaleId(), "string", "com.halkyproject.pausemenu"))}</font>"
    }

    override fun getBottomTextRight(): String {
        return when {
            obj.type == FinancialAccount.AccountType.INFINITE_EXPENSE -> "<font color=\"#FF0000\">${NumberFormat.getCurrencyInstance(obj.currency.locale).currency.symbol} ∞</font>"
            obj.type == FinancialAccount.AccountType.INFINITE_EARNING -> "<font color=\"#00FF00\">${NumberFormat.getCurrencyInstance(obj.currency.locale).currency.symbol} ∞</font>"
            else -> NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.balance ?: 0)
        }
    }

    override fun getBgState(): BgState {
        return if (obj.active) {
            BgState.NORMAL
        } else {
            BgState.INACTIVE
        }
    }

    override fun getIdForAction(): Int? {
        return obj.id
    }

    override fun getEditClass(): Class<*> {
        return FinancesAccountEdit::class.java
    }

}
