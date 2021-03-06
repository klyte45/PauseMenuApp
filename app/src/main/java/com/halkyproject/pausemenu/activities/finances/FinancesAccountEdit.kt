package com.halkyproject.pausemenu.activities.finances

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import com.halkyproject.lifehack.interfaces.Localizable
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.lifehack.model.finances.FinancialAccount.Companion.ACCOUNT_VALUES_TYPE_BANK
import com.halkyproject.lifehack.model.finances.FinancialAccount.Companion.ACCOUNT_VALUES_TYPE_NO_BALANCE
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SimpleSpinnerTypeAdapter
import com.halkyproject.pausemenu.singletons.FormatSingleton
import com.halkyproject.pausemenu.singletons.FormatSingleton.toBigDecimal
import com.halkyproject.pausemenu.singletons.finances.AccountService
import com.halkyproject.pausemenu.superclasses.BasicActivity
import com.halkyproject.pausemenu.superclasses.BasicFragment.Companion.KEY_EDIT_ID
import com.halkyproject.pausemenu.wrappers.DefaultColorI18nWrapper
import com.halkyproject.pausemenu.wrappers.DefaultI18nWrapper
import kotlinx.android.synthetic.main.activity_finances_account_edit.*
import java.math.BigDecimal


class FinancesAccountEdit : BasicActivity() {
    private var editingObject: FinancialAccount? = null

