package com.halkyproject.pausemenu.activities.finances.reports

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import com.halkyproject.lifehack.model.aggregation.finances.CurrentBalanceReportItem
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.BasicTableTitleFragment
import com.halkyproject.pausemenu.fragments.finances.report.CurrentBalanceItemFragment
import com.halkyproject.pausemenu.fragments.finances.report.CurrentBalanceTotalFragment
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener
import com.halkyproject.pausemenu.singletons.finances.FinancesService
import com.halkyproject.pausemenu.superclasses.BasicActivity
import com.halkyproject.pausemenu.superclasses.BasicFragment
import kotlinx.android.synthetic.main.activity_finances_report_balances.*

class CurrentBalanceReport : BasicActivity(), OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    private class GetData : AsyncTask<CurrentBalanceReport, Void, CurrentBalanceReport>() {
        private lateinit var balances: Map<Currency, List<CurrentBalanceReportItem>>
        override fun doInBackground(vararg params: CurrentBalanceReport): CurrentBalanceReport {
            if (params.isEmpty()) throw IllegalStateException("Cadê a Activity?")
            balances = FinancesService.getCurrentBalances()
            return params[0]
        }

        override fun onPostExecute(result: CurrentBalanceReport?) {
            super.onPostExecute(result)
            try {
                with(result!!) {
                    if (supportFragmentManager.fragments.isNotEmpty()) {
                        val trRemove = supportFragmentManager.beginTransaction()
                        for (fragment in supportFragmentManager.fragments) {
                            trRemove.remove(fragment)
                        }
                        trRemove.commit()
                    }
                    if (balances.isNotEmpty()) {
                        val balances = balances.toSortedMap(compareBy { it.ordinal })
                        val trAdd = supportFragmentManager.beginTransaction()
                        for ((currency, values) in balances) {
                            val valuesOrd = values.sortedBy { it._id.type.ordinal }
                            val fragTt = BasicFragment.newInstance(getString(resources.getIdentifier("finances.currency.${currency.name}", "string", "com.halkyproject.pausemenu")), BasicTableTitleFragment::class.java)
                            trAdd.add(m_listing.id, fragTt, "title" + currency.name)
                            var sum = 0.0f
                            for (value in valuesOrd) {
                                val fragIt = BasicFragment.newInstance(value, CurrentBalanceItemFragment::class.java)
                                trAdd.add(m_listing.id, fragIt, "it" + value.toString())
                                sum += value.currentBalance.toFloat()
                            }
                            val fragIt = BasicFragment.newInstance(Pair(currency, sum), CurrentBalanceTotalFragment::class.java)
                            trAdd.add(m_listing.id, fragIt, "tot" + currency.name)
                        }
                        trAdd.commit()
                    }
                    closeLoadingScreen()
                }

            } catch (e: Exception) {
                Log.e("ERR!", "Erro post Execute: CurrentBalanceReport!", e)
            } finally {
                result?.closeLoadingScreen()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_report_balances)
        showLoadingScreen()
    }

    override fun onStart() = safeExecute({}(), true) {
        super.onStart()
        GetData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this)
    }
}