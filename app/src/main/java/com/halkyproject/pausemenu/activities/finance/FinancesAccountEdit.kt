package com.halkyproject.pausemenu.activities.finance

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.ACCOUNT_VALUES_TYPE_BANK
import com.halkyproject.pausemenu.model.finances.Currency
import com.halkyproject.pausemenu.model.finances.FinancialAccount
import com.halkyproject.pausemenu.singletons.AccountService
import com.halkyproject.pausemenu.singletons.FormatSingleton
import com.halkyproject.pausemenu.singletons.FormatSingleton.toBigDecimal
import kotlinx.android.synthetic.main.activity_finances_account_edit.*
import java.math.BigDecimal

typealias T = FinancialAccount

class FinancesAccountEdit : AppCompatActivity() {
    private var editingObject: T? = null

    companion object {
        val KEY_EDIT_ID = "EditItemId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_account_edit)
        m_accountBalance.addTextChangedListener(FormatSingleton.maskNumberInput(m_accountBalance, resources.configuration.locale))
        m_accountLimit.addTextChangedListener(FormatSingleton.maskNumberInput(m_accountLimit, resources.configuration.locale))
        m_accountNumber.addTextChangedListener(FormatSingleton.mask(m_accountNumber, FormatSingleton.FORMAT_FINANCIAL_ACCOUNT))

        m_groupAccountType.setOnCheckedChangeListener { _, id ->
            if (id == op_savings.id || id == op_current.id) {
                m_additionalAccountInfoGroup.visibility = View.VISIBLE
            } else {
                m_additionalAccountInfoGroup.visibility = View.GONE
            }
        }

        val editId: Int = intent?.extras?.getInt(KEY_EDIT_ID) ?: -1

        if (editId != -1) {
            editingObject = AccountService.findById(editId)
            if (editingObject != null) {
                m_accountName.setText(editingObject!!.name, TextView.BufferType.EDITABLE)
                m_bankNumber.setText(editingObject!!.bankNumber, TextView.BufferType.EDITABLE)
                m_accountBranch.setText(editingObject!!.branch, TextView.BufferType.EDITABLE)
                m_accountNumber.setText(editingObject!!.number, TextView.BufferType.EDITABLE)
                when (editingObject!!.type) {
                    FinancialAccount.AccountType.VIRTUAL -> op_virtual.isChecked = true
                    FinancialAccount.AccountType.SAVINGS -> op_savings.isChecked = true
                    FinancialAccount.AccountType.CURRENT -> op_current.isChecked = true
                    FinancialAccount.AccountType.LOCAL -> op_local.isChecked = true
                }
                when (editingObject!!.currency) {
                    Currency.EUR -> op_euro.isChecked = true
                    Currency.USD -> op_dolar.isChecked = true
                    Currency.BRL -> op_real.isChecked = true
                }


                for (x in arrayOf(op_virtual, op_savings, op_current, op_local, op_euro, op_dolar, op_real, m_bankNumber, m_accountBranch, m_accountNumber)) {
                    x.isEnabled = false
                }
                m_accountBalance.setText(editingObject!!.balance!!.multiply(BigDecimal(100)).intValueExact().toString(), TextView.BufferType.EDITABLE)
                m_accountLimit.setText(editingObject!!.creditLimit!!.multiply(BigDecimal(100)).intValueExact().toString(), TextView.BufferType.EDITABLE)
            }
        }
        m_loadingFrame.visibility = View.GONE
    }


    fun save(v: View) {
        val nf = NumberFormat.getInstance() as DecimalFormat
        val decSep = nf.decimalFormatSymbols.decimalSeparator
        val tsdSep = nf.decimalFormatSymbols.groupingSeparator
        val accountName = m_accountName.text.toString()
        var bankNum: String? = m_bankNumber.text.toString()
        var branch: String? = m_accountBranch.text.toString()
        var accountNum: String? = FormatSingleton.unmask(m_accountNumber.text.toString())
        val accountBalance: BigDecimal = toBigDecimal(m_accountBalance.text.toString(), decSep, tsdSep)
        val credLimit: BigDecimal = toBigDecimal(m_accountLimit.text.toString(), decSep, tsdSep)
        val selectedAccountType = when (m_groupAccountType.checkedRadioButtonId) {
            op_current.id -> FinancialAccount.AccountType.CURRENT
            op_local.id -> FinancialAccount.AccountType.LOCAL
            op_savings.id -> FinancialAccount.AccountType.SAVINGS
            op_virtual.id -> FinancialAccount.AccountType.VIRTUAL
            else -> null
        }
        val selectedCurrency = when (m_groupCurrency.checkedRadioButtonId) {
            op_real.id -> Currency.BRL
            op_dolar.id -> Currency.USD
            op_euro.id -> Currency.EUR
            else -> null
        }

        if (selectedAccountType == null) {
            Toast.makeText(applicationContext, "Selecione o tipo de conta!", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedCurrency == null) {
            Toast.makeText(applicationContext, "Selecione a moeda da conta!", Toast.LENGTH_SHORT).show()
            return
        }
        if (accountName.length < 2) {
            Toast.makeText(applicationContext, "Nome muito curto!", Toast.LENGTH_SHORT).show()
            return
        }
        if (ACCOUNT_VALUES_TYPE_BANK.contains(selectedAccountType) && selectedCurrency == Currency.BRL) {
            if ((bankNum == null || bankNum.length != 3)) {
                Toast.makeText(applicationContext, "Número de banco inválido!", Toast.LENGTH_SHORT).show()
                return
            }
            if ((branch == null || branch.isEmpty())) {
                Toast.makeText(applicationContext, "Agência inválida!", Toast.LENGTH_SHORT).show()
                return
            }
            if ((accountNum == null || accountNum.isEmpty())) {
                Toast.makeText(applicationContext, "Número de conta inválido!", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            accountNum = null
            branch = null
            bankNum = null
        }

        m_loadingFrame.visibility = View.VISIBLE
        try {
            val obj: T = editingObject ?: T()
            if (editingObject == null) {
                obj.branch = branch
                obj.bankNumber = bankNum
                obj.number = accountNum
                obj.currency = selectedCurrency
                obj.type = selectedAccountType
            }
            obj.name = accountName
            obj.balance = accountBalance
            obj.creditLimit = credLimit

            if (obj.id == null) {
                if (!AccountService.insert(obj).get()) {
                    throw java.lang.Exception("Erro na persistência!")
                }
            } else {
                if (!AccountService.update(obj).get()) {
                    throw java.lang.Exception("Erro na persistência!")
                }
            }
            Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Erro ao salvar: " + e.message, Toast.LENGTH_SHORT).show()
            Log.e("ERROR", "Erro salvando conta financeira", e)
            m_loadingFrame.visibility = View.GONE
        }
    }

}