    private val defaultListenerSpinners = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) = safeExecute({}()) {
            redrawForm(Currency.values()[m_spinnerCurrency.selectedItemPosition], FinancialAccount.AccountType.values()[m_spinnerType.selectedItemPosition])
        }

        override fun onNothingSelected(parentView: AdapterView<*>?) = safeExecute({}()) {
            m_additionalAccountInfoGroup.visibility = View.GONE
            m_accountBalanceInfoGroup.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_account_edit)
        m_accountBalance.addTextChangedListener(FormatSingleton.maskNumberInput(m_accountBalance, resources.configuration.locale))
        m_accountLimit.addTextChangedListener(FormatSingleton.maskNumberInput(m_accountLimit, resources.configuration.locale))
        m_accountNumber.addTextChangedListener(FormatSingleton.mask(m_accountNumber, FormatSingleton.FORMAT_FINANCIAL_ACCOUNT))

        m_spinnerCurrency.adapter = SimpleSpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, Currency.values().map { DefaultI18nWrapper(this, Localizable.Adapter { "finances.currency.${it.name}" }) }, 14f, R.color.defaultMenuItemColor)
        m_spinnerCurrency.onItemSelectedListener = defaultListenerSpinners

        m_spinnerType.adapter = SimpleSpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, FinancialAccount.AccountType.values().map { DefaultColorI18nWrapper(this, it) }, 14f)
        m_spinnerType.onItemSelectedListener = defaultListenerSpinners

        val editId: Int = intent?.extras?.getInt(KEY_EDIT_ID) ?: -1

        if (editId != -1) {
            editingObject = AccountService.findById(editId)
            if (editingObject != null) {
                m_accountName.setText(editingObject!!.name, TextView.BufferType.EDITABLE)
                m_bankNumber.setText(editingObject!!.bankNumber, TextView.BufferType.EDITABLE)
                m_accountBranch.setText(editingObject!!.branch, TextView.BufferType.EDITABLE)
                m_accountNumber.setText(editingObject!!.number, TextView.BufferType.EDITABLE)
                m_spinnerType.setSelection(editingObject!!.type.ordinal)
                m_spinnerCurrency.setSelection(editingObject!!.currency.ordinal)


                for (x in arrayOf(m_spinnerType, m_spinnerType.selectedView, m_spinnerCurrency, m_spinnerCurrency.selectedView, m_bankNumber, m_accountBranch, m_accountNumber)) {
                    x?.isEnabled = false
                }
                if (editingObject!!.type in ACCOUNT_VALUES_TYPE_NO_BALANCE) {
                    m_accountBalanceInfoGroup.visibility = View.GONE
                } else {
                    m_accountBalanceInfoGroup.visibility = View.VISIBLE
                    m_accountBalance.setText(editingObject!!.balance!!.multiply(BigDecimal(100)).intValueExact().toString(), TextView.BufferType.EDITABLE)
                    m_accountLimit.setText(editingObject!!.creditLimit!!.multiply(BigDecimal(100)).intValueExact().toString(), TextView.BufferType.EDITABLE)
                }
                m_active.isChecked = editingObject!!.active
                if (!editingObject!!.active) {
                    for (x in arrayOf(m_active, m_accountLimit, m_accountBalance, m_accountName)) {
                        x.isEnabled = false
                    }
                    m_saveButton.visibility = View.GONE
                }
            }
        }
        if (editingObject == null) {
            m_spinnerType.setSelection(-1)
            m_active.visibility = View.GONE
        }
        closeLoadingScreen()

    }

    private fun redrawForm(currencySelected: Currency, accountTypeSelected: FinancialAccount.AccountType) = safeExecute({}()) {
        if (currencySelected == Currency.BRL && accountTypeSelected in ACCOUNT_VALUES_TYPE_BANK) {
            m_additionalAccountInfoGroup.visibility = View.VISIBLE
        } else {
            m_additionalAccountInfoGroup.visibility = View.GONE
        }
        if (accountTypeSelected in ACCOUNT_VALUES_TYPE_NO_BALANCE) {
            m_accountBalanceInfoGroup.visibility = View.GONE
        } else {
            m_accountBalanceInfoGroup.visibility = View.VISIBLE
        }
    }


    fun save(v: View) = safeExecute({}()) {

        if (editingObject != null && !m_active.isChecked) {
            confirmDelete(v)
        } else {
            execSave(v)
        }

    }

    fun confirmDelete(v: View) = safeExecute({}()) {
        linearLayout4.visibility = View.VISIBLE;
        with(titleLbl) {
            text = getString(R.string.all_removeCantUndo)
            maxLines = 2
            setSingleLine(false)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    fun cancelDelete(v: View) = safeExecute({}()) {
        linearLayout4.visibility = View.GONE;
        with(titleLbl) {
            text = getString(R.string.finances_editAccount)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 60f)
            maxLines = 1
            setSingleLine(true)
        }
    }

    fun execSave(v: View) = safeExecute({}()) {

        val nf = NumberFormat.getInstance() as DecimalFormat
        val decSep = nf.decimalFormatSymbols.decimalSeparator
        val tsdSep = nf.decimalFormatSymbols.groupingSeparator
        val accountName = m_accountName.text.toString()
        var bankNum: String? = m_bankNumber.text.toString()
        var branchVal: String? = m_accountBranch.text.toString()
        var accountNum: String? = FormatSingleton.unmask(m_accountNumber.text.toString())
        val accountBalance: BigDecimal = toBigDecimal(m_accountBalance.text.toString(), decSep, tsdSep)
        val credLimit: BigDecimal = toBigDecimal(m_accountLimit.text.toString(), decSep, tsdSep)
        if (m_spinnerType.selectedItemPosition == -1) {
            Toast.makeText(applicationContext, "Selecione o tipo de conta!", Toast.LENGTH_SHORT).show()
            return
        }
        if (m_spinnerCurrency.selectedItemPosition == -1) {
            Toast.makeText(applicationContext, "Selecione a moeda da conta!", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedAccountType = FinancialAccount.AccountType.values()[m_spinnerType.selectedItemPosition]
        val selectedCurrency = Currency.values()[m_spinnerCurrency.selectedItemPosition]

        if (accountName.length < 2) {
            Toast.makeText(applicationContext, "Nome muito curto!", Toast.LENGTH_SHORT).show()
            return
        }
        if (ACCOUNT_VALUES_TYPE_BANK.contains(selectedAccountType) && selectedCurrency == Currency.BRL) {
            if ((bankNum == null || bankNum.length != 3)) {
                Toast.makeText(applicationContext, "Número de banco inválido!", Toast.LENGTH_SHORT).show()
                return
            }
            if ((branchVal == null || branchVal.isEmpty())) {
                Toast.makeText(applicationContext, "Agência inválida!", Toast.LENGTH_SHORT).show()
                return
            }
            if ((accountNum == null || accountNum.isEmpty())) {
                Toast.makeText(applicationContext, "Número de conta inválido!", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            accountNum = null
            branchVal = null
            bankNum = null
        }

        showLoadingScreen()
        val obj: FinancialAccount = editingObject ?: FinancialAccount()
        with(obj) {
            if (editingObject == null) {
                branch = branchVal
                bankNumber = bankNum
                number = accountNum
                currency = selectedCurrency
                type = selectedAccountType
                active = true
            } else {
                active = m_active.isChecked
            }
            name = accountName
            if (type in ACCOUNT_VALUES_TYPE_NO_BALANCE) {
                balance = null
                creditLimit = null
            } else {
                balance = accountBalance
                creditLimit = credLimit
            }

            if (id == null) {
                AccountService.insert(this)
            } else {
                AccountService.update(this)
            }
        }
        Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }


}
