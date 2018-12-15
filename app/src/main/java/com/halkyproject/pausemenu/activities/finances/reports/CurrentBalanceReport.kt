package com.halkyproject.pausemenu.activities.finances.reports

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.aggregation.finances.CurrentBalanceReportItem
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.DefaultListPageAdapter
import com.halkyproject.pausemenu.fragments.BasicTableTitleFragment
import com.halkyproject.pausemenu.fragments.finances.report.BasicAccountingTotal
import com.halkyproject.pausemenu.fragments.finances.report.CurrentBalanceItemFragment
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener
import com.halkyproject.pausemenu.singletons.finances.FinancesService
import com.halkyproject.pausemenu.superclasses.BasicActivity
import com.halkyproject.pausemenu.superclasses.BasicFragment
import kotlinx.android.synthetic.main.activity__basic_report.*

class CurrentBalanceReport : BasicActivity(), OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    private lateinit var viewListing: LinearLayout

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
                            trAdd.add(R.id.tt_title, fragTt, "title" + currency.name)

                            viewListing.addView(fragTt.view)

                            var sum = 0.0f
                            for (value in valuesOrd) {
                                val fragIt = BasicFragment.newInstance(value, CurrentBalanceItemFragment::class.java)
                                trAdd.add(R.id.tt_title, fragIt, "it" + value.toString())
                                sum += value.currentBalance.toFloat()
                                viewListing.addView(fragIt.view)
                            }
                            val fragIt = BasicFragment.newInstance(sum, BasicAccountingTotal::class.java)
                            trAdd.add(R.id.tt_title, fragIt, "tot" + currency.name)
                            viewListing.addView(fragIt.view)
                        }
                        trAdd.commit()
                    }
                    closeLoadingScreen()
                }

            } catch (e: Exception) {
                result!!.runOnUiThread { result.onErrorThrown(e) }
            } finally {
                result?.closeLoadingScreen()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__basic_report)
        tt_title.text = getString(R.string.finances_currentBalance)
        GetData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this)
        showLoadingScreen()
        m_pager.adapter = DefaultListPageAdapter(this)
        val item = ((m_pager.adapter as DefaultListPageAdapter).instantiateItem(this.m_pager, 0) as ViewGroup)
        Log.w("XXX", "$item")
        viewListing = item.findViewById(R.id.m_listing)

    }
}